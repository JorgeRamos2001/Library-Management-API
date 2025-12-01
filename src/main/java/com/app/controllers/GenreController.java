package com.app.controllers;

import com.app.models.dtos.GenreDTO;
import com.app.models.requests.CreateGenreRequest;
import com.app.services.IGenreService;
import com.app.services.implementations.GenreServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final IGenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDTO> save(@Valid @RequestBody CreateGenreRequest genre) {
        return new ResponseEntity(genreService.save(genre), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<GenreDTO> findByName(@RequestParam String name) {
        return ResponseEntity.ok(genreService.findByName(name));
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }
}
