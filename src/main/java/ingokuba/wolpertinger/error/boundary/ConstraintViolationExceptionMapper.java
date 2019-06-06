package ingokuba.wolpertinger.error.boundary;

import static ingokuba.wolpertinger.error.boundary.Error.error;
import static ingokuba.wolpertinger.error.boundary.Status.UNPROCESSABLE_ENTITY;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException>
{

    @Override
    public Response toResponse(ConstraintViolationException exception)
    {
        ErrorResponse error = new ErrorResponse().setErrors(fromConstraintViolations(exception.getConstraintViolations()));
        return Response.status(UNPROCESSABLE_ENTITY).entity(error).build();
    }

    /**
     * Map a set of constraint violations to an array of errors.
     * 
     * @param constraintViolations Set of {@link ConstraintViolation}.
     * @return Array of {@link Error}.
     */
    private static Error[] fromConstraintViolations(Set<ConstraintViolation<?>> constraintViolations)
    {
        List<Error> errors = new ArrayList<>();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<?> constraintViolation = iterator.next();
            Error error = error()
                .setName(ConstraintViolationException.class.getName())
                .setField(constraintViolation.getPropertyPath().toString())
                .setTemplate(removeCurlyBrackets(constraintViolation.getMessageTemplate()))
                .setMessage(constraintViolation.getMessage());
            errors.add(error);
        }
        return errors.toArray(new Error[0]);
    }

    private static String removeCurlyBrackets(String string)
    {
        return string.replace("{", "").replace("}", "");
    }
}