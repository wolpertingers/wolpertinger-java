package ingokuba.wolpertinger.image.entity;

import static ingokuba.wolpertinger.entity.EntitySetup.validImage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

import ingokuba.wolpertinger.image.entity.Image;

public class ImageTest
{

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void should_accept_valid_image_with_all_attributes()
    {
        Image image = validImage().setId(1L).setMedium("/relative/path.txt").setLow("FileName.png");

        Set<ConstraintViolation<Image>> violations = validator.validate(image);

        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void should_not_allow_null_name()
    {
        Image image = validImage().setName(null);

        Set<ConstraintViolation<Image>> violations = validator.validate(image);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Image> constraintViolation = violations.iterator().next();
        assertThat(constraintViolation.getPropertyPath().toString(), equalTo(Image.Fields.name));
    }

    @Test
    public void should_not_allow_empty_name()
    {
        Image image = validImage().setName("");

        Set<ConstraintViolation<Image>> violations = validator.validate(image);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Image> constraintViolation = violations.iterator().next();
        assertThat(constraintViolation.getPropertyPath().toString(), equalTo(Image.Fields.name));
    }

    @Test
    public void should_not_allow_null_high_path()
    {
        Image image = validImage().setHigh(null);

        Set<ConstraintViolation<Image>> violations = validator.validate(image);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Image> constraintViolation = violations.iterator().next();
        assertThat(constraintViolation.getPropertyPath().toString(), equalTo(Image.Fields.high));
    }

    @Test
    public void should_not_allow_empty_high_path()
    {
        Image image = validImage().setHigh("");

        Set<ConstraintViolation<Image>> violations = validator.validate(image);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Image> constraintViolation = violations.iterator().next();
        assertThat(constraintViolation.getPropertyPath().toString(), equalTo(Image.Fields.high));
    }

    @Test
    public void should_not_allow_low_path_if_medium_is_not_set()
    {
        Image image = validImage().setMedium(null).setLow("a/file/path");

        Set<ConstraintViolation<Image>> violations = validator.validate(image);

        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<Image> constraintViolation = violations.iterator().next();
        assertThat(constraintViolation.getMessage(), equalTo("Low resolution can only be set if medium resolution is set."));
    }
}
