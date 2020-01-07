package ua.training.restaurant.entity;

import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "order_id_seq", name = "order_id_seq")
    @GeneratedValue(generator = "order_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private User user;
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<OrderUnit> orderUnits;
    private LocalDateTime created, accepted, ready, paid;
    @Enumerated
    private Order_Status status;

    public Order() {
        this.orderUnits = new ArrayList<>();
    }
    private OrderUnit findOrderUnitById(Long id) {
        for (OrderUnit unit : this.orderUnits) {
            if (unit.getDish().getId().equals(id)) {
                return unit;
            }
        }
        return null;
    }

    public void addDish(Dish dish, int quantity) {
        OrderUnit unit = this.findOrderUnitById(dish.getId());

        if (unit == null) {
            unit = new OrderUnit();
            unit.setQuantity(0);
            unit.setDish(dish);
            this.orderUnits.add(unit);
        }
        int newQuantity = unit.getQuantity() + quantity;
        if (newQuantity <= 0) {
            this.orderUnits.remove(unit);
        } else {
            unit.setQuantity(newQuantity);
        }
    }

    public void updateDish(Long id, int quantity) {
        OrderUnit unit = this.findOrderUnitById(id);

        if (unit != null) {
            if (quantity <= 0) {
                this.orderUnits.remove(unit);
            } else {
                unit.setQuantity(quantity);
            }
        }
    }

    public void removeDish(Dish dish) {
        OrderUnit unit = this.findOrderUnitById(dish.getId());
        if (unit != null) {
            this.orderUnits.remove(unit);
        }
    }

    public boolean isEmpty() {
        return this.orderUnits.isEmpty();
    }

    public boolean isValidCustomer() {
        return this.user != null && this.user.isEnabled();
    }

    public int getQuantityTotal() {
        int quantity = 0;
        for (OrderUnit unit : this.orderUnits) {
            quantity += unit.getQuantity();
        }
        return quantity;
    }

    public Long getAmountTotal() {
        Long total = 0L;
        for (OrderUnit unit : this.orderUnits) {
            total += unit.getAmount();
        }
        return total;
    }

    public void updateQuantity(Order order) {
        if (order != null) {
            List<OrderUnit> units = order.getOrderUnits();
            for (OrderUnit unit : units) {
                this.updateDish(unit.getDish().getId(), unit.getQuantity());
            }
        }
    }
}