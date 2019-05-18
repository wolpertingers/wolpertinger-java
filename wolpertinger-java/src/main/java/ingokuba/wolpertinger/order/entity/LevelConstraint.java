package ingokuba.wolpertinger.order.entity;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({})
@Retention(RUNTIME)
public @interface LevelConstraint
{

    String message() default "{wolpertinger.validation.Order.images.level}";

    int level();

    int amount();
}
