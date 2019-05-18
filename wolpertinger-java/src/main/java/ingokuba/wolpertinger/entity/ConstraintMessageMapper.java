package ingokuba.wolpertinger.entity;

import ingokuba.wolpertinger.image.entity.Image;

public class ConstraintMessageMapper
{

    /**
     * Mappings of constraint labels to attribute name and readable message.
     * All mapping arrays must be of length 3.
     */
    private static final String[][] MAPPINGS  = {
            // Image constraints:
            {"Image_name_unique", Image.Fields.name, "Name of the image already taken."},
            {"Image_high_unique", Image.Fields.high, "High resolution path can only be referenced once."},
            {"Image_medium_unique", Image.Fields.high, "Medium resolution path can only be referenced once."},
            {"Image_low_unique", Image.Fields.high, "Low resolution path can only be referenced once."}
    };
    private String                  constraint;

    private String                  attribute = "unknown";
    private String                  message   = "unknown";

    public ConstraintMessageMapper(String value)
    {
        this.constraint = value.toLowerCase();
        for (String[] mapping : MAPPINGS) {
            if (constraint.contains(mapping[0].toLowerCase())) {
                this.attribute = mapping[1];
                this.message = mapping[2];
            }
        }
    }

    public String getConstraint()
    {
        return constraint;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public String getMessage()
    {
        return message;
    }
}
