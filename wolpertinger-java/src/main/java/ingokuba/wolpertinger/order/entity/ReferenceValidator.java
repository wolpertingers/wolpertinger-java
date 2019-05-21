package ingokuba.wolpertinger.order.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReferenceValidator
    implements ConstraintValidator<ValidReference, List<ImageReference>>
{

    @Override
    public boolean isValid(List<ImageReference> references, ConstraintValidatorContext context)
    {
        boolean isValid = true;
        context.disableDefaultConstraintViolation();
        List<Integer> levels = new ArrayList<>();
        Map<String, Integer> images = new HashMap<>();
        for (ImageReference reference : references) {
            // levels
            Integer level = reference.getLevel();
            if (levels.contains(level)) {
                context.buildConstraintViolationWithTemplate("Level '" + level + "' is contained more than once.").addConstraintViolation();
                isValid = false;
            }
            else {
                levels.add(level);
            }
            // images
            String name = reference.getImage().getName();
            Integer imageCount = images.getOrDefault(name, 0);
            if (imageCount > 0) {
                context.buildConstraintViolationWithTemplate("Image '" + name + "' is referenced more than once.").addConstraintViolation();
                isValid = false;
            }
            images.put(name, imageCount + 1);
        }
        return isValid;
    }
}
