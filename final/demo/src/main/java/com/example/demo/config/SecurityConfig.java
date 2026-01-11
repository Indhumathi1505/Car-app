package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
    // ❌ Disable CSRF for your APIs so the login form works
.csrf(csrf -> csrf.disable())
    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    // ✅ Authorization rules
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/","/test","/force-session","/recommended").permitAll()
    .requestMatchers("/api/auth/**").permitAll() // 👈 THIS MUST BE HERE
    .requestMatchers("/api/cars/**","/api/reviews/**","/ws/**","/app/**","/api/chat/**","/topic/**",  "/api/seller/**").permitAll()
    .anyRequest().authenticated()
)
    .formLogin(form -> form.disable());


        return http.build();
    }

    // ✅ CORS CONFIGURATION
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 🔥 REQUIRED FOR SESSION
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // Optional: Register CorsFilter explicitly (safe)
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
