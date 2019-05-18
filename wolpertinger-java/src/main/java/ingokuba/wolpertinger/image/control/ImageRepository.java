package ingokuba.wolpertinger.image.control;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import org.hibernate.Session;

import ingokuba.wolpertinger.control.Repository;
import ingokuba.wolpertinger.control.RepositoryException;
import ingokuba.wolpertinger.image.entity.Image;

@Stateful
@ApplicationScoped
public class ImageRepository extends Repository<Image>
{

    public ImageRepository()
    {
        super(Image.class);
    }

    /**
     * Get image by name (natural id).
     */
    public Image get(String name)
    {
        try {
            Session session = (Session)getEntityManager().getDelegate();
            return session.byNaturalId(Image.class).using(Image.Fields.name, name).load();
        } catch (Exception e) {
            throw new RepositoryException("Failed to retrieve Image.", e);
        }
    }
}
