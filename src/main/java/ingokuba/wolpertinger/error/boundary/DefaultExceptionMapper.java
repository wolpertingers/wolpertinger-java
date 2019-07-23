package ingokuba.wolpertinger.error.boundary;

import static ingokuba.wolpertinger.error.boundary.Error.error;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Providers           providers;

    private static final Logger LOGGER = Logger.getLogger(DefaultExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable throwable)
    {
        LOGGER.log(Level.WARNING, "Error caught by default mapper.", throwable);
        Response response = getMappedResponse(throwable);

        if (response == null) {
            LOGGER.warning("Couldn't find matching exception mapper for: " + getName(throwable));
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
        if (throwable == null || providers == null) {
            return null;
        }
        LOGGER.info("Looking for ExceptionMapper for: " + throwable.getClass().getName());
        ExceptionMapper<Throwable> exceptionMapper = (ExceptionMapper<Throwable>)providers.getExceptionMapper(throwable.getClass());
        if (exceptionMapper != null && !(exceptionMapper instanceof DefaultExceptionMapper) && checkPackage(exceptionMapper)) {
            LOGGER.info("Found ExceptionMapper: " + exceptionMapper.getClass().getName());
            return exceptionMapper.toResponse(throwable);
        }
        return getMappedResponse(throwable.getCause());
    }

    /**
     * Checks whether the package of the object matches the package of this class.
     * 
     * @return true if object is in this package.
     */
    private boolean checkPackage(Object object)
    {
        return getClass().getPackage().equals(object.getClass().getPackage());
    }

    /**
     * Get class name of the throwable.
     * 
     * @return Full class name or 'unknown'.
     */
    private String getName(Throwable throwable)
    {
        return throwable == null ? "unknown" : throwable.getClass().getName();
    }
}