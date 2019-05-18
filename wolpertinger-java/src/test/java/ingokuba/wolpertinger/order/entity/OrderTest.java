package ingokuba.wolpertinger.order.entity;

import static ingokuba.wolpertinger.entity.EntitySetup.images;
import static ingokuba.wolpertinger.entity.EntitySetup.validImage;
import static ingokuba.wolpertinger.entity.EntitySetup.validImageReference;
import static ingokuba.wolpertinger.entity.EntitySetup.validOrder;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import ingokuba.wolpertinger.image.entity.Image;

public class OrderTest
{

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_accept_valid_order_with_all_attributes()
    {
        Order order = validOrder().setId(2L).setComment("This is a comment.");

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void should_fail_for_missing_orderer()
    {
        Order order = validOrder().setOrderer(null);

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("Name of the orderer is mandatory"));
    }

    @Test
    public void should_fail_for_empty_orderer()
    {
        Order order = validOrder().setOrderer("");

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
    }

    @Test
    public void should_fail_for_orderer_too_short()
    {
        Order order = validOrder().setOrderer("Bo");

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("between 3 and 100"));
    }

    @Test
    public void should_fail_for_orderer_too_long()
    {
        Order order = validOrder()
            .setOrderer("Boi this is a very long name that exceeds well over a hundred symbols! If you don't believe me, let me add some more words here.");

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("between 3 and 100"));
    }

    @Test
    public void should_fail_for_missing_visible_flag()
    {
        Order order = validOrder().setVisible(null);

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("must be set"));
    }

    @Test
    public void should_fail_for_missing_mainImage()
    {
        Order order = validOrder().setImages(images(2, 2, 3, 3, 3));

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("cannot be missing"));
    }

    @Test
    public void should_fail_for_missing_secondary_Image()
    {
        Order order = validOrder().setImages(images(1, 2, 3, 3, 3));

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("must be set"));
    }

    @Test
    public void should_fail_for_missing_tertiary_Image()
    {
        Order order = validOrder().setImages(images(1, 2, 2, 3, 3));

        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertThat(violation.getMessage(), containsString("must be set"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_create_configuration_array()
    {
        Order order = validOrder();

        String references = order.getConfiguration();

        assertThat(references, allOf(startsWith("["),
                                     containsString(order.getImages().get(0).getImage().getId().toString()),
                                     containsString(order.getImages().get(1).getImage().getId().toString()),
                                     containsString(order.getImages().get(2).getImage().getId().toString()),
                                     containsString(order.getImages().get(3).getImage().getId().toString()),
                                     containsString(order.getImages().get(4).getImage().getId().toString()),
                                     containsString(order.getImages().get(5).getImage().getId().toString()),
                                     endsWith("]")));
    }

    @Test
    public void should_sort_configuration_array()
    {
        Image image1 = validImage().setId(2L);
        Image image2 = validImage().setId(1L);
        ImageReference reference1 = validImageReference(1).setImage(image1);
        ImageReference reference2 = validImageReference(2).setImage(image2);
        Order order = validOrder().setImages(asList(reference1, reference2));

        String references = order.getConfiguration();

        assertThat(references, equalTo("[1,2]"));
    }
}
