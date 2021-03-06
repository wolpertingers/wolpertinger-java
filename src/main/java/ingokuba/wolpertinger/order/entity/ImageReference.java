package ingokuba.wolpertinger.order.entity;

import static javax.persistence.FetchType.LAZY;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Range;

import ingokuba.wolpertinger.image.entity.Image;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Accessors(chain = true)
@FieldNameConstants
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "ImageReference_image_unique", columnNames = {ImageReference.Fields.image + "_id", ImageReference.Fields.order + "_id"}),
        @UniqueConstraint(name = "ImageReference_level_unique", columnNames = {ImageReference.Fields.level, ImageReference.Fields.order + "_id"})
})
@Entity
public class ImageReference
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reference_generator")
    @SequenceGenerator(name = "reference_generator", sequenceName = "reference_sequence")
    private Long    id;

    @NotNull(message = "{ImageReference.image.NotNull}")
    @ManyToOne(optional = false)
    private Image   image;

    @ToString.Exclude
    @JsonbTransient
    @XmlTransient
    @ManyToOne(fetch = LAZY)
    private Order   order;

    @NotNull(message = "{ImageReference.level.NotNull}")
    @Range(message = "{ImageReference.level.Range}", min = 1, max = 6)
    private Integer level;
}
