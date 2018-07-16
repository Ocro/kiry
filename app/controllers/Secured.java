package controllers;

import models.Admin;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

  @Override
  public Result onUnauthorized(Http.Context context) {
    return redirect(routes.Application.login());
  }

  @Override
  public String getUsername(Http.Context context) {
    return context.session().get("email");
  }
  
  public static boolean isAdmin() {
    return Admin.isAdmin(Kiry.getLoggedUser());
  }
}
