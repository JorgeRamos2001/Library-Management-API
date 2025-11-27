package com.app.repositories;

import com.app.models.entities.Author;
import com.app.models.entities.Book;
import com.app.models.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAllByTitleContainingIgnoreCase(String title);
    List<Book> findAllByGenre(Genre genre);
    List<Book> findAllByAuthors_Id(Long id);
    boolean existsByIsbn(String isbn);
}
