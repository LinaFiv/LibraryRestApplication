package org.example.services.impl;

import org.example.models.Book;
import org.example.repositories.BookRepository;
import org.example.services.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;
    BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void findByIdTest() {
        Book book = new Book("Book", 1900);
        UUID id = UUID.randomUUID();
        book.setId(id);

        when(bookRepository.findById(id)).thenReturn(book);

        Book bookServiceById = bookService.findById(id);

        verify(bookRepository, times(1)).findById(id);
        Assertions.assertEquals(book, bookServiceById);
    }

    @Test
    void findAllTest() {
        List<Book> books = getBookList(List.of("Book1", "Book2", "Book3"));

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> bookList = bookService.findAll();

        verify(bookRepository, times(1)).findAll();
        Assertions.assertEquals(3, bookList.size());
    }

    @Test
    void createTest() {
        Book bookCreate = new Book("Book Create", 1900);
        UUID id = UUID.randomUUID();

        when(bookRepository.save(bookCreate)).then((Answer<Book>) invocationOnMock -> {
            Book book = invocationOnMock.getArgument(0);
            book.setId(id);
            return book;
        });

        Book book = bookService.create(bookCreate);

        verify(bookRepository, times(1)).save(bookCreate);
        Assertions.assertNotNull(bookCreate.getId());
        Assertions.assertEquals(id, bookCreate.getId());
        Assertions.assertEquals(book, bookCreate);
    }

    @Test
    void updateByIdTest() {
        Book book = new Book("Book", 1900);
        UUID id = UUID.randomUUID();
        book.setId(id);

        when(bookRepository.updateById(id, book)).then((Answer<Book>) invocationOnMock -> {
            Book bookMockArgument = invocationOnMock.getArgument(1);
            bookMockArgument.setTitle("Update book");
            bookMockArgument.setYear(2000);
            return bookMockArgument;
        });

        Book updateById = bookService.updateById(id, book);

        verify(bookRepository, times(1)).updateById(id, book);
        Assertions.assertEquals(book, updateById);
        Assertions.assertEquals("Update book", updateById.getTitle());
        Assertions.assertEquals(2000, updateById.getYear());
    }

    @Test
    void deleteTest() {
        Book book1 = new Book("Book", 1900);
        UUID id1 = UUID.randomUUID();
        book1.setId(id1);

        when(bookRepository.deleteById(id1)).thenReturn(true);

        boolean delete = bookService.delete(id1);

        verify(bookRepository, times(1)).deleteById(id1);
        Assertions.assertTrue(delete);
    }

    private List<Book> getBookList(List<String> names) {
        return names.stream().map(e -> new Book(UUID.randomUUID(), e, 1900)).toList();
    }
}