package org.example.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Author;
import org.example.services.AuthorService;
import org.example.servlets.DTO.AuthorResponseDTO;
import org.example.servlets.mapper.AuthorMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorsServletTest {

    @Mock
    AuthorService authorService;
    @Mock
    AuthorMapper mapper;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    PrintWriter printWriter;

    AuthorsServlet authorsServlet;

    @BeforeEach
    void setUp() {
        authorsServlet = new AuthorsServlet(authorService, mapper);
    }

    @Test
    void doGetTest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        List<Author> authors = getAuthorList(List.of("Author1", "Author2", "Author3"));
        List<AuthorResponseDTO> authorResponseDTOS = getAuthorResponseDTOList(authors);

        when(response.getWriter()).thenReturn(printWriter);
        when(authorService.findAll()).thenReturn(authors);
        when(mapper.mapToList(authors)).thenReturn(authorResponseDTOS);
        objectMapper.writeValueAsString(authorResponseDTOS);

        authorsServlet.doGet(request, response);

        verify(authorService, times(1)).findAll();
        verify(mapper, times(1)).mapToList(authors);
        verify(objectMapper, times(1)).writeValueAsString(authorResponseDTOS);
        verify(response, times(1)).setStatus(200);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).println(anyString());

        Assertions.assertNotNull(objectMapper);
        Assertions.assertNotNull(printWriter);
    }

    private List<Author> getAuthorList(List<String> names) {
        return names.stream().map(e -> new Author(UUID.randomUUID(), e)).toList();
    }

    private List<AuthorResponseDTO> getAuthorResponseDTOList(List<Author> authorList) {
        return authorList.stream().map(e -> new AuthorResponseDTO(e.getId(), e.getFullName())).toList();
    }
}

//    @Test
//    void doPost() {
//        try {
//            HttpServletRequest request = mock(HttpServletRequest.class);
//            HttpServletResponse response =mock(HttpServletResponse.class);
//            PrintWriter writer = mock(PrintWriter.class);
//            BufferedReader reader = mock(BufferedReader.class);
//            when(request.getReader()).thenReturn(reader);
//            when(reader.readLine()).thenReturn("{  owner:'dasha' }");
//            IncomingDtoSimple simpleIn = new IncomingDtoSimple();
//            simpleIn.setOwner(NAME);
//            servlet.doPost(request, response);