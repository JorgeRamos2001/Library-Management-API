package com.app.services.implementations;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.OperationNotPermittedException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.entities.Book;
import com.app.models.entities.BookCopy;
import com.app.models.enums.BookCondition;
import com.app.repositories.IBookCopyRepository;
import com.app.repositories.IBookRepository;
import com.app.repositories.ILoanRepository;
import com.app.services.IBookCopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCopyService implements IBookCopyService {

    private final IBookCopyRepository bookCopyRepository;
    private final IBookRepository bookRepository;
    private final ILoanRepository loanRepository;

    @Override
    public void addBookCopy(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found."));

        List<BookCopy> copiesToAdd = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            copiesToAdd.add(BookCopy.builder()
                    .book(book)
                    .condition(BookCondition.ACTIVE)
                    .build());
        }

        bookCopyRepository.saveAll(copiesToAdd);
    }

    @Override
    public void updateBookCopy(Long bookId, Long copyId, String condition) {
        BookCopy bookCopy = bookCopyRepository.findById(copyId).orElseThrow(() -> new ResourceNotFoundException("Book copy with id: " + copyId + " not found."));

        if (!bookCopy.getBook().getId().equals(bookId)) {
            throw new BadRequestException("BookCopy with id " + copyId + " does not belong to Book with id " + bookId);
        }

        try {
            BookCondition newCondition = BookCondition.valueOf(condition.toUpperCase());
            bookCopy.setCondition(newCondition);
            bookCopyRepository.save(bookCopy);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado inválido: " + condition + ". Valores permitidos: NEW, USED, DAMAGED, LOST");
        }
    }

    @Override
    public void removeBookCopy(Long bookId, Long copyId) {
        BookCopy bookCopy = bookCopyRepository.findById(copyId).orElseThrow(() -> new ResourceNotFoundException("Book copy with id: " + copyId + " not found."));

        if (!bookCopy.getBook().getId().equals(bookId)) {
            throw new BadRequestException("BookCopy with id " + copyId + " does not belong to Book with id " + bookId);
        }

        if(loanRepository.existsByBookCopy(bookCopy)) throw new OperationNotPermittedException("Cannot delete book copy with active loan.");

        bookCopyRepository.delete(bookCopy);
    }
}