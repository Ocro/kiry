package controllers;

import static play.data.Form.form;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.login;
import views.html.subscribe;

public class Application extends Controller {

  public static class Login {

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

    public boolean rememberMe;

    public String validate() {
      if (User.authenticate(email, password) == null) {
        return "Informations incorrectes";
      }
      return null;
    }
  }

  public static class CreateAccount {

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String name;

    public boolean rememberMeCreateAccount;

    public String validate() {
      try {
        new User(email, password, name).save();
      } catch (Exception e) {
        return e.getMessage();
      }
      return null;
    }
  }

  public static Result login() {
    Http.Cookie rememberme = request().cookies().get("rememberme");

    if (rememberme != null && rememberme.value() != "") {
      authUser(User.findByAuthKey(rememberme.value()).email);
      return redirect(routes.Kiry.index(2));
    }
    return ok(login.render(form(Login.class)));
  }

  public static Result authenticate() {
    Form<Login> loginForm = form(Login.class).bindFromRequest();
    if (loginForm.hasErrors()) {
      return badRequest(login.render(loginForm));
    } else {
      authUser(loginForm.get().email);
      if (loginForm.get().rememberMe)
        rememberMe(loginForm.get().email, loginForm.get().password);
      return redirect(routes.Kiry.index(2));
    }
  }

  public static Result subscribe() {
    return ok(subscribe.render(form(CreateAccount.class)));
  }

  public static Result createAccount() {
    Form<CreateAccount> createAccountForm = form(CreateAccount.class)
        .bindFromRequest();
    if (createAccountForm.hasErrors()) {
      return badRequest(subscribe.render(createAccountForm));
    } else {
      authUser(createAccountForm.get().email);
      if (createAccountForm.get().rememberMeCreateAccount)
        rememberMe(createAccountForm.get().email,
            createAccountForm.get().password);
      return redirect(routes.Kiry.welcome());
    }
  }

  private static void authUser(String email) {
    session("email", email);
  }

  private static void rememberMe(String email, String password) {
    String salt = Double.toString(Math.random() * 1000);
    String cookie = play.libs.Crypto.sign(email + password + salt);
    response().setCookie("rememberme", cookie, 2592000);
    User u = User.findByEmail(email);
    u.authkey = cookie;
    u.save();
  }

  /**
   * Logout and clean the session.
   */
  public static Result logout() {
    session().clear();
    response().discardCookie("rememberme");
    flash("success", "You've been logged out");
    return redirect(routes.Kiry.index(2));
  }
}
