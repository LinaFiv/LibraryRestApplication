package org.example.services.impl;

import org.example.models.Author;
import org.example.repositories.AuthorRepository;
import org.example.repositories.impl.AuthorRepositoryImpl;
import org.example.services.AuthorService;

import java.util.List;
import java.util.UUID;

public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl() {
        this.authorRepository = new AuthorRepositoryImpl();
    }

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author findById(UUID id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author updateById(UUID id, Author updateAuthor) {
        return authorRepository.updateById(id, updateAuthor);
    }

    @Override
    public boolean delete(UUID id) {
        return authorRepository.deleteById(id);
    }
}
