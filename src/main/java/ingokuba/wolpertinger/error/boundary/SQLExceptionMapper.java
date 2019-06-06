package ingokuba.wolpertinger.error.boundary;

import static ingokuba.wolpertinger.error.boundary.Error.error;
import static ingokuba.wolpertinger.error.boundary.Status.UNPROCESSABLE_ENTITY;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ingokuba.wolpertinger.entity.ConstraintMessageMapper;

@Provider
public class SQLExceptionMapper
    implements ExceptionMapper<SQLException>
{

    @Override
    public Response toResponse(SQLException exception)
    {
        ErrorResponse error = new ErrorResponse().setErrors(fromSQLExceptions(exception, new ArrayList<>()));
        return Response.status(UNPROCESSABLE_ENTITY).entity(error).build();
    }

    private static Error[] fromSQLExceptions(SQLException exception, List<Error> errors)
    {
        if (exception == null) {
            return errors.toArray(new Error[0]);
        }
        errors.add(fromSQLException(exception));
        return fromSQLExceptions(exception.getNextException(), errors);
    }

    private static Error fromSQLException(SQLException exception)
    {
        ConstraintMessageMapper mapper = new ConstraintMessageMapper(exception.getMessage());
        return error().setName(exception.getClass().getName())
            .setField(mapper.getAttribute())
            .setTemplate(mapper.getTemplate())
            .setMessage(mapper.getMessage());
    }
}
