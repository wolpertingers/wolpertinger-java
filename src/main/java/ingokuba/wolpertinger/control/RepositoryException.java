package ingokuba.wolpertinger.control;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;

@ApplicationException
public class RepositoryException extends WebApplicationException
{

    private static final long serialVersionUID = 1L;

    public RepositoryException(Throwable cause)
    {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
