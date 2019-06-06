package ingokuba.wolpertinger.entity;

import ingokuba.wolpertinger.image.entity.Image;
import ingokuba.wolpertinger.order.entity.Order;
import ingokuba.wolpertinger.order.entity.Token;

public class ConstraintMessageMapper
{

    /**
     * Mappings of constraint labels to attribute name and readable message.
     * All mapping arrays must be of length 3.
     */
    private static final String[][] MAPPINGS = {
            // Image constraints:
            {"Image_name_unique", Image.Fields.name, "Name of the image already taken."},
            {"Image_high_unique", Image.Fields.high, "High resolution path can only be referenced once."},
            {"Image_medium_unique", Image.Fields.high, "Medium resolution path can only be referenced once."},
            {"Image_low_unique", Image.Fields.high, "Low resolution path can only be referenced once."},
            {"Order_orderer_unique", Order.Fields.orderer, "Orderer name must be unique."},
            {"Order_configuration_unique", Order.Fields.configuration, "This configuration has already been created."},
            {"ImageReference_level_unique", Order.Fields.images, "Image reference level is contained more than once."},
            {"ImageReference_image_unique", Order.Fields.images, "Image is referenced more than once."},
            {"Token_value_unique", Token.Fields.value, "Token has to be unique."},
            {"Order_token_unique", Order.Fields.token, "A token can only be referenced once."}
    };

    private String                  attribute;
    private String                  message;
    private String                  template;

    public ConstraintMessageMapper(String value)
    {
        for (String[] mapping : MAPPINGS) {
            if (value.toLowerCase().contains(mapping[0].toLowerCase())) {
                this.template = mapping[0];
                this.attribute = mapping[1];
                this.message = mapping[2];
                break;
            }
        }
    }

    public String getAttribute()
    {
        return attribute;
    }

    public String getMessage()
    {
        return message;
    }

    public String getTemplate()
    {
        return template;
    }
}
