package ingokuba.wolpertinger.control;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ingokuba.wolpertinger.image.control.ImageRepository;
import ingokuba.wolpertinger.image.entity.Image;

@ApplicationScoped
public class ImageLoader
{

    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());

    @Inject
    @ConfigProperty(name = "wolpertinger.ImageLoader.imageUrl")
    private String              imageUrl;

    @Inject
    private ImageRepository     repository;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init)
    {
        try {
            Document imageRepository = Jsoup.connect(imageUrl).get();
            for (Element element : imageRepository.select("li a")) {
                if (element.hasAttr("href")) {
                    if (element.html().contains("Parent Directory")) {
                        continue;
                    }
                    String subUrl = element.attr("href");
                    String imageName = subUrl.replace("/", "");
                    LOGGER.info(() -> "Found image: " + imageName);
                    Image image = new Image().setName(imageName);
                    getImages(imageUrl + subUrl, image);
                    repository.store(image);
                }
            }
        } catch (IOException e) {
            throw new EJBException("Cannot connect to " + imageUrl, e);
        }
    }

    private void getImages(String url, Image image)
    {
        try {
            Document imageFolder = Jsoup.connect(url).get();
            for (Element element : imageFolder.select("li a")) {
                String href = element.attr("href");
                String fullUrl = getUrl(url + href);
                if (href.startsWith("high")) {
                    image.setHigh(fullUrl);
                }
                else if (href.startsWith("medium")) {
                    image.setMedium(fullUrl);
                }
                else if (href.startsWith("low")) {
                    image.setLow(fullUrl);
                }
            }
        } catch (IOException e) {
            throw new EJBException("Cannot connect to " + url, e);
        }
    }

    /**
     * If the URL contains 'wolpertinger-apache' as the host, replace it with 'localhost' so it can
     * be accessed by the frontend.
     */
    private String getUrl(String url)
    {
        return url.replace("wolpertinger-apache", "localhost");
    }
}
