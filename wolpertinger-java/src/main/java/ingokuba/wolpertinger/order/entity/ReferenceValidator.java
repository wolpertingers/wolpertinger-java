package ingokuba.wolpertinger.order.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReferenceValidator
    implements ConstraintValidator<ValidReference, List<ImageReference>>
{

    private LevelConstraint[] levelConstraints;

    @Override
    public void initialize(ValidReference constraintAnnotation)
    {
        this.levelConstraints = constraintAnnotation.levelConstraints();
    }

    @Override
    public boolean isValid(List<ImageReference> references, ConstraintValidatorContext context)
    {
        boolean isValid = true;
        context.disableDefaultConstraintViolation();
        Map<Integer, Integer> levels = new HashMap<>();
        Map<String, Integer> images = new HashMap<>();
        for (ImageReference reference : references) {
            // levels
            Integer amount = levels.getOrDefault(reference.getLevel(), 0);
            levels.put(reference.getLevel(), amount + 1);
            // images
            String name = reference.getImage().getName();
            Integer imageCount = images.getOrDefault(name, 0);
            if (imageCount > 0) {
                context.buildConstraintViolationWithTemplate("Image '" + name + "' is referenced more than once.").addConstraintViolation();
                isValid = false;
            }
            images.put(name, imageCount + 1);
        }
        // check levels
        for (LevelConstraint levelConstraint : levelConstraints) {
            Integer level = levels.get(levelConstraint.level());
            if (level == null || level != levelConstraint.amount()) {
                context.buildConstraintViolationWithTemplate(levelConstraint.message()).addConstraintViolation();
                isValid = false;
            }
        }
        return isValid;
    }
}
