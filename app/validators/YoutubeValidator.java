package validators;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.libs.F;

/**
 * Created with IntelliJ IDEA. User: David Date: 03.04.13 Time: 16:21 To change
 * this template use File | Settings | File Templates.
 */
public class YoutubeValidator extends Constraints.Validator<Object> implements
    ConstraintValidator<YoutubeAdress, Object> {
  /* Default error message */
  final static public String message = "Le lien vidéo youtube n'est pas valide";

  /**
   * Validator init Can be used to initialize the validation based on parameters
   * passed to the annotation.
   */
  public void initialize(YoutubeAdress constraintAnnotation) {
  }

  @Override
  public F.Tuple<String, Object[]> getErrorMessageKey() {
    return null;
  }

  /**
   * The validation itself
   */
  public boolean isValid(Object object) {
    if (object == null)
      return false;

    if (!(object instanceof String))
      return false;

    String s = object.toString();

    if (s.equals(""))
      return true;

    if (!s.contains("youtube.com"))
      return false;
    if (!s.contains("watch?"))
      return false;
    try {
      URL url = new URL(s);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      if (connection.getResponseCode() != 200)
        return false;
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Constructs a validator instance.
   */
  public static play.data.validation.Constraints.Validator<Object> youtubeAdress() {
    return new YoutubeValidator();
  }
}
