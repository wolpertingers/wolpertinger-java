package ingokuba.wolpertinger.image.boundary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import ingokuba.wolpertinger.boundary.WolpertingerClient;
import ingokuba.wolpertinger.image.entity.Image;

public class ImageResourceIT
{

    private WolpertingerClient client = new WolpertingerClient("images");

    @Test
    public void should_return_all_images()
    {
        Response response = client.get(null);

        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        Image[] images = response.readEntity(Image[].class);
        assertThat(images.length, greaterThan(1));
    }

    @Test
    public void should_return_image_by_name()
    {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.putSingle(Image.Fields.name, "axe");

        Response response = client.get(null, queryParams);

        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        Image image = response.readEntity(Image.class);
        assertThat(image.getHigh(), containsString("high"));
    }

    @Test
    public void should_return_404_if_image_doesnt_exist()
    {
        MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.putSingle(Image.Fields.name, "sdfsdfwe");

        Response response = client.get(null, queryParams);

        assertThat(response.getStatus(), equalTo(Response.Status.NOT_FOUND.getStatusCode()));
    }
}
