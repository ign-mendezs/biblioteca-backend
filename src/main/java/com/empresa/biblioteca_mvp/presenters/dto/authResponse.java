package com.empresa.biblioteca_mvp.presenters.dto;

public record authResponse(
        String token,
        String role
) {}
