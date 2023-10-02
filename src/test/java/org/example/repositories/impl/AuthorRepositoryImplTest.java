package org.example.repositories.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.models.Author;
import org.example.repositories.AuthorRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
class AuthorRepositoryImplTest {

    AuthorRepository authorRepository;

    @Container
    static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("library_db")
                    .withUsername("postgres")
                    .withInitScript("init.sql")
                    .withPassword("postgres");

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ConnectionManager connectionManager = new ConnectionManagerImpl(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        authorRepository = new AuthorRepositoryImpl(connectionManager);
    }

    @Test
    void findAll() {
        List<Author> authors = new ArrayList<>(List.of(
                authorRepository.save(new Author("Author1")),
                authorRepository.save(new Author("Author2"))));


        List<Author> foundAll = authorRepository.findAll();

        Assertions.assertEquals(2, foundAll.size());
        Assertions.assertEquals(authors, foundAll);
    }

    @Test
    void findById() {
        Author author = authorRepository.save(new Author("Author"));
        Author foundAuthor = authorRepository.findById(author.getId());

        Assertions.assertEquals(author, foundAuthor);
    }

    @Test
    void save() {
        Author createAuthor = authorRepository.save(new Author("Author"));

        Assertions.assertNotNull(createAuthor.getId());
        Assertions.assertEquals("Author", createAuthor.getFullName());
    }

    @Test
    void updateById() {
        Author author = authorRepository.save(new Author("Author"));
        Author authorNewName = new Author("New Name");
        Author updateAuthor = authorRepository.updateById(author.getId(), authorNewName);

        Assertions.assertEquals(author.getId(), updateAuthor.getId());
        Assertions.assertEquals("New Name",updateAuthor.getFullName());
    }

    @Test
    void deleteById() {
        Author author = authorRepository.save(new Author("Author"));
        boolean delete = authorRepository.deleteById(author.getId());

        Assertions.assertTrue(delete);
    }
}