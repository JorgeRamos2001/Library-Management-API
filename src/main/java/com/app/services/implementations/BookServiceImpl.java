package com.app.services.implementations;

import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.AuthorDTO;
import com.app.models.dtos.BookDTO;
import com.app.models.entities.Author;
import com.app.models.entities.Book;
import com.app.models.entities.BookCopy;
import com.app.models.entities.Genre;
import com.app.models.enums.BookCondition;
import com.app.models.requests.CreateBookRequest;
import com.app.models.requests.UpdateBookAuthorRequest;
import com.app.repositories.IAuthorRepository;
import com.app.repositories.IBookCopyRepository;
import com.app.repositories.IBookRepository;
import com.app.repositories.IGenreRepository;
import com.app.services.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements IBookService {
    private final IBookRepository bookRepository;
    private final IBookCopyRepository bookCopyRepository;
    private final IAuthorRepository authorRepository;
    private final IGenreRepository genreRepository;

    @Override
    public BookDTO save(CreateBookRequest book) {
        List<Author> authors = new ArrayList<>();
        book.getAuthorIds().forEach(authorId -> {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author with id: " + authorId + " not found."));
            authors.add(author);
        });

        Genre genre = genreRepository.findById(book.getGenreId()).orElseThrow(() -> new ResourceNotFoundException("Genre with id: " + book.getGenreId() + " not found."));

        Book createdBook = bookRepository.save(Book
                .builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationDate(book.getPublicationDate())
                .genre(genre)
                .authors(authors)
                .build()
        );

        for(int i = 0; i < book.getBookCopies(); i++) {
            bookCopyRepository.save(BookCopy
                    .builder()
                    .book(createdBook)
                    .condition(BookCondition.ACTIVE)
                    .build()
            );
        }

        return mapToDTO(createdBook, authors, genre, book.getBookCopies());
    }

    @Override
    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found."));
        Long bookCopies = (long) bookCopyRepository.countByBookAndCondition(book, BookCondition.ACTIVE);

        return mapToDTO(book, book.getAuthors(), book.getGenre(), bookCopies);
    }

    @Override
    public BookDTO findByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new ResourceNotFoundException("Book with ISBN: " + isbn + " not found."));
        Long bookCopies = (long) bookCopyRepository.countByBookAndCondition(book, BookCondition.ACTIVE);

        return mapToDTO(book, book.getAuthors(), book.getGenre(), bookCopies);
    }

    @Override
    public List<BookDTO> findAll() {
        return mapToDTO(bookRepository.findAll());
    }

    @Override
    public List<BookDTO> findAllByTitleContainingIgnoreCase(String title) {
        return mapToDTO(bookRepository.findAllByTitleContainingIgnoreCase(title));
    }

    @Override
    public List<BookDTO> findAllByAuthorId(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author with id: " + authorId + " not found."));

        return mapToDTO(bookRepository.findAllByAuthors_Id(authorId));
    }

    @Override
    public BookDTO update(Long id, CreateBookRequest book) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found."));
        existingBook.setTitle(book.getTitle());
        existingBook.setDescription(book.getDescription());
        existingBook.setPublicationDate(book.getPublicationDate());
        existingBook.setGenre(genreRepository.findById(book.getGenreId()).orElseThrow(() -> new ResourceNotFoundException("Genre with id: " + book.getGenreId() + " not found.")));

        return mapToDTO(bookRepository.save(existingBook), existingBook.getAuthors(), existingBook.getGenre(), (long) bookCopyRepository.countByBookAndCondition(existingBook, BookCondition.ACTIVE));
    }

    @Override
    public BookDTO updateAuthorsByBookId(Long bookId, UpdateBookAuthorRequest request) {
        Book existingBook = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + request.getBookId() + " not found."));

        List<Author> authors = new ArrayList<>();
        request.getAuthorIds().forEach(authorId -> {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author with id: " + authorId + " not found."));
            authors.add(author);
        });
        existingBook.setAuthors(authors);

        return mapToDTO(bookRepository.save(existingBook), authors, existingBook.getGenre(), (long) bookCopyRepository.countByBookAndCondition(existingBook, BookCondition.ACTIVE));
    }

    @Override
    public void delete(Long id) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " not found."));
        bookRepository.delete(existingBook);
    }

    private BookDTO mapToDTO(Book book, List<Author> authors, Genre genre, Long bookCopies) {
        return BookDTO
                .builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationDate(book.getPublicationDate())
                .genre(genre.getName())
                .bookCopies(bookCopies)
                .authors(authors.stream().map(this::mapToDTO).toList())
                .build();
    }

    private AuthorDTO mapToDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .nationality(author.getNationality())
                .build();
    }

    private List<BookDTO> mapToDTO(List<Book> books) {

        List<BookDTO> bookDTOS = new ArrayList<>();

        books.forEach(book -> {
            bookDTOS.add(mapToDTO(book, book.getAuthors(), book.getGenre(), (long) bookCopyRepository.countByBookAndCondition(book, BookCondition.ACTIVE)));
        });

        return bookDTOS;
    }
}
