package ingokuba.wolpertinger.image.entity;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ImageValidator.class})
public @interface ValidImage
{

    String message() default "{wolpertinger.validation.Image}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
