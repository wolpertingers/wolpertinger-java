package ingokuba.wolpertinger.entity;

import java.util.ArrayList;
import java.util.List;

import ingokuba.wolpertinger.image.entity.Image;
import ingokuba.wolpertinger.order.entity.ImageReference;
import ingokuba.wolpertinger.order.entity.Order;

public class EntitySetup
{

    private static int suffix = 0;

    public static Image validImage()
    {
        suffix++;
        return new Image().setName("image" + suffix).setHigh("this/is/a/test/path");
    }

    public static Order validOrder()
    {
        return new Order().setOrderer("User " + suffix).setImages(images(1, 2, 3, 4, 5, 6));
    }

    public static ImageReference validImageReference(Integer level)
    {
        return new ImageReference().setLevel(level).setImage(validImage().setId((long)suffix));
    }

    /**
     * Creates image references for all given levels.
     */
    public static List<ImageReference> images(Integer... levels)
    {
        List<ImageReference> images = new ArrayList<>();
        for (Integer level : levels) {
            images.add(validImageReference(level));
        }
        return images;
    }
}
