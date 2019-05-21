package ingokuba.wolpertinger.boundary;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class WolpertingerClient
{

    private static final String BASE_URL = "http://localhost:8080/wolpertinger-java/rest";
    private String              resource;

    public WolpertingerClient(String resource)
    {
        this.resource = resource;
    }

    private WebTarget getTargetResource()
    {
        return ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class).target(BASE_URL).path(resource);
    }

    public Response get(String path)
    {
        WebTarget targetResource = getTargetResource();
        if (path != null) {
            return targetResource.path(path).request().get();
        }
        return targetResource.request().get();
    }

    public Response get(String path, MultivaluedMap<String, String> queryParameters)
    {
        WebTarget targetResource = getTargetResource();
        for (String key : queryParameters.keySet()) {
            targetResource = targetResource.queryParam(key, queryParameters.get(key).toArray(new Object[0]));
        }
        if (path != null) {
            return targetResource.path(path).request().get();
        }
        return targetResource.request().get();
    }

    public Response post(String path, Object entity)
    {
        WebTarget targetResource = getTargetResource();
        if (path != null) {
            targetResource = targetResource.path(path);
        }
        return targetResource.request().post(entity(entity, APPLICATION_JSON));
    }
}
