package ua.training.restaurant.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Getter
@Setter
public class Bill {
    private User user;
    private List<OrderUnit> orderUnits;
    private Long totalCost;

    public Bill(Order order) {
        this.user=order.getUser();
        this.orderUnits=order.getOrderUnits();
        this.totalCost=order.getAmountTotal();
    }
}
