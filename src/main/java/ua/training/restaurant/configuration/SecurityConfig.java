package ua.training.restaurant.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.training.restaurant.service.UserService;

/**
 * Created by Student
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // The pages does not require login
        http.authorizeRequests().antMatchers("/").permitAll();
        //Only for NOT authenticated users
        http.authorizeRequests().antMatchers("/login", "/signup").not().authenticated();
        //Only for authenticated users
        http.authorizeRequests().antMatchers("/logout").authenticated();
        //Admin pages
        http.authorizeRequests().antMatchers("/user/**")
                .access("hasAuthority(T(ua.training.restaurant.entity.user.Role).USER.getAuthority())");
        //User pages
        http.authorizeRequests().antMatchers("/admin/**")
                .access("hasAuthority(T(ua.training.restaurant.entity.user.Role).ADMIN.getAuthority())");
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/");

        // Config for Login Form
        http.authorizeRequests().and().formLogin()//
                .loginPage("/login")//
                .successHandler(loginSuccessHandler)
                .failureUrl("/login?error")//
                .usernameParameter("username")
                .passwordParameter("password")
                // Config for Logout Page
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
