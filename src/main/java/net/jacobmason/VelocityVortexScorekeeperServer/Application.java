package net.jacobmason.VelocityVortexScorekeeperServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by JacobAMason on 12/21/16.
 */
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/audience").setViewName("/audience.html");
        registry.addViewController("/scorekeeper").setViewName("/scorekeeper.html");
    }
}