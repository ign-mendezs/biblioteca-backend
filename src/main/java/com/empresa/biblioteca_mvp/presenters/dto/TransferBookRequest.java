package com.empresa.biblioteca_mvp.presenters.dto;

import jakarta.validation.constraints.NotBlank;

public record TransferBookRequest(
        @NotBlank(message = "El ID del usuario destino es obligatorio") String toUserId
) {}