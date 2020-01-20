package ua.training.restaurant.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ua.training.restaurant.entity.user.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Student
 */
@Slf4j
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public MyAccessDeniedHandler() {
    }

    public MyAccessDeniedHandler(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String authorisedURLRedirect = "";

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();

        List<String> roles = new ArrayList<>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        log.info("request.isAdminInRole == isAdmin(roles) == " + isAdmin(roles));
        log.info("request.isUserInRole ==  isUser(roles) == " + isUser(roles));

        if (isAdmin(roles)) {
            authorisedURLRedirect = "/admin/";
        } else if (isUser(roles)) {
            authorisedURLRedirect = "/user/";
        } else {
            authorisedURLRedirect = "/login?error";
        }
        log.info(accessDeniedException.getLocalizedMessage());

        redirectStrategy.sendRedirect(request, response, authorisedURLRedirect);

    }

    private boolean isUser(List<String> roles) {
        return roles.contains(Role.USER.getAuthority());
    }

    private boolean isAdmin(List<String> roles) {
        return roles.contains(Role.ADMIN.getAuthority());
    }

}
