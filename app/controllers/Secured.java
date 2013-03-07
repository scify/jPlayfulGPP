package controllers;

import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;

/**
 * <p>The application authenticator.</p>
 *
 * <p>
 * Method {@code getUsername()} is used to get the user name of the current
 * logged in user from the session. If the method returns a value, the
 * authenticator considers the user to be logged in and lets the request to
 * proceed. If it returns {@code null} then it blocks the request and invoke
 * {@code onUnauthorized} which redirects to the sign-in screen.
 * </p>
 *
 * @author billy
 *
 */
public class Secured extends Security.Authenticator {
    /* (non-Javadoc)
     * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
     */
    @Override
    public String getUsername(Context ctx) {
        // In our case the user name is the e-mail address.
        return ctx.session().get("email");
    }

    /* (non-Javadoc)
     * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
     */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.signIn());
    }
}
