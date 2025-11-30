package com.app.repositories;

import com.app.models.entities.Book;
import com.app.models.entities.BookCopy;
import com.app.models.entities.Member;
import com.app.models.enums.BookCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findAllByBook(Book book);
    int countByBookAndCondition(Book book, BookCondition status);
    List<BookCopy> findAllByBookIdAndCondition(Long bookId, BookCondition status);
}
