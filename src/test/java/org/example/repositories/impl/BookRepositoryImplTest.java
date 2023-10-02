package org.example.repositories.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.models.Book;
import org.example.repositories.BookRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
class BookRepositoryImplTest {

    BookRepository bookRepository;

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
        bookRepository = new BookRepositoryImpl(connectionManager);
    }

    @Test
    void findAll() {
        List<Book> books = new ArrayList<>(List.of(
                bookRepository.save(new Book("Book1", 1900)),
                bookRepository.save(new Book("Book2", 1900))
        ));

        List<Book> foundAll = bookRepository.findAll();

        Assertions.assertEquals(2, foundAll.size());
        Assertions.assertEquals(books, foundAll);
    }

    @Test
    void findById() {
        Book book = bookRepository.save(new Book("Book", 1900));
        Book foundBook = bookRepository.findById(book.getId());

        Assertions.assertEquals(book, foundBook);
    }

    @Test
    void save() {
        Book createBook = bookRepository.save(new Book("Book", 1900));

        Assertions.assertNotNull(createBook.getId());
        Assertions.assertEquals("Book", createBook.getTitle());
        Assertions.assertEquals(1900, createBook.getYear());
    }

    @Test
    void updateById() {
        Book book = bookRepository.save(new Book("Book", 1900));
        Book bookNew = new Book("New title", 2000);
        Book updateBook = bookRepository.updateById(book.getId(), bookNew);

        Assertions.assertEquals(book.getId(), updateBook.getId());
        Assertions.assertEquals("New title", updateBook.getTitle());
        Assertions.assertEquals(2000, updateBook.getYear());
    }

    @Test
    void deleteById() {
        Book book = bookRepository.save(new Book("Book", 1900));
        boolean delete = bookRepository.deleteById(book.getId());

        Assertions.assertTrue(delete);
    }
}