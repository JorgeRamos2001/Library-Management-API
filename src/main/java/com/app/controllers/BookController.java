package com.app.controllers;

import com.app.models.dtos.BookDTO;
import com.app.models.requests.AddCopiesRequest;
import com.app.models.requests.CreateBookRequest;
import com.app.models.requests.UpdateBookAuthorRequest;
import com.app.services.IBookCopyService;
import com.app.services.IBookService;
import com.app.services.implementations.BookServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;
    private final IBookCopyService bookCopyService;

    @PostMapping
    public ResponseEntity<BookDTO> save(@Valid @RequestBody CreateBookRequest book) {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> findByIsbn(@PathVariable String isbn){
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> findAll(){
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchByTitle(@RequestParam String title){
        return ResponseEntity.ok(bookService.findAllByTitleContainingIgnoreCase(title));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookDTO>> findAllByAuthorId(@PathVariable Long authorId){
        return ResponseEntity.ok(bookService.findAllByAuthorId(authorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @Valid @RequestBody CreateBookRequest book){
        return ResponseEntity.ok(bookService.update(id, book));
    }

    @PutMapping("/{id}/authors")
    public ResponseEntity<BookDTO> addAuthors(@PathVariable Long id, @RequestBody UpdateBookAuthorRequest request){
        return new ResponseEntity<>(bookService.updateAuthorsByBookId(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/copies")
    public ResponseEntity<Void> addCopies(@PathVariable Long id, @Valid @RequestBody AddCopiesRequest request) {
        bookCopyService.addBookCopy(id, request.getCopies());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{bookId}/copies/{copyId}")
    public ResponseEntity<Void> removeCopy(@PathVariable Long bookId, @PathVariable Long copyId) {
        bookCopyService.removeBookCopy(bookId, copyId);
        return ResponseEntity.noContent().build();
    }
}
