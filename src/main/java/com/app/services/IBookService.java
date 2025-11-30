package com.app.services;

import com.app.models.dtos.BookDTO;
import com.app.models.requests.CreateBookRequest;
import com.app.models.requests.UpdateBookAuthorRequest;

import java.util.List;

public interface IBookService {
    BookDTO save(CreateBookRequest book);
    BookDTO findById(Long id);
    BookDTO findByIsbn(String isbn);
    List<BookDTO> findAll();
    List<BookDTO> findAllByTitleContainingIgnoreCase(String title);
    List<BookDTO> findAllByAuthorId(Long authorId);
    BookDTO update(Long id, CreateBookRequest book);
    BookDTO updateAuthorsByBookId(UpdateBookAuthorRequest request);
    void delete(Long id);
}
