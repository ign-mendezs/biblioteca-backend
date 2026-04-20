package com.empresa.biblioteca_mvp.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitimos el origen de tu frontend (Vite)
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        // Permitimos los métodos HTTP que usaremos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permitimos todos los headers (incluyendo Authorization para nuestro JWT)
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicamos esta regla a todas las rutas de la API
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }

    // 1. Usuarios en Memoria (Nuestra base de datos falsa para el MVP)
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails librarian = User.builder()
                .username("bibliotecario@empresa.com")
                .password(passwordEncoder().encode("admin123"))
                .roles("LIBRARIAN") // Spring le agrega internamente el prefijo "ROLE_"
                .build();

        UserDetails commonUser = User.builder()
                .username("usuario@empresa.com")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        UserDetails secondCommonUser = User.builder()
                .username("usuario2@empresa.com")
                .password(passwordEncoder().encode("user456"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(librarian, commonUser, secondCommonUser);
    }

    // 2. Configuración de Rutas y Filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // Desactivamos CSRF porque usamos JWT (Stateless)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll() // Rutas públicas (Login)
                        .requestMatchers("/h2-console/**").permitAll()  // Permitir acceso a H2
                        .requestMatchers("/api/v1/management/**").hasRole("LIBRARIAN") // Solo bibliotecarios
                        .requestMatchers("/api/v1/operations/**").hasAnyRole("USER", "LIBRARIAN") // Usuarios (y bibliotecarios por conveniencia)
                        .anyRequest().authenticated() // Cualquier otra ruta debe estar autenticada
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No guardar sesiones en memoria (100% REST)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Colocar nuestro filtro JWT antes del filtro de usuario/password estándar

        // Excepción necesaria para que la consola H2 funcione con frames
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}