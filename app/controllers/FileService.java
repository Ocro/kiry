package controllers;

import java.io.File;

import play.mvc.Controller;
import play.mvc.Result;

public class FileService extends Controller {
  static String path = "public/";

  public static Result getFile(String file) {
    File myfile = new File(path + file);
    return ok(myfile);
  }
}