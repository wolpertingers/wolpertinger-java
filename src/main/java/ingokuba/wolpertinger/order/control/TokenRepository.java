package ingokuba.wolpertinger.order.control;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import ingokuba.wolpertinger.control.Repository;
import ingokuba.wolpertinger.order.entity.Token;

@Stateful
@ApplicationScoped
public class TokenRepository extends Repository<Token>
{

    public TokenRepository()
    {
        super(Token.class);
    }
}
