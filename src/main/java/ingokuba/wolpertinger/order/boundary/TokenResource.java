package ingokuba.wolpertinger.order.boundary;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import ingokuba.wolpertinger.boundary.WolpertingerApplication.Roles;
import ingokuba.wolpertinger.order.control.TokenRepository;
import ingokuba.wolpertinger.order.entity.Token;

@Path("tokens")
@ApplicationScoped
public class TokenResource
{

    @Inject
    private TokenRepository repository;

    @RolesAllowed(Roles.ADMIN)
    @POST
    @Produces(APPLICATION_JSON)
    public Response create()
    {
        Token token = new Token();
        repository.store(token);
        return Response.status(CREATED).entity(token).build();
    }

    @RolesAllowed(Roles.ADMIN)
    @GET
    @Produces(APPLICATION_JSON)
    public Response all()
    {
        return Response.ok(new GenericEntity<List<Token>>(repository.getEntities()) {}).build();
    }

    @Path("{value}")
    @GET
    @Produces(APPLICATION_JSON)
    public Response byValue(@PathParam(Token.Fields.value) String value)
    {
        return Response.ok().entity(repository.byNaturalId(Token.Fields.value, value)).build();
    }
}
