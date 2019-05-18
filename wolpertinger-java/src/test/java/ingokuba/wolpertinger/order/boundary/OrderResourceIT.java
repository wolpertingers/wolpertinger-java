package ingokuba.wolpertinger.order.boundary;

import static ingokuba.wolpertinger.entity.EntitySetup.validImageReference;
import static ingokuba.wolpertinger.entity.EntitySetup.validOrder;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import ingokuba.wolpertinger.boundary.WolpertingerClient;
import ingokuba.wolpertinger.image.entity.Image;
import ingokuba.wolpertinger.order.entity.ImageReference;
import ingokuba.wolpertinger.order.entity.Order;

public class OrderResourceIT
{

    private WolpertingerClient client      = new WolpertingerClient("orders");
    private WolpertingerClient imageClient = new WolpertingerClient("images");

    @Test
    public void should_create_valid_order()
    {
        // GET images
        Response imageResponse = imageClient.get(null);
        assertThat(imageResponse.getStatus(), equalTo(OK.getStatusCode()));
        Image[] images = imageResponse.readEntity(Image[].class);
        assertThat(images.length, greaterThan(5));
        // create reference list
        List<ImageReference> references = new ArrayList<>();
        references.add(validImageReference(1).setImage(images[0]));
        references.add(validImageReference(2).setImage(images[1]));
        references.add(validImageReference(2).setImage(images[2]));
        references.add(validImageReference(3).setImage(images[3]));
        references.add(validImageReference(3).setImage(images[4]));
        references.add(validImageReference(3).setImage(images[5]));

        Order order = validOrder().setVisible(true).setImages(references);

        Response response = client.post(null, order);

        assertThat(response.getStatus(), equalTo(CREATED.getStatusCode()));
    }
}
