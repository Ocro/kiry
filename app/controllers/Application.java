package controllers;

import models.User;
import play.*;
import play.data.*;
import play.mvc.*;
import views.html.login;
import views.html.subscribe;
import javax.inject.*;
import play.data.validation.Constraints;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validate;
import play.data.validation.Constraints.Validatable;


public class Application extends Controller {

  @Inject FormFactory formFactory;

  public static class Login implements Validatable<String>  {

    @Constraints.Required
    protected String email;

    @Constraints.Required
    protected String password;

    public boolean rememberMe;
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public boolean setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
        return rememberMe;
    }
    
    public boolean getRememberMe() {
        return this.rememberMe;
    }
    
    @Override
    public String validate() {
      if (User.authenticate(email, password) == null) {
        return "Informations incorrectes";
      }
      return null;
    }
  }

  public static class CreateAccount implements Validatable<String> {

    @Constraints.Required
    protected String name;

    @Constraints.Required
    protected String email;

    @Constraints.Required
    protected String password;
    
    public boolean rememberMeCreateAccount;
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean setRememberMeCreateAccount(boolean rememberMe) {
        this.rememberMeCreateAccount = rememberMe;
        return rememberMe;
    }
    
    public boolean getRememberMeCreateAccount() {
        return this.rememberMeCreateAccount;
    }

    public String validate() {
      try {
        new User(email, password, name).save();
      } catch (Exception e) {
        return e.getMessage();
      }
      return null;
    }
  }

  public Result login() {
    Http.Cookie rememberme = request().cookies().get("rememberme");

    if (rememberme != null && rememberme.value() != "") {
      authUser(User.findByAuthKey(rememberme.value()).email);
      return redirect(routes.Kiry.index(2));
    }
    return ok(login.render(formFactory.form(Login.class)));
  }

  public Result authenticate() {
  System.out.println("test");
    Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

    if (loginForm.hasErrors()) {
      return badRequest(login.render(loginForm));
    } else {
      authUser(loginForm.get().email);
      if (loginForm.get().rememberMe)
        rememberMe(loginForm.get().email, loginForm.get().password);
      return redirect(routes.Kiry.index(2));
    }
  }

  public Result subscribe() {
    return ok(subscribe.render(formFactory.form(CreateAccount.class)));
  }

  public Result createAccount() {
    Form<CreateAccount> createAccountForm = formFactory.form(CreateAccount.class).bindFromRequest();
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
    String cookie = /*play.libs.Crypto.sign(*/email + password + salt/*)*/;
    response().setCookie("rememberme", cookie, 2592000, "user/passwd", "kiry.com", false, true, Http.Cookie.SameSite.STRICT);
    User u = User.findByEmail(email);
    u.authkey = cookie;
    u.save();
  }

  /**
   * Logout and clean the session.
   */
  public Result logout() {
    session().clear();
    response().discardCookie("rememberme");
    flash("success", "You've been logged out");
    return redirect(routes.Kiry.index(2));
  }
}
