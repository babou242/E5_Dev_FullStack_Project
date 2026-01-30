package com.example.TP3_Pinia.controller;

import com.example.TP3_Pinia.model.Book;
import com.example.TP3_Pinia.model.BookCategory;
import com.example.TP3_Pinia.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // =========================
    // GET /api/books (public)
    // + filtres possibles: category, author, title
    // =========================
    @GetMapping
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) BookCategory category,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title
    ) {
        if (category != null) {
            return ResponseEntity.ok(bookService.findByCategory(category));
        }
        if (author != null && !author.isBlank()) {
            return ResponseEntity.ok(bookService.searchByAuthor(author));
        }
        if (title != null && !title.isBlank()) {
            return ResponseEntity.ok(bookService.searchByTitle(title));
        }
        return ResponseEntity.ok(bookService.findAll());
    }

    // =========================
    // GET /api/books/{id} (public)
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // POST /api/books (admin)
    // =========================
    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book) {

        // Exemple de contrôle métier : ISBN unique
        if (bookService.existsByIsbn(book.getIsbn())) {
            return ResponseEntity.badRequest()
                    .body("ISBN déjà existant");
        }

        Book created = bookService.save(book);
        return ResponseEntity
                .created(URI.create("/api/books/" + created.getId()))
                .body(created);
    }

    // =========================
    // PUT /api/books/{id} (admin)
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book bookDetails
    ) {
        return bookService.update(id, bookDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // DELETE /api/books/{id} (admin)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
