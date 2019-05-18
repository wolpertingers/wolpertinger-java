package ingokuba.wolpertinger.order.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ReferenceValidator.class})
public @interface ValidReference
{

    String message() default "{wolpertinger.validation.Order}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    LevelConstraint[] levelConstraints();
}
