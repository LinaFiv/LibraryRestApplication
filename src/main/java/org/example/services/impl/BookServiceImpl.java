package org.example.services.impl;

import org.example.models.Book;
import org.example.repositories.BookRepository;
import org.example.repositories.impl.BookRepositoryImpl;
import org.example.services.BookService;

import java.util.List;
import java.util.UUID;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl() {
        this.bookRepository = new BookRepositoryImpl();
    }

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findById(UUID id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateById(UUID id, Book updateBook) {
        return bookRepository.updateById(id, updateBook);
    }

    @Override
    public boolean delete(UUID id) {
        return bookRepository.deleteById(id);
    }
}
