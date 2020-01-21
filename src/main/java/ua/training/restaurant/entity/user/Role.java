package ua.training.restaurant.entity.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Student
 */
public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
