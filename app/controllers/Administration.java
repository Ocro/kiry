package controllers;

import models.Game;
import play.*;
import play.data.*;
import play.mvc.*;
import javax.inject.*;
import play.data.validation.Constraints;
import views.html.administration;


@Security.Authenticated(Secured.class)
public class Administration extends Controller {

  @Inject FormFactory formFactory;

  public static class FormCleanGame {

    @Constraints.Required
    public String email;

    @Constraints.Required
    public Long fromID;

    public String validate() {
      return null;
    }
  }

  public Result index() {
    if (Secured.isAdmin())
      return ok(administration.render(formFactory.form(FormCleanGame.class)));
    else
      return forbidden();
  }
  
  public Result cleanGame() {
    if (!Secured.isAdmin())
      return forbidden();
    
    Form<FormCleanGame> cleanGame = formFactory.form(FormCleanGame.class).bindFromRequest();

    // Check du formulaire
    if (cleanGame.hasErrors()) 
      return badRequest(administration.render(cleanGame));

    Game.cleanGameFrom(cleanGame.get().email, cleanGame.get().fromID);
    return ok(administration.render(cleanGame));
  }
}
