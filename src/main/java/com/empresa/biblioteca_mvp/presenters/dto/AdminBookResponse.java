package com.empresa.biblioteca_mvp.presenters.dto;

import com.empresa.biblioteca_mvp.domain.model.Book;
import java.time.LocalDateTime;

public record AdminBookResponse(
        String id,
        String title,
        String author,
        String status,
        String activeUserId,
        LocalDateTime endTime
) {
    // DTO para administración: incluye el usuario con reserva/préstamo activo.
    public static AdminBookResponse fromDomain(Book book) {
        return new AdminBookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getStatus().name(),
                book.getActiveUserId(),
                book.getEndTime()
        );
    }
}
