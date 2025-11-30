package com.app.services;

public interface IBookCopyService {
    void addBookCopy(Long bookId, int quantity);
    void updateBookCopy(Long bookId, Long copyId, String condition);
    void removeBookCopy(Long bookId, Long copyId);
}
