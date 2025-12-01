package com.app.controllers;

import com.app.models.dtos.AuthorDTO;
import com.app.models.requests.CreateAuthorRequest;
import com.app.services.IAuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final IAuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorDTO> save(@Valid @RequestBody CreateAuthorRequest author) {
        return new ResponseEntity<>(authorService.save(author), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAll() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AuthorDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(authorService.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @Valid @RequestBody CreateAuthorRequest author) {
        return ResponseEntity.ok(authorService.update(id, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
