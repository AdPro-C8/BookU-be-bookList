package com.adproc8.booku.booklist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.adproc8.booku.booklist.enums.UserRole;

import java.util.List;

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    private static final String BOOK_PATTERN = "/book/**";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.GET, BOOK_PATTERN)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, BOOK_PATTERN)
                        .hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, BOOK_PATTERN)
                        .hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH, BOOK_PATTERN)
                        .hasRole(UserRole.ADMIN.toString())
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
