package controllers;
import static play.data.Form.form;
import models.Game;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.administration;


@Security.Authenticated(Secured.class)
public class Administration extends Controller {

  public static class FormCleanGame {

    @Constraints.Required
    public String email;

    @Constraints.Required
    public Long fromID;

    public String validate() {
      return null;
    }
  }

  public static Result index() {
    if (Secured.isAdmin())
      return ok(administration.render(form(FormCleanGame.class)));
    else
      return forbidden();
  }
  
  public static Result cleanGame() {
    if (!Secured.isAdmin())
      return forbidden();
    
    Form<FormCleanGame> cleanGame = form(FormCleanGame.class).bindFromRequest();

    // Check du formulaire
    if (cleanGame.hasErrors()) 
      return badRequest(administration.render(cleanGame));

    Game.cleanGameFrom(cleanGame.get().email, cleanGame.get().fromID);
    return ok(administration.render(cleanGame));
  }
}