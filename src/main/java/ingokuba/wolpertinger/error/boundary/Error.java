package ingokuba.wolpertinger.error.boundary;

public class Error
{

    private String name;
    private String message;
    private String template;
    private String field;

    public String getName()
    {
        return name;
    }

    public Error setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public Error setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public String getTemplate()
    {
        return template;
    }

    public Error setTemplate(String template)
    {
        this.template = template;
        return this;
    }

    public String getField()
    {
        return field;
    }

    public Error setField(String field)
    {
        this.field = field;
        return this;
    }

    public static Error error()
    {
        return new Error();
    }
}