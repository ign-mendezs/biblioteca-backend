package com.empresa.biblioteca_mvp.infrastructure.config;

import com.empresa.biblioteca_mvp.application.BookManagement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class DatabaseSeederConfig {

    @Bean
    public CommandLineRunner initDatabase(BookManagement bookManagement) {
        return args -> {
            // Solo creamos datos si la base de datos está vacía
            if (bookManagement.getAllBooks().isEmpty()) {
                System.out.println("Cargando libros iniciales en la base de datos H2...");

                bookManagement.createBook(UUID.randomUUID().toString(), "El Señor de los Anillos", "J.R.R. Tolkien");
                bookManagement.createBook(UUID.randomUUID().toString(), "1984", "George Orwell");
                bookManagement.createBook(UUID.randomUUID().toString(), "Cien Años de Soledad", "Gabriel García Márquez");
                bookManagement.createBook(UUID.randomUUID().toString(), "Clean Architecture", "Robert C. Martin");
                bookManagement.createBook(UUID.randomUUID().toString(), "Domain-Driven Design", "Eric Evans");

                System.out.println("¡Libros cargados exitosamente!");
            }
        };
    }
}