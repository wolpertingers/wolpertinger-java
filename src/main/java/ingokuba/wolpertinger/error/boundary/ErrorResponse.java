package ingokuba.wolpertinger.error.boundary;

/**
 * Wrapper for all errors returned in a response.
 * 
 * <pre>
 * {
 * "errors": [
 * ...
 * ]
 * }
 */
public class ErrorResponse
{

    private Error[] errors;

    public Error[] getErrors()
    {
        return errors;
    }

    public ErrorResponse setErrors(Error... errors)
    {
        this.errors = errors;
        return this;
    }
}