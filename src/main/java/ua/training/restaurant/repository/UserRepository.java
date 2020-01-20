package ua.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.restaurant.entity.user.Role;
import ua.training.restaurant.entity.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by Student
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    boolean existsByUsername(String username);

    List<User> findByAuthoritiesContaining(Role role);


}
