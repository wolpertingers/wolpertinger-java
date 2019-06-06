package ingokuba.wolpertinger.control;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.util.IDNEmailAddressConverter;

import ingokuba.wolpertinger.order.entity.ImageReference;
import ingokuba.wolpertinger.order.entity.Order;

public class EmailService
{

    private EmailService()
    {
        throw new UnsupportedOperationException("static class");
    }

    public static void sendEmail(Order order, String username, String password, String... recipients)
        throws EmailException, IOException
    {
        if (recipients.length < 1) {
            return;
        }
        ClassLoader classLoader = order.getClass().getClassLoader();
        String template = convert(classLoader.getResourceAsStream("email.html"));
        String content = format(template, order.getOrderer(), order.getToken().getValue(), order.getUrl(),
                                getImageNames(order.getImages()));
        for (String recipient : recipients) {
            if (!isValid(recipient)) {
                continue;
            }
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setStartTLSEnabled(true);
            email.setSSLCheckServerIdentity(true);
            email.setAuthentication(username, password);
            email.addTo(recipient);
            email.setFrom(username + "@gmail.com", "Wolpertinger Support");
            email.setSubject("New order from " + order.getOrderer());
            email.setHtmlMsg(content);
            email.setTextMsg("Your email client does not support HTML messages.");
            email.send();
        }
    }

    /**
     * Check whether email has a valid format
     * 
     * @param email The email address, e.g. 'test@gmail.com'
     */
    private static boolean isValid(String email)
    {
        try {
            new InternetAddress(new IDNEmailAddressConverter().toASCII(email));
        } catch (AddressException e) {
            return false;
        }
        return true;
    }

    /**
     * Format the email template with the given arguments.
     */
    private static String format(String template, String orderer, String token, String url, String... images)
        throws EmailException
    {
        if (images.length != 6) {
            throw new EmailException("Invalid amount of images: " + images.length);
        }
        return String.format(template, orderer, token, url, images[0], images[1], images[2], images[3], images[4], images[5]);
    }

    /**
     * Get image names sorted by level.
     */
    private static String[] getImageNames(List<ImageReference> references)
    {
        String[] names = new String[references.size()];
        for (ImageReference reference : references) {
            names[reference.getLevel() - 1] = reference.getImage().getName();
        }
        return names;
    }

    private static String convert(InputStream inputStream)
        throws IOException
    {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
