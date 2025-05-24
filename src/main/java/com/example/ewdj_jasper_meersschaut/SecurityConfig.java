package com.example.ewdj_jasper_meersschaut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

//    user:
//    username: nameUser
//    password: 12345678
//    admin:
//    username: admin
//    password: admin


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/rest/**")
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/login**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/403**").permitAll()
                                .requestMatchers("/404**").permitAll()
                                .requestMatchers("/events/create").hasRole("ADMIN")
                                .requestMatchers("/", "/events").permitAll()
                                .requestMatchers("/events/*").permitAll()
                                .requestMatchers("/events/*/edit").hasRole("ADMIN")
                                .requestMatchers("/events/*/update").hasRole("ADMIN")
                                .requestMatchers("/rooms/form").hasRole("ADMIN")
                                .requestMatchers("/rooms/create").hasRole("ADMIN")
                                .requestMatchers("/favourites", "/favourites/**").hasRole("USER")
                                .requestMatchers("/rooms/create").hasRole("ADMIN")
                                .requestMatchers("/rest/**").permitAll()
                                .requestMatchers("/*")
                                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')"))
                )
                .formLogin(form ->
                        form.defaultSuccessUrl("/events", true)
                                .loginPage("/login")
                                .usernameParameter("username").passwordParameter("password")
                )
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"));

        return http.build();
    }

}

