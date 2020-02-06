package ua.training.restaurant.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ua.training.restaurant.entity.OrderUnit;
import ua.training.restaurant.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Student
 */
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
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<OrderUnit> orderUnits;
    private LocalDateTime created, accepted, ready, paid;
    @Enumerated
    private Order_Status status;

    public Order() {
        this.orderUnits = new ArrayList<>();
    }

    public boolean isEmpty() {
        return this.getOrderUnits().isEmpty();
    }

    public int getQuantityTotal() {
        return orderUnits.stream().mapToInt(OrderUnit::getQuantity).sum();
    }

    public Long getAmountTotal() {
        return orderUnits.stream().mapToLong(OrderUnit::getAmount).sum();
    }
}