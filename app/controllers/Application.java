package controllers;

import javax.persistence.Id;

import models.Evaluation;
import models.User;
import play.*;
import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.*;
import play.mvc.Http.HeaderNames;
import scala.App;

import views.html.*;
import views.html.defaultpages.todo;

/**
 * The controller class for the application action methods.
 *
 * @author billy
 *
 */
public class Application extends Controller {
    // -- Welcome

    /**
     * <p>The welcome page action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/welcome.scala.html} as a body.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    public static Result welcome() {
        return ok(welcome.render());
    }

    // -- Home

    /**
     * <p>The index / home page action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/index.scala.html} as a body.
     * </p>
     *
     * <p>
     * Annotated with the {@link Secured} authenticator.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render(User.find.byId(request().username())));
    }

    // -- Sign-up

    /**
     * <p>The sign-up action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/signup.scala.html} as a body.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    public static Result signUp() {

        return ok(
                signup.render(form(User.class))
        );
    }

    /**
     * <p>The registration action.</p>
     *
     * <p>
     * Checks the submitted form for errors. If no errors were found it
     * constructs a {@code 303 SEE_OTHER} HTTP response pointing to
     * {@link Application.index()} action. In case of errors it prepares a
     * {@code 400 BAD_REQUEST} HTTP response with
     * {@code app/views/signup.scala.html} as body.
     * </p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} if the request has
     *         no errors; a {@code 400 BAD_REQUEST} HTTP {@link Result}
     *         otherwise.
     */
    public static Result register() {
        Result res = null;
        Form<User> form = form(User.class).bindFromRequest();

        // Check if e-mail confirmation is successful
        if(!form.field("email").value().equals(form.field("confirm-email").value())) {
            form.reject("confirm-email", "Emails should match");
        }

        // Check if password confirmation is successful
        if(!form.field("password").value().equals(form.field("confirm-password").value())) {
            form.reject("confirm-password", "Passwords should match");
        }

        if(form.hasErrors()) {
            res = badRequest(signup.render(form));
        }
        else {
            User user = form.get();
            user.save();
            session().clear();
            session("email", form.get().email);
            res = redirect(routes.Application.index());
        }

        return res;
    }

    // -- Sign-in / Log-in

    /**
     * The inner class that backs the sign-in page.
     *
     * @author billy
     *
     */
    public static class SignIn {
        /**
         * The e-mail to be authenticated.
         */
        @Required(message = "Please enter your e-mail")
        public String email;

        /**
         * The password with which to authenticate the e-mail.
         */
        @Required(message = "Please enter your password")
        public String password;

        /**
         * <p>Validates the email and the password given.</p>
         *
         * <p>
         * Uses the {@link models.User.authenticate()} helper method to
         * authenticate the information given.
         * </p>
         *
         * @return A {@code null} value if the validation passes or a
         *         {@link String} with an error message, if it fails.
         */
        public String validate() {
            String ret = null;
            if (User.authenticate(email, password) == null) {
                ret = "Invalid user or password";
            }

            return ret;
        }
    }

    /**
     * <p>The sign-in action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/signin.scala.html} as a body.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    public static Result signIn() {
        return ok(
                signin.render(form(SignIn.class))
        );
    }

    /**
     * <p>The authentication action.</p>
     *
     * <p>
     * Checks the submitted form for errors. If no errors were found it
     * constructs a {@code 303 SEE_OTHER} HTTP response pointing to
     * {@link Application.index()} action. In case of errors it prepares a
     * {@code 400 BAD_REQUEST} HTTP response with
     * {@code app/views/signin.scala.html} as body.
     * </p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} if the request has
     *         no errors; a {@code 400 BAD_REQUEST} HTTP {@link Result}
     *         otherwise.
     */
    public static Result authenticate() {
        Result res = null;
        // Create a form filled with the requested data.
        Form<SignIn> form = form(SignIn.class).bindFromRequest();

        if (form.hasErrors()) {
            res = badRequest(signin.render(form));
        }
        else {
            session().clear();
            session("email", form.get().email);
            res = redirect(routes.Application.index());
        }

        return res;
    }

    // -- Sign-out / Log-out

    /**
     * <p>The sign-out action.</p>
     *
     * <p>
     * Cleans up the session and adds a success message to the flash scope.
     * </p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} pointing to
     * {@link Application.signIn()} action.
     */
    public static Result signOut() {
        session().clear();
        flash("success", "You've successfully signed out");

        return redirect(routes.Application.signIn());
    }

    // -- Evaluation

    /**
     * <p>The evaluation action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/evaluation.scala.html} as a body.
     * </p>
     *
     * <p>
     * Annotated with the {@link Secured} authenticator.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    @Security.Authenticated(Secured.class)
    public static Result evaluation() {
        return ok(evaluation.render(form(Evaluation.class), User.find.byId(request().username())));
    }

    /**
     * <p>The evaluation submission action.</p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result}.
     */
    public static Result evaluate() {
        Result res = null;
        Form<Evaluation> form = form(Evaluation.class).bindFromRequest();

        if (form.hasErrors()) {
            res = badRequest(evaluation.render(form, User.find.byId(request().username())));
        }
        else {
            Evaluation evaluation = form.get();
            evaluation.save();
            flash("success", "Thank you for your time. Your evaluation has been successfully saved.");
            res =  redirect(routes.Application.index());
        }

        return res;
    }

    /**
     * <p>The set language action.</p>
     *
     * @param code
     *     The language to be set.
     *
     * @return
     *     A {@code 303 SEE_OTHER} HTTP {@link Result}. Always redirects to
     *     the referer.
     */
    public static Result setLanguage(String code) {
        Result res = null;

        changeLang(code);

        String[] refererList = request().headers().get(HeaderNames.REFERER);
        String referer = (refererList.length != 0 ? refererList[0] : null);

        if (referer != null) {
            res = redirect(referer);
        }
        else {
            res = redirect(routes.Application.welcome());
        }

        return res;
    }
}