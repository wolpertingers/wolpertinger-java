package ingokuba.wolpertinger.error.boundary;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/**
 * Custom status type set because {@link Response.Status} does not provide all needed statuses,
 * see {@link <a href="https://httpstatuses.com/">HTTP Status Codes</a>}
 */
public enum Status
    implements StatusType
{
    /**
     * 200 OK
     */
    OK(200, "OK"),
    /**
     * 201 Created
     */
    CREATED(201, "Created"),
    /**
     * 204 No Content
     */
    NO_CONTENT(204, "No Content"),
    /**
     * 304 Not Modified
     */
    NOT_MODIFIED(304, "Not Modified"),
    /**
     * 400 Bad Request
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 404 Not Found
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * 409 Conflict
     */
    CONFLICT(409, "Conflict"),
    /**
     * 418 I'm a teapot
     */
    IM_A_TEAPOT(418, "I'm a teapot"),
    /**
     * 422 Unprocessable Entity
     */
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int    code;
    private final String reason;
    private final Family family;

    Status(final int statusCode, final String reasonPhrase)
    {
        this.code = statusCode;
        this.reason = reasonPhrase;
        this.family = Family.familyOf(statusCode);
    }

    @Override
    public int getStatusCode()
    {
        return code;
    }

    @Override
    public Family getFamily()
    {
        return family;
    }

    @Override
    public String getReasonPhrase()
    {
        return reason;
    }

    @Override
    public String toString()
    {
        return reason;
    }
}