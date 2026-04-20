package com.empresa.biblioteca_mvp.presenters.controller;

import com.empresa.biblioteca_mvp.application.BookOperations;
import com.empresa.biblioteca_mvp.domain.model.Book;
import com.empresa.biblioteca_mvp.presenters.dto.BookResponse;
import com.empresa.biblioteca_mvp.presenters.dto.TransferBookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/operations/books")
@RequiredArgsConstructor
public class BookOperationsController {

    private final BookOperations bookOperations; // Usando el nombre limpio que definimos en la arquitectura

    @PostMapping("/{id}/reserve")
    public BookResponse reserveBook(@PathVariable String id, Principal principal) {
        // principal.getName() nos devuelve el email (nuestro userId) del JWT
        Book book = bookOperations.reserveBook(id, principal.getName());
        return BookResponse.fromDomain(book);
    }

    @PostMapping("/{id}/borrow")
    public BookResponse borrowBook(@PathVariable String id, Principal principal) {
        Book book = bookOperations.borrowBook(id, principal.getName());
        return BookResponse.fromDomain(book);
    }

    @PostMapping("/{id}/return")
    public BookResponse returnBook(@PathVariable String id, Principal principal) {
        Book book = bookOperations.returnBook(id, principal.getName());
        return BookResponse.fromDomain(book);
    }

    @PostMapping("/{id}/transfer")
    public BookResponse transferBook(
            @PathVariable String id,
            Principal principal,
            @Valid @RequestBody TransferBookRequest request) {

        String currentUserId = principal.getName();
        Book book = bookOperations.transferBook(id, currentUserId, request.toUserId());
        return BookResponse.fromDomain(book);
    }
}