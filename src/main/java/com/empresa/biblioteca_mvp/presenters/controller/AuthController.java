package com.empresa.biblioteca_mvp.presenters.controller;

import com.empresa.biblioteca_mvp.infrastructure.security.jwtService;
import com.empresa.biblioteca_mvp.presenters.dto.authRequest;
import com.empresa.biblioteca_mvp.presenters.dto.authResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final jwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<authResponse> login(@Valid @RequestBody authRequest request) {
        // 1. Spring Security valida las credenciales. Si fallan, lanza un 403 Forbidden automáticamente.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Si pasamos el punto anterior, el usuario es válido. Lo buscamos.
        UserDetails user = userDetailsService.loadUserByUsername(request.email());

        // 3. Generamos el JWT
        String jwtToken = jwtService.generateToken(user);

        // 4. Extraemos el rol (ej. "ROLE_LIBRARIAN") para enviarlo al frontend
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        return ResponseEntity.ok(new authResponse(jwtToken, role));
    }
}