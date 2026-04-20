package com.empresa.biblioteca_mvp.presenters.dto;

import com.empresa.biblioteca_mvp.domain.model.Book;
import java.time.LocalDateTime;

public record BookResponse(
        String id,
        String title,
        String author,
        String status,
        LocalDateTime endTime
) {
    // Método de utilidad para mapear desde el Dominio al DTO
    public static BookResponse fromDomain(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getStatus().name(),
                book.getEndTime()
        );
    }
}