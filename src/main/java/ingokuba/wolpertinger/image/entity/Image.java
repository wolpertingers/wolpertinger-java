package ingokuba.wolpertinger.image.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Accessors(chain = true)
@FieldNameConstants
@ValidImage
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "Image_name_unique", columnNames = {Image.Fields.name}),
        @UniqueConstraint(name = "Image_high_unique", columnNames = {Image.Fields.high}),
        @UniqueConstraint(name = "Image_medium_unique", columnNames = {Image.Fields.medium}),
        @UniqueConstraint(name = "Image_low_unique", columnNames = {Image.Fields.low})
})
@Entity
public class Image
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_generator")
    @SequenceGenerator(name = "image_generator", sequenceName = "image_sequence")
    private Long   id;

    @NotEmpty(message = "{Image.name.NotEmpty}")
    @NaturalId(mutable = true)
    private String name;

    @NotEmpty(message = "{Image.high.NotEmpty}")
    private String high;

    private String medium;

    private String low;
}
