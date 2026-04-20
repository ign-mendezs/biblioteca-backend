
package com.empresa.biblioteca_mvp.application;

import com.empresa.biblioteca_mvp.domain.model.Book;
import com.empresa.biblioteca_mvp.domain.port.BookRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Orquestador para las transacciones de los usuarios de la biblioteca.
 */
@RequiredArgsConstructor
public class BookOperations {

    private final BookRepositoryPort bookRepository;

    public Book reserveBook(String bookId, String userId) {
        Book book = getBookOrThrow(bookId);

        // Delegamos la lógica de negocio al dominio
        book.reserve(userId, LocalDateTime.now());

        return bookRepository.save(book);
    }

    public Book borrowBook(String bookId, String userId) {
        Book book = getBookOrThrow(bookId);

        book.borrow(userId, LocalDateTime.now());

        return bookRepository.save(book);
    }

    public Book returnBook(String bookId, String userId) {
        Book book = getBookOrThrow(bookId);

        book.returnBook(userId);

        return bookRepository.save(book);
    }

    public Book transferBook(String bookId, String fromUserId, String toUserId) {
        Book book = getBookOrThrow(bookId);

        // Validación de seguridad para que un usuario no transfiera libros de otros
        if (!fromUserId.equals(book.getActiveUserId())) {
            throw new IllegalStateException("Solo el usuario actual del préstamo puede transferirlo.");
        }

        book.transferLoan(toUserId, LocalDateTime.now());

        return bookRepository.save(book);
    }

    private Book getBookOrThrow(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + id));
    }
}