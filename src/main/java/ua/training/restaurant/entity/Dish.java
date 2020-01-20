package ua.training.restaurant.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Student
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name_en", "name_ua"})})
public class Dish {
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "dish_id_seq", name = "dish_id_seq")
    @GeneratedValue(generator = "dish_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name_en", nullable = false)
    private String nameEn;
    @Column(name = "name_ua", nullable = false)
    private String nameUa;
    @Column(nullable = false)
    private Long portion; //in grams
    @Column(nullable = false)
    private Long price; //in US pennies
}
