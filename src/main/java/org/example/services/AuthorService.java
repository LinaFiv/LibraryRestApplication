package org.example.services;

import org.example.models.Author;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    Author findById(UUID id);

    List<Author> findAll();

    Author create(Author author);

    Author updateById(UUID id, Author updateAuthor);

    boolean delete(UUID id);
}
