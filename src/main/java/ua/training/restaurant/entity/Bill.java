package ua.training.restaurant.entity;

import lombok.Getter;
import lombok.Setter;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.user.User;

import java.util.List;

@Getter
@Setter
public class Bill {
    private Long id;
    private User user;
    private List<OrderUnit> orderUnits;
    private Long totalCost;

    public Bill(Order order) {
        this.id=order.getId();
        this.user=order.getUser();
        this.orderUnits=order.getOrderUnits();
        this.totalCost=order.getAmountTotal();
    }
}
