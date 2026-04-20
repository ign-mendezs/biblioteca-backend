package com.empresa.biblioteca_mvp.presenters.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record authRequest(
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Debe ser un email válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
