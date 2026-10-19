package com.Hero.Pricing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Admin@123"))
                .roles("ADMIN")
                .build();

        UserDetails salesman = User.builder()
                .username("salesman")
                .password(passwordEncoder.encode("Salesman@123"))
                .roles("SALESMAN")
                .build();

        return new InMemoryUserDetailsManager(admin, salesman);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Both roles can view all components
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/parts",
                                "/api/parts/**"
                        ).hasAnyRole("ADMIN", "SALESMAN")

                        // Both roles can calculate total cycle price
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/parts/calculate-price"
                        ).hasAnyRole("ADMIN", "SALESMAN")

                        // Only Admin can add components
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/parts",
                                "/api/parts/**"
                        ).hasRole("ADMIN")

                        // Only Admin can update prices
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/parts",
                                "/api/parts/**"
                        ).hasRole("ADMIN")

                        // Only Admin can delete components
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/parts",
                                "/api/parts/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

