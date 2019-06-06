package ingokuba.wolpertinger.order.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Accessors(chain = true)
@FieldNameConstants
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "Token_value_unique", columnNames = {Token.Fields.value})
})
@Entity
public class Token
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_generator")
    @SequenceGenerator(name = "token_generator", sequenceName = "token_sequence")
    private Long   id;

    @NotNull
    @NaturalId
    @Column(updatable = false)
    private String value;

    public Token()
    {
        this.value = UUID.randomUUID().toString();
    }
}
