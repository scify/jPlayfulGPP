package models;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;

import junit.framework.TestCase;

import static play.test.Helpers.*;

public class EvaluationTest extends TestCase {

    @Override
    @Before
    protected void setUp() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        super.setUp();
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testit() {
        Result res = null;
        Map<String, String> fakeFormFields = new HashMap<String, String>();

        // -- Evaluation submission successfull

        fakeFormFields.put("q1", "Answer for question #1");
        fakeFormFields.put("q2", "Answer for question #2");
        fakeFormFields.put("q3", "Answer for question #3");
        res = callAction(
                controllers.routes.ref.Application.evaluate(),
                fakeRequest().withFormUrlEncodedBody(fakeFormFields)
        );
        assertEquals(303, status(res));
        assertNotNull(flash(res).get("success"));

    }

}
