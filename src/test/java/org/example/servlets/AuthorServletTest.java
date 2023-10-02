package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Author;
import org.example.services.AuthorService;
import org.example.servlets.DTO.AuthorRequestDTO;
import org.example.servlets.DTO.AuthorResponseDTO;
import org.example.servlets.mapper.AuthorMapper;
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
class AuthorServletTest {

    @Mock
    AuthorService authorService;
    @Mock
    AuthorMapper mapper;
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
    AuthorServlet authorServlet;

    @BeforeEach
    void setUp() {
        authorServlet = new AuthorServlet(authorService, mapper);
    }

    @Test
    void doGet_validId_test() throws Exception {
        Author author = new Author(UUID.randomUUID(), "Author");
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(author.getId(), author.getFullName());

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(author.getId()));
        when(authorService.findById(author.getId())).thenReturn(author);
        when(mapper.mapToResponseDTO(author)).thenReturn(authorResponseDTO);
        objectMapper.writeValueAsString(authorResponseDTO);

        authorServlet.doGet(request, response);

        verify(response, times(1)).getWriter();
        verify(request, times(1)).getParameter("id");
        verify(authorService, times(1)).findById(author.getId());
        verify(mapper, times(1)).mapToResponseDTO(author);
        verify(objectMapper, times(1)).writeValueAsString(authorResponseDTO);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertEquals(String.valueOf(author.getId()), request.getParameter("id"));
        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doGet_invalidId_test() throws Exception {
        String invalidId = "32095jwf";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);

        authorServlet.doGet(request, response);

        verify(printWriter, times(1)).write("Invalid author ID");
        verify(response, times(1)).setStatus(400);

        Assertions.assertEquals(invalidId, request.getParameter("id"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorService.findById(UUID.fromString(invalidId)));
    }

    @Test
    void doGet_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        authorServlet.doGet(request, response);

        verify(printWriter, times(1)).write("ID must not be empty");
        verify(response, times(1)).setStatus(400);
        verify(authorService, never()).findById(any());

        Assertions.assertNull(request.getParameter("id"));
    }

    @Test
    void doGet_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(authorService.findById(notFoundId)).thenReturn(null);

        authorServlet.doGet(request, response);

        verify(printWriter, times(1)).write("Author not found by ID " + notFoundId);
        verify(response, times(1)).setStatus(404);

        Assertions.assertNull(authorService.findById(notFoundId));
    }

    @Test
    void doPostTest() throws Exception {
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO("Author");
        Author createAuthor = new Author(UUID.randomUUID(), "Author");
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(createAuthor.getId(), createAuthor.getFullName());
        String requestBody = "{\"fullName\": \"Author\"}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, AuthorRequestDTO.class);
        when(mapper.mapToEntity(authorRequestDTO)).thenReturn(createAuthor);
        when(authorService.create(createAuthor)).thenReturn(createAuthor);
        when(mapper.mapToResponseDTO(createAuthor)).thenReturn(authorResponseDTO);
        objectMapper.writeValueAsString(authorResponseDTO);

        authorServlet.doPost(request, response);

        verify(request, times(1)).getReader();
        verify(objectMapper, times(1)).readValue(requestBody, AuthorRequestDTO.class);
        verify(mapper, times(1)).mapToEntity(authorRequestDTO);
        verify(authorService, times(1)).create(createAuthor);
        verify(mapper, times(1)).mapToResponseDTO(createAuthor);
        verify(objectMapper, times(1)).writeValueAsString(authorResponseDTO);
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
        String newName = "New author name";
        Author author = new Author(id, "Author");
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO(newName);
        Author updateAuthor = new Author(id, newName);
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(id, newName);
        String requestBody = "{\"fullName\": \"New author name\"}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(id));
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, AuthorRequestDTO.class);
        when(authorService.findById(id)).thenReturn(author);
        when(mapper.mapToEntity(authorRequestDTO)).thenReturn(updateAuthor);
        when(authorService.updateById(id, updateAuthor)).thenReturn(updateAuthor);
        when(mapper.mapToResponseDTO(updateAuthor)).thenReturn(authorResponseDTO);
        objectMapper.writeValueAsString(authorResponseDTO);

        authorServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(request, times(1)).getReader();
        verify(objectMapper, times(1)).readValue(requestBody, AuthorRequestDTO.class);
        verify(mapper, times(1)).mapToEntity(authorRequestDTO);
        verify(authorService, times(1)).updateById(id, updateAuthor);
        verify(mapper, times(1)).mapToResponseDTO(updateAuthor);
        verify(objectMapper, times(1)).writeValueAsString(authorResponseDTO);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write(anyString());

        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    @Test
    void doPut_invalidId_test() throws Exception {
        String invalidId = "alksg4t";
        Author author = new Author("Author");

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn("{\"fullName\": \"Author\"}", null);

        authorServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("Invalid author ID");

        Assertions.assertThrows(IllegalArgumentException.class, () -> authorService.updateById(UUID.fromString(invalidId), author));
    }

    @Test
    void doPut_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        authorServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("ID must not be empty");
        verify(authorService, never()).updateById(any(), any());
    }

    @Test
    void doPut_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();
        String requestBody = "{\"fullName\": \"Author\"}";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn(requestBody, null);
        objectMapper.readValue(requestBody, AuthorRequestDTO.class);
        when(authorService.findById(notFoundId)).thenReturn(null);

        authorServlet.doPut(request, response);

        verify(request, times(1)).getParameter("id");
        verify(response, times(1)).setStatus(404);
        verify(printWriter, times(1)).write("Author not found by ID " + notFoundId);
        verify(authorService, never()).updateById(any(), any());
    }

    @Test
    void doDelete_validId_test() throws Exception {
        Author author = new Author(UUID.randomUUID(), "Author");

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(author.getId()));
        when(authorService.findById(author.getId())).thenReturn(author);
        when(authorService.delete(author.getId())).thenReturn(true);

        authorServlet.doDelete(request, response);

        verify(response, times(1)).getWriter();
        verify(request, times(1)).getParameter("id");
        verify(authorService, times(1)).delete(author.getId());
        verify(response, times(1)).setStatus(200);
        verify(printWriter, times(1)).write("Author was deleted successfully by ID " + author.getId());

        Assertions.assertTrue(authorService.delete(author.getId()));
    }

    @Test
    void doDelete_invalidId_test() throws Exception {
        String invalidId = "32095jwf";

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(invalidId);

        authorServlet.doDelete(request, response);

        verify(response, times(1)).setStatus(400);
        verify(printWriter, times(1)).write("Invalid author ID");

        Assertions.assertThrows(IllegalArgumentException.class, () -> authorService.delete(UUID.fromString(invalidId)));
    }

    @Test
    void doDelete_nullId_test() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(null);

        authorServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("ID must not be empty");
        verify(response, times(1)).setStatus(400);
        verify(authorService, never()).delete(any());

        Assertions.assertNull(request.getParameter("id"));
    }

    @Test
    void doDelete_notFoundById_test() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getParameter("id")).thenReturn(String.valueOf(notFoundId));
        when(authorService.findById(notFoundId)).thenReturn(null);

        authorServlet.doDelete(request, response);

        verify(printWriter, times(1)).write("Author not found by ID " + notFoundId);
        verify(response, times(1)).setStatus(404);
        verify(authorService, never()).delete(notFoundId);

        Assertions.assertNull(authorService.findById(notFoundId));
    }
}