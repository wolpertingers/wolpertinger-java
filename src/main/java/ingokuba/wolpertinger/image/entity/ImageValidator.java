package ingokuba.wolpertinger.image.entity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator
    implements ConstraintValidator<ValidImage, Image>
{

    @Override
    public boolean isValid(Image image, ConstraintValidatorContext context)
    {
        if (image.getMedium() == null) {
            return image.getLow() == null;
        }
        return true;
    }
}
