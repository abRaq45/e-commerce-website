package com.example.instamart.config;

import com.example.instamart.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 🔐 Password Encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 🌍 CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

       configuration.setAllowedOrigins(List.of(
    "http://localhost:5173",
    "https://e-commerce-website-it4i73chz-abdul-raquibs-projects.vercel.app"
));
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 🔥 MAIN SECURITY CONFIG
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 🔓 PUBLIC AUTH ENDPOINTS
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/login"
                        ).permitAll()

                        // 🔓 PUBLIC PRODUCT ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/items").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()

                        // 🔥 FIX: ALLOW CHAT ENDPOINT
                        .requestMatchers("/api/chat/**").permitAll()

                        // 🔒 PROTECTED CART
                        .requestMatchers("/api/cart/**").authenticated()

                        // 🔒 PROTECTED WISHLIST
                        .requestMatchers("/api/users/*/wishlist/**").authenticated()

                        // 🔒 EVERYTHING ELSE
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                );

        // 🔐 Add JWT Filter
        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
