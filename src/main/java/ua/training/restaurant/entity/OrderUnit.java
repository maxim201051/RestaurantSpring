package ua.training.restaurant.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Student
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "order_units")
public class OrderUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Dish dish;
    private Integer quantity;
    public OrderUnit() {
        this.quantity=0;
    }

    public Long getAmount() {
        return this.dish.getPrice() * this.quantity;
    }
}
