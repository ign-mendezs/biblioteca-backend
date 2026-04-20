package com.empresa.biblioteca_mvp.presenters.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBookRequest(
        @NotBlank(message = "El ID es obligatorio") String id,
        @NotBlank(message = "El título es obligatorio") String title,
        @NotBlank(message = "El autor es obligatorio") String author
) {}