package ingokuba.wolpertinger.error.boundary;

public class ErrorUtil
{

    public static final String NO_ENTITY = "No entity found in body.";
    public static final String NOT_FOUND = "No entity found for key '%d'.";
    /**
     * Message template for unequal key values in request body entity and url parameter.
     * <ul>
     * <li>arg1: Request entity key.
     * <li>arg2: Key from url parameter.
     */
    public static final String WRONG_ID  = "Request body entity key '%d' is different to url key '%d'.";

    private ErrorUtil()
    {
    }

    /**
     * Iterate through causes of the throwable and find one of the given type.
     * 
     * @param throwable Throwable to iterate through
     * @param type Class of the desired Throwable
     * @return Throwable if found or null
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> E getExceptionFromCause(Throwable throwable, Class<E> type)
    {
        if (throwable == null) {
            return null;
        }
        if (type.isInstance(throwable)) {
            return (E)throwable;
        }
        return getExceptionFromCause(throwable.getCause(), type);
    }
}
