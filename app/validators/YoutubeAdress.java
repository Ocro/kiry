package validators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = YoutubeValidator.class)
@play.data.Form.Display(name = "constraint.youtubeadress")
public @interface YoutubeAdress {
  String message() default YoutubeValidator.message;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
