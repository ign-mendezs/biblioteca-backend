package com.empresa.biblioteca_mvp.presenters.dto;

public record authResponse(
        String token,
        String role // Opcional, pero muy útil para que el Frontend sepa qué menú mostrar
) {}
