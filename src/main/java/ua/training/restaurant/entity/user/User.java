package ua.training.restaurant.entity.user;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import ua.training.restaurant.entity.RegexContainer;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Student
 */
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
    @Pattern(regexp = RegexContainer.NAME_REGEX, message = "Invalid en name")
    @Column(name = "name_en", nullable = false)
    private String nameEN;
    @Pattern(regexp = RegexContainer.NAME_REGEX_UA, message = "Invalid ua name")
    @Column(name = "name_ua", nullable = false)
    private String nameUA;
    @Pattern(regexp = RegexContainer.USERNAME_REGEX, message = "Invalid username")
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    @Size(min = 3, max = 100)
    private String password;
    @Column(nullable = false, columnDefinition = "int8 check(funds between 0 and 1000000)")
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

