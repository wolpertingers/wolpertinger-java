package ingokuba.wolpertinger.order.boundary;

import static ingokuba.wolpertinger.error.boundary.ErrorUtil.NOT_FOUND;
import static ingokuba.wolpertinger.error.boundary.ErrorUtil.NO_ENTITY;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.apache.commons.mail.EmailException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ingokuba.wolpertinger.boundary.WolpertingerApplication.Roles;
import ingokuba.wolpertinger.control.EmailService;
import ingokuba.wolpertinger.control.RepositoryException;
import ingokuba.wolpertinger.error.boundary.DefaultExceptionMapper;
import ingokuba.wolpertinger.error.boundary.ErrorUtil;
import ingokuba.wolpertinger.error.boundary.SQLExceptionMapper;
import ingokuba.wolpertinger.order.control.OrderRepository;
import ingokuba.wolpertinger.order.entity.Order;

@Path("orders")
@ApplicationScoped
public class OrderResource
{

    @Inject
    private OrderRepository repository;
    @Inject
    @ConfigProperty(name = "wolpertinger.OrderResource.recipients")
    private String          recipients;
    @Inject
    @ConfigProperty(name = "wolpertinger.OrderResource.username")
    private String          username;
    @Inject
    @ConfigProperty(name = "wolpertinger.OrderResource.password")
    private String          password;

    @RolesAllowed(Roles.ADMIN)
    @GET
    @Produces(APPLICATION_JSON)
    public Response all()
    {
        return Response.ok(new GenericEntity<List<Order>>(repository.getEntities()) {}).build();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response find(@PathParam(Order.Fields.id) long id)
    {
        Order order = repository.get(id);
        if (!order.getVisible()) {
            throw new ForbiddenException("Order with id '" + id + "' is not visible to the public.");
        }
        return Response.ok(order).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response create(Order order)
    {
        if (order == null) {
            throw new BadRequestException(NO_ENTITY);
        }
        try {
            order.setConfiguration();
            repository.store(order);
            EmailService.sendEmail(order, username, password, recipients.split(","));
        } catch (RepositoryException re) {
            SQLException sqlException = ErrorUtil.getExceptionFromCause(re, SQLException.class);
            if (sqlException != null) {
                return new SQLExceptionMapper().toResponse(sqlException);
            }
            throw re;
        } catch (IOException | EmailException ee) {
            return new DefaultExceptionMapper().toResponse(ee);
        }
        return Response.status(CREATED).entity(order).build();
    }

    @RolesAllowed(Roles.ADMIN)
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam(Order.Fields.id) long id)
    {
        Order order = repository.get(id);
        if (order == null) {
            throw new NotFoundException(format(NOT_FOUND, id));
        }
        repository.delete(order);
        return Response.status(NO_CONTENT).build();
    }
}
