package ingokuba.wolpertinger.image.control;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import ingokuba.wolpertinger.control.Repository;
import ingokuba.wolpertinger.image.entity.Image;

@Stateful
@ApplicationScoped
public class ImageRepository extends Repository<Image>
{

    public ImageRepository()
    {
        super(Image.class);
    }
}
