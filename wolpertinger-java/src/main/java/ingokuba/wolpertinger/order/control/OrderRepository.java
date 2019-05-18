package ingokuba.wolpertinger.order.control;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import ingokuba.wolpertinger.control.Repository;
import ingokuba.wolpertinger.order.entity.Order;

@Stateful
@ApplicationScoped
public class OrderRepository extends Repository<Order>
{

    public OrderRepository()
    {
        super(Order.class);
    }

    @Override
    public List<Order> getEntities()
    {
        return getEntityManager().createQuery("select e from Order e where visible=true", Order.class).getResultList();
    }
}
