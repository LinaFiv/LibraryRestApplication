package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Book;
import org.example.services.BookService;
import org.example.servlets.DTO.BookResponseDTO;
import org.example.servlets.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServletTest {

    @Mock
    BookService bookService;
    @Mock
    BookMapper mapper;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    PrintWriter printWriter;
    BooksServlet booksServlet;

    @BeforeEach
    void setUp() {
        booksServlet = new BooksServlet(bookService, mapper);
    }

    @Test
    void doGet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        List<Book> books = getBookList(List.of("Book1", "Book2", "Book3"));
        List<BookResponseDTO> bookResponseDTOS = getBookResponseDTOList(books);

        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.findAll()).thenReturn(books);
        when(mapper.mapToList(books)).thenReturn(bookResponseDTOS);
        objectMapper.writeValueAsString(bookResponseDTOS);

        booksServlet.doGet(request, response);

        verify(bookService, times(1)).findAll();
        verify(mapper, times(1)).mapToList(books);
        verify(objectMapper, times(1)).writeValueAsString(bookResponseDTOS);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).println(anyString());
    }

    private List<Book> getBookList(List<String> names) {
        return names.stream().map(e -> new Book(UUID.randomUUID(), e, 1900)).toList();
    }

    private List<BookResponseDTO> getBookResponseDTOList(List<Book> bookList) {
        return bookList.stream().map(e -> new BookResponseDTO(e.getId(), e.getTitle(), e.getYear())).toList();
    }
}