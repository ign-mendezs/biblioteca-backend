package com.empresa.biblioteca_mvp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Book {
    private String id;
    private String title;
    private String author;
    private BookStatus status;
    private String activeUserId;
    private LocalDateTime endTime; 

    // --- LÓGICA DE NEGOCIO ---

    public boolean isCurrentlyAvailable(LocalDateTime now) {
        if (this.status == BookStatus.AVAILABLE) {
            return true;
        }
        // Lazy Expiration: Si está reservado pero el tiempo ya pasó, está disponible.
        if (this.status == BookStatus.RESERVED && now.isAfter(this.endTime)) {
            return true;
        }
        return false;
    }

    public void reserve(String userId, LocalDateTime now) {
        if (!isCurrentlyAvailable(now)) {
            throw new IllegalStateException("El libro no está disponible para reserva.");
        }
        this.status = BookStatus.RESERVED;
        this.activeUserId = userId;
        this.endTime = now.plusHours(1); // Regla de negocio: 1 hora
    }

    public void borrow(String userId, LocalDateTime now) {
        boolean isReservedByMe = this.status == BookStatus.RESERVED &&
                this.activeUserId.equals(userId) &&
                !now.isAfter(this.endTime);

        if (!isCurrentlyAvailable(now) && !isReservedByMe) {
            throw new IllegalStateException("El libro no está disponible para préstamo.");
        }

        this.status = BookStatus.BORROWED;
        this.activeUserId = userId;
        this.endTime = now.plusDays(2); // Regla de negocio: 2 días
    }

    public void returnBook(String userId) {
        if (this.status != BookStatus.BORROWED) {
            throw new IllegalStateException("Solo se pueden devolver libros que están prestados.");
        }
        if (this.activeUserId == null || !this.activeUserId.equals(userId)) {
            throw new IllegalStateException("Solo el usuario que tiene el préstamo activo puede devolver este libro.");
        }
        this.status = BookStatus.AVAILABLE;
        this.activeUserId = null;
        this.endTime = null;
    }

    public void transferLoan(String newUserId, LocalDateTime now) {
        if (this.status != BookStatus.BORROWED) {
            throw new IllegalStateException("Solo se pueden transferir libros que están prestados.");
        }
        if (now.isAfter(this.endTime)) {
            throw new IllegalStateException("El préstamo ha expirado, no se puede transferir. Debe devolverlo.");
        }
        // Transferimos al nuevo usuario, MANTENIENDO el endTime original.
        this.activeUserId = newUserId;
    }
}