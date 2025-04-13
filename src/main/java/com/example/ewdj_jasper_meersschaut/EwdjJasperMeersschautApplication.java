package com.example.ewdj_jasper_meersschaut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class EwdjJasperMeersschautApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(EwdjJasperMeersschautApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/events").setViewName("eventsList");
        registry.addViewController("/events/{id}").setViewName("eventDetails");
    }

}
