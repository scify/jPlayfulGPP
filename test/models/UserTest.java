package models;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;

import static play.test.Helpers.*;

public class UserTest extends TestCase {
    @Override
    @Before
    public void setUp() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testit() {
        Result res = null;
        Map<String, String> fakeFormFields = new HashMap<String, String>();

        // -- Count user
        assertEquals(3, User.find.findRowCount());

        // -- Authenticate users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNotNull(User.authenticate("jeff@example.com", "secret"));

        assertNull(User.authenticate("bob@example.com", "badpassword"));
        assertNull(User.authenticate("jane@example.com", "badpassword"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));

        assertNull(User.authenticate("tom@example.com", "secret"));

        // -- Register success
        assertNull(User.authenticate("success@example.com", "secret"));

        fakeFormFields.put("email", "success@example.com");
        fakeFormFields.put("confirm-email", "success@example.com");
        fakeFormFields.put("name", "Foo");
        fakeFormFields.put("surname", "Bar");
        fakeFormFields.put("password", "secret");
        fakeFormFields.put("confirm-password", "secret");
        res = callAction(
                controllers.routes.ref.Application.register(),
                fakeRequest().withFormUrlEncodedBody(fakeFormFields)
        );
        assertEquals(303, status(res));
        assertNotNull(User.authenticate("success@example.com", "secret"));

        // -- Register failure

        // Empty email field
        fakeFormFields.clear();
        fakeFormFields.put("email", "");
        fakeFormFields.put("confirm-email", "failur@example.com");
        fakeFormFields.put("name", "Foo");
        fakeFormFields.put("surname", "Bar");
        fakeFormFields.put("password", "secret");
        fakeFormFields.put("confirm-password", "secret");
        res = callAction(
                controllers.routes.ref.Application.register(),
                fakeRequest().withFormUrlEncodedBody(fakeFormFields)
        );
        assertEquals(400, status(res));
        assertNull(User.authenticate("failure@example.com", "secret"));

        // Wrong email confirmation
        fakeFormFields.clear();
        fakeFormFields.put("email", "failure@example.com");
        fakeFormFields.put("confirm-email", "failur@example.com");
        fakeFormFields.put("name", "Foo");
        fakeFormFields.put("surname", "Bar");
        fakeFormFields.put("password", "secret");
        fakeFormFields.put("confirm-password", "secret");
        res = callAction(
                controllers.routes.ref.Application.register(),
                fakeRequest().withFormUrlEncodedBody(fakeFormFields)
        );
        assertEquals(400, status(res));
        assertNull(User.authenticate("failure@example.com", "secret"));

        // Empty password field
        fakeFormFields.clear();
        fakeFormFields.put("email", "failure@example.com");
        fakeFormFields.put("confirm-email", "failure@example.com");
        fakeFormFields.put("name", "Foo");
        fakeFormFields.put("surname", "Bar");
        fakeFormFields.put("password", "");
        fakeFormFields.put("confirm-password", "secet");
        res = callAction(
                controllers.routes.ref.Application.register(),
                fakeRequest().withFormUrlEncodedBody(fakeFormFields)
        );
        assertEquals(400, status(res));
        assertNull(User.authenticate("failure@example.com", "secret"));

        // Wrong password confirmation
        fakeFormFields.clear();
        fakeFormFields.put("email", "failure@example.com");
        fakeFormFields.put("confirm-email", "failure@example.com");
        fakeFormFields.put("name", "Foo");
        fakeFormFields.put("surname", "Bar");
        fakeFormFields.put("password", "secret");
        fakeFormFields.put("confirm-password", "secet");
        res = callAction(
                controllers.routes.ref.Application.register(),
                fakeRequest().withFormUrlEncodedBody(fakeFormFields)
        );
        assertEquals(400, status(res));
        assertNull(User.authenticate("failure@example.com", "secret"));
    }
}
