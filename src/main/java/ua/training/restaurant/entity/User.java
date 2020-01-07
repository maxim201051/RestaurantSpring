package ua.training.restaurant.entity;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User implements UserDetails {
    private static final int EXCHANGE_RATE = 25;
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "user_id_seq", name = "user_id_seq")
    @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank
    @Column(name = "name_en", nullable = false)
    private String nameEN;
    @NotBlank
    @Column(name = "name_ua", nullable = false)
    private String nameUA;
    @NotBlank
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    @Size(min=3,max=100)
    private String password;
    @Column(nullable = false)
    private Long funds; //in US pennies
    @Column(nullable = false)
    private int ordersNumber;
    @Column(nullable = false)
    private Long ordersTotalCost;
    @Column(nullable = false)
    private LocalDate registrationDate;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}

