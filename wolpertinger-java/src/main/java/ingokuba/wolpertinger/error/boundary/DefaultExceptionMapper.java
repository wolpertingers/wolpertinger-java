package ingokuba.wolpertinger.error.boundary;

import static ingokuba.wolpertinger.error.boundary.Error.error;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

@Provider
public class DefaultExceptionMapper
    implements ExceptionMapper<Throwable>
{

    @Context
    private Providers providers;

    @Override
    public Response toResponse(Throwable throwable)
    {
        Response response = getMappedResponse(throwable);
        if (response == null) {
            ErrorResponse error = new ErrorResponse().setErrors(fromStackTrace(throwable, new ArrayList<>()));
            response = Response.serverError().entity(error).build();
        }
        return response;
    }

    /**
     * Maps the stack trace of a {@link Throwable} to an array of errors.
     * 
     * @param throwable Throwable supplying the stack trace.
     * @param errors Initially empty list of {@link Error}s.
     * @return Array of {@link Error}.
     */
    private static Error[] fromStackTrace(Throwable throwable, List<Error> errors)
    {
        if (throwable == null) {
            return errors.toArray(new Error[0]);
        }
        Error error = error().setName(throwable.getClass().getName()).setMessage(throwable.getMessage());
        errors.add(error);
        return fromStackTrace(throwable.getCause(), errors);
    }

    /**
     * Get response from the first matching {@link ExceptionMapper} implementation.
     * 
     * @return Response from another {@link ExceptionMapper} or null if none could be found.
     */
    @SuppressWarnings("unchecked")
    private Response getMappedResponse(Throwable throwable)
    {
        if (throwable == null) {
            return null;
        }
        ExceptionMapper<Throwable> exceptionMapper = (ExceptionMapper<Throwable>)providers.getExceptionMapper(throwable.getClass());
        if (exceptionMapper != null && !(exceptionMapper instanceof DefaultExceptionMapper)) {
            return exceptionMapper.toResponse(throwable);
        }
        return getMappedResponse(throwable.getCause());
    }
}