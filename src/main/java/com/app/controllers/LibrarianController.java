package com.app.controllers;

import com.app.models.dtos.LibrarianDTO;
import com.app.models.requests.CreateLibrarianRequest;
import com.app.services.ILibrarianService;
import com.app.services.implementations.LibrarianServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/librarians")
@RequiredArgsConstructor
public class LibrarianController {
    private final ILibrarianService librarianService;

    @PostMapping
    public ResponseEntity<LibrarianDTO> save(@Valid @RequestBody CreateLibrarianRequest librarian) {
        return new ResponseEntity<>(librarianService.save(librarian), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibrarianDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(librarianService.findById(id));
    }

    @GetMapping("/dui/{dui}")
    public ResponseEntity<LibrarianDTO> findByDui(@PathVariable String dui){
        return ResponseEntity.ok(librarianService.findByDui(dui));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<LibrarianDTO> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(librarianService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<LibrarianDTO>> findAll(){
        return ResponseEntity.ok(librarianService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<LibrarianDTO>> findAllActiveLibrarians() {
        return ResponseEntity.ok(librarianService.findAllActiveLibrarians());
    }

    @GetMapping("/search")
    public ResponseEntity<List<LibrarianDTO>> findByName(@RequestParam String name){
        return ResponseEntity.ok(librarianService.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibrarianDTO> update(@PathVariable Long id, @Valid @RequestBody CreateLibrarianRequest librarian){
        return ResponseEntity.ok(librarianService.update(id, librarian));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        librarianService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
