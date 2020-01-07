package ua.training.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class RestaurantSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSpringApplication.class, args);
    }

}
