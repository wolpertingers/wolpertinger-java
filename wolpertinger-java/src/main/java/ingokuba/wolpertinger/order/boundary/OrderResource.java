package ingokuba.wolpertinger.order.boundary;

import static ingokuba.wolpertinger.error.boundary.ErrorUtil.NO_ENTITY;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import ingokuba.wolpertinger.control.RepositoryException;
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
            repository.store(order);
        } catch (RepositoryException e) {
            SQLException sqlException = ErrorUtil.getExceptionFromCause(e, SQLException.class);
            if (sqlException != null) {
                return new SQLExceptionMapper().toResponse(sqlException);
            }
            throw e;
        }
        return Response.status(CREATED).entity(order).build();
    }
}
