package controllers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import play.mvc.Result;
import static play.test.Helpers.*;

public class SignInTest {

    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAuthenticate() {
        Result res = null;// -- Authenticate success

        res = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email", "bob@example.com", "password", "secret"))
        );
        assertEquals(303, status(res));
        assertEquals("bob@example.com", session(res).get("email"));

        // -- Authenticate failure

        res = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email", "bob@example.com", "password", "badpassword"))
        );
        assertEquals(400, status(res));
        assertNull(session(res).get("email"));

        res = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email", "unknown@example.com", "password", "secret"))
        );
        assertEquals(400, status(res));
        assertNull(session(res).get("email"));
    }

    @Test
    public void testAuthenticator() {
        Result res = null;

        // -- Authenticator authenticate

        res = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest().withSession("email", "bob@example.com")
        );
        assertEquals(200, status(res));

        // -- Authenticator not authenticate

        res = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest()
        );
        assertEquals(303, status(res));
        assertEquals("/signin", header("Location", res));
    }

    @Ignore
    @Test
    public void testI18N() {
        Result res = null;

        res = callAction(
                controllers.routes.ref.Application.setLanguage("en-US"),
                fakeRequest()
        );

        // TODO Have to find a way to test cookies!
    }
}
