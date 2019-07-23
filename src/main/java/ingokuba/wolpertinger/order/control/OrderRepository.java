package ingokuba.wolpertinger.order.control;

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
}
