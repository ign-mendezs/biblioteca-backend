
package com.empresa.biblioteca_mvp.application;

import com.empresa.biblioteca_mvp.domain.model.Book;
import com.empresa.biblioteca_mvp.domain.model.BookStatus;
import com.empresa.biblioteca_mvp.domain.port.BookRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Orquestador para las operaciones exclusivas del rol LIBRARIAN.
 */
@RequiredArgsConstructor
public class BookManagement {

    private final BookRepositoryPort bookRepository;

    public Book createBook(String id, String title, String author) {
        Book newBook = Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .status(BookStatus.AVAILABLE)
                .build();

        return bookRepository.save(newBook);
    }

    public Book updateBook(String id, String title, String author) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + id));

        book.setTitle(title);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    public void removeBook(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + id));

        // Regla de negocio básica: no borrar si está prestado
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("No se puede eliminar un libro que está reservado o prestado.");
        }

        bookRepository.deleteById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}