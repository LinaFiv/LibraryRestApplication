package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Author;
import org.example.services.AuthorService;
import org.example.services.impl.AuthorServiceImpl;
import org.example.servlets.DTO.AuthorResponseDTO;
import org.example.servlets.mapper.AuthorMapper;
import org.example.servlets.mapper.AuthorMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/authors")
public class AuthorsServlet extends HttpServlet {

    private AuthorService authorService;
    private AuthorMapper authorMapper;

    public AuthorsServlet() {
    }

    public AuthorsServlet(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @Override
    public void init() throws ServletException {
        authorService = new AuthorServiceImpl();
        authorMapper = new AuthorMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        List<Author> authors = authorService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        List<AuthorResponseDTO> dtoList = authorMapper.mapToList(authors);
        String authorsJson = objectMapper.writeValueAsString(dtoList);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        writer.println(authorsJson);
    }
}
