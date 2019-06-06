package ingokuba.wolpertinger.image.boundary;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import ingokuba.wolpertinger.image.control.ImageRepository;
import ingokuba.wolpertinger.image.entity.Image;

@Path("images")
@ApplicationScoped
public class ImageResource
{

    @Inject
    private ImageRepository repository;

    @GET
    @Produces(APPLICATION_JSON)
    public Response get(@QueryParam(Image.Fields.name) String name)
    {
        if (name != null) {
            Image image = repository.byNaturalId(Image.Fields.name, name);
            if (image == null) {
                throw new NotFoundException("Image with name '" + name + "' was not found.");
            }
            return Response.ok().entity(image).build();
        }
        return Response.ok().entity(new GenericEntity<List<Image>>(repository.getEntities()) {}).build();
    }
}
