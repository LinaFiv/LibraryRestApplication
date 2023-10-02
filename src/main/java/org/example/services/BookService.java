package org.example.services;

import org.example.models.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {
    Book findById(UUID id);

    List<Book> findAll();

    Book create(Book book);

    Book updateById(UUID id, Book updateBook);

    boolean delete(UUID id);
}
