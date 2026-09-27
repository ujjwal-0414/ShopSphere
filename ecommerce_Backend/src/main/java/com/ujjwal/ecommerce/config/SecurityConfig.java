package com.ujjwal.ecommerce.config;

import com.ujjwal.ecommerce.enums.RoleType;
import com.ujjwal.ecommerce.security.CustomAccessDeniedHandler;
import com.ujjwal.ecommerce.security.CustomAuthenticationEntryPoint;
import com.ujjwal.ecommerce.security.CustomUserDetailsService;
import com.ujjwal.ecommerce.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                // Enable CORS support using the custom CorsConfigurationSource bean
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authenticationProvider(authenticationProvider())
                // Configures custom handlers for unauthenticated (401) and unauthorized(403) security errors
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                .authorizeHttpRequests(auth -> auth
                        // auth request is a public request.Therefore, guest users can and allow the internal /error endpoint
                        .requestMatchers("/api/auth/**","/error").permitAll()
                        // anyone can search products
                        .requestMatchers(HttpMethod.GET,"/api/product/**").permitAll()
                        // Only ADMIN can create/update/delete products
                        .requestMatchers(HttpMethod.POST, "/api/product/**").hasRole(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/product/**").hasRole(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasRole(RoleType.ADMIN.name())
                        // Only ADMIN can create/update/delete categories
                        .requestMatchers(HttpMethod.POST, "/api/category/**").hasRole(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole(RoleType.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasRole(RoleType.ADMIN.name())
                        // only loggedin user can see their orders
                        .requestMatchers("/api/orders/**").authenticated()
                        // request accessible only to admin
                        .requestMatchers("/api/admin/**").hasRole(RoleType.ADMIN.name())
                        // all cart requests require authentication
                        .requestMatchers("/api/cart/**").authenticated()
                        // all payment requests require authentication
                        .requestMatchers("/api/payments/**").authenticated()
                        // address requests require authentication
                        .requestMatchers("/api/addresses/**").authenticated()
                        // every other request requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // encapsulating the authentication logic using AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService); // DAO authenticate users stored in database
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Exposes CORS rules allowing specified frontend origins to interact with the API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
