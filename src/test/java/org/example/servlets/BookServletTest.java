package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Book;
import org.example.services.BookService;
import org.example.servlets.DTO.BookRequestDTO;
import org.example.servlets.DTO.BookResponseDTO;
import org.example.servlets.mapper.BookMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServletTest {
    @Mock
    BookService bookService;
    @Mock
    BookMapper mapper;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    PrintWriter printWriter;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    BufferedReader reader;
    BookServlet bookByIdServlet;

    @BeforeEach
    void setUp() {
        bookByIdServlet = new BookServlet(bookService, mapper);
    }

    @Test
    void doGet_validId_test() throws Exception {
        Book book = new Book(UUID.randomUUID(), "Book", 1900);
        BookResponseDTO bookResponseDTO = new BookResponseDTO(book.getId(), book.getTitle(), book.getYear());

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(book.getId()));
        when(bookService.findById(book.getId())).thenReturn(book);
        when(mapper.mapToResponseDto(book)).thenReturn(bookResponseDTO);
        objectMapper.writeValueAsString(bookResponseDTO);

        bookByIdServlet.doGet(request, response);

        verify(response, times(1)).getWriter();
        verify(request, times(1)).getParameter("id");
        verify(bookService, times(1)).findById(book.getId());
        verify(mapper, times(1)).mapToResponseDto(book);
        verify(objectMapper, times(1)).writeValueAsString(bookResponseDTO);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertEquals(String.valueOf(book.getId()), request.getParameter("id"));
        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doGet_invalidId_test() throws Exception {
        String invalidId = "fs4jws";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);

        bookByIdServlet.doGet(request, response);

        verify(printWriter, times(1)).write("Invalid book ID");
        verify(response, times(1)).setStatus(400);

        Assertions.assertEquals(invalidId, request.getParameter("id"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.findById(UUID.fromString(invalidId)));
    }

    @Test
    void doGet_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        bookByIdServlet.doGet(request, response);

        verify(printWriter, times(1)).write("ID must not be empty");
        verify(response, times(1)).setStatus(400);
        verify(bookService, never()).findById(any());

        Assertions.assertNull(request.getParameter("id"));
    }

    @Test
    void doGet_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(bookService.findById(notFoundId)).thenReturn(null);

        bookByIdServlet.doGet(request, response);

        verify(printWriter, times(1)).write("Book not found by ID " + notFoundId);
        verify(response, times(1)).setStatus(404);

        Assertions.assertNull(bookService.findById(notFoundId));
    }

    @Test
    void doPostTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO("Book", 1900);
        Book createBook = new Book(UUID.randomUUID(), "Book", 1900);
        BookResponseDTO bookResponseDTO = new BookResponseDTO(createBook.getId(), createBook.getTitle(), createBook.getYear());
        String requestBody = "{\"title\": \"Book\", \"year\": 1900}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, BookRequestDTO.class);
        when(mapper.mapToEntity(bookRequestDTO)).thenReturn(createBook);
        when(bookService.create(createBook)).thenReturn(createBook);
        when(mapper.mapToResponseDto(createBook)).thenReturn(bookResponseDTO);
        objectMapper.writeValueAsString(bookResponseDTO);

        bookByIdServlet.doPost(request, response);

        verify(request, times(1)).getReader();
        verify(objectMapper, times(1)).readValue(requestBody, BookRequestDTO.class);
        verify(mapper, times(1)).mapToEntity(bookRequestDTO);
        verify(bookService, times(1)).create(createBook);
        verify(mapper, times(1)).mapToResponseDto(createBook);
        verify(objectMapper, times(1)).writeValueAsString(bookResponseDTO);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doPut_validId_test() throws Exception {
        UUID id = UUID.randomUUID();
        String newTitle = "New book name";
        int newYear = 1990;
        Book book = new Book(id, "Book", 1900);
        BookRequestDTO bookRequestDTO = new BookRequestDTO(newTitle, newYear);
        Book updateBook = new Book(id, newTitle, newYear);
        BookResponseDTO bookResponseDTO = new BookResponseDTO(id, newTitle, newYear);
        String requestBody = "{\"title\": \"New book name\", \"year\": 1990}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, BookRequestDTO.class);
        when(bookService.findById(id)).thenReturn(book);
        when(mapper.mapToEntity(bookRequestDTO)).thenReturn(updateBook);
        when(bookService.updateById(id, updateBook)).thenReturn(updateBook);
        when(mapper.mapToResponseDto(updateBook)).thenReturn(bookResponseDTO);
        objectMapper.writeValueAsString(bookResponseDTO);

        bookByIdServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(request, times(1)).getReader();
        verify(objectMapper, times(1)).readValue(requestBody, BookRequestDTO.class);
        verify(mapper, times(1)).mapToEntity(bookRequestDTO);
        verify(bookService, times(1)).updateById(id, updateBook);
        verify(mapper, times(1)).mapToResponseDto(updateBook);
        verify(objectMapper, times(1)).writeValueAsString(bookResponseDTO);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doPut_invalidId_test() throws Exception {
        String invalidId = "lf2v2";
        Book book = new Book("Book", 1900);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn("{\"title\": \"Book\", \"year\": 1990}", null);

        bookByIdServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("Invalid book ID");

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.updateById(UUID.fromString(invalidId), book));
    }

    @Test
    void doPut_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        bookByIdServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("ID must not be empty");
        verify(bookService, never()).updateById(any(), any());
    }

    @Test
    void doPut_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();
        String requestBody = "{\"title\": \"Book\", \"year\": \"1900\"}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, BookRequestDTO.class);
        when(bookService.findById(notFoundId)).thenReturn(null);

        bookByIdServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(404);
        verify(printWriter, times(1)).write("Book not found by ID " + notFoundId);
        verify(bookService, never()).updateById(any(), any());
    }

    @Test
    void doDelete_validId_test() throws Exception {
        Book book = new Book(UUID.randomUUID(), "Book", 1900);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(book.getId()));
        when(bookService.findById(book.getId())).thenReturn(book);
        when(bookService.delete(book.getId())).thenReturn(true);

        bookByIdServlet.doDelete(request, response);

        verify(response, times(1)).getWriter();
        verify(request, times(1)).getParameter("id");
        verify(bookService, times(1)).delete(book.getId());
        verify(response, times(1)).setStatus(200);
        verify(printWriter, times(1)).write("Book was deleted successfully by ID " + book.getId());

        Assertions.assertTrue(bookService.delete(book.getId()));
    }

    @Test
    void doDelete_invalidId_test() throws Exception {
        String invalidId = "jf3o4af";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(invalidId));

        bookByIdServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("Invalid book ID");
        verify(response, times(1)).setStatus(400);

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.delete(UUID.fromString(invalidId)));
    }

    @Test
    void doDelete_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        bookByIdServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("ID must not be empty");
        verify(response, times(1)).setStatus(400);
        verify(bookService, never()).delete(any());

        Assertions.assertNull(request.getParameter("id"));
    }

    @Test
    void doDelete_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(bookService.findById(notFoundId)).thenReturn(null);

        bookByIdServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("Book not found by ID " + notFoundId);
        verify(response, times(1)).setStatus(404);
        verify(bookService, never()).delete(notFoundId);

        Assertions.assertNull(bookService.findById(notFoundId));
    }
}