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
import org.example.servlets.DTO.AuthorRequestDTO;
import org.example.servlets.DTO.AuthorResponseDTO;
import org.example.servlets.mapper.AuthorMapper;
import org.example.servlets.mapper.AuthorMapperImpl;
import org.example.utils.ServletUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(urlPatterns = "/author")
public class AuthorServlet extends HttpServlet {

    private AuthorService authorService;
    private AuthorMapper authorMapper;

    public AuthorServlet() {
    }

    public AuthorServlet(AuthorService authorService, AuthorMapper authorMapper) {
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
        String id = req.getParameter("id");

        if (id == null) {
            writer.write("ID must not be empty");
            resp.setStatus(400);
            return;
        }

        try {
            Author author = authorService.findById(UUID.fromString(id));

            if (author == null) {
                writer.write("Author not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            AuthorResponseDTO authorResponseDTO = authorMapper.mapToResponseDTO(author);
            ObjectMapper objectMapper = new ObjectMapper();
            String authorJson = objectMapper.writeValueAsString(authorResponseDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            writer.write(authorJson);
        } catch (IllegalArgumentException e) {
            writer.write("Invalid author ID");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = ServletUtil.getRequestBody(req.getReader());
        AuthorRequestDTO authorRequestDTO = objectMapper.readValue(requestBody, AuthorRequestDTO.class);

        Author author = authorMapper.mapToEntity(authorRequestDTO);
        Author createAuthor = authorService.create(author);
        AuthorResponseDTO authorResponseDTO = authorMapper.mapToResponseDTO(createAuthor);
        String authorJson = objectMapper.writeValueAsString(authorResponseDTO);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        writer.write(authorJson);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");

        if (id == null) {
            writer.write("ID must not be empty");
            resp.setStatus(400);
            return;
        }

        try {
            String requestBody = ServletUtil.getRequestBody(req.getReader());
            AuthorRequestDTO authorRequestDTO = objectMapper.readValue(requestBody, AuthorRequestDTO.class);

            if (authorService.findById(UUID.fromString(id)) == null) {
                writer.write("Author not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            Author author = authorMapper.mapToEntity(authorRequestDTO);
            Author updateAuthor = authorService.updateById(UUID.fromString(id), author);
            AuthorResponseDTO authorResponseDTO = authorMapper.mapToResponseDTO(updateAuthor);
            String authorJson = objectMapper.writeValueAsString(authorResponseDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            writer.write(authorJson);
        } catch (IllegalArgumentException e) {
            writer.write("Invalid author ID");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String id = req.getParameter("id");

        if (id == null) {
            writer.write("ID must not be empty");
            resp.setStatus(400);
            return;
        }

        try {
            if (authorService.findById(UUID.fromString(id)) == null) {
                writer.write("Author not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            authorService.delete(UUID.fromString(id));
            writer.write("Author was deleted successfully by ID " + id);
            resp.setStatus(200);
        } catch (IllegalArgumentException e) {
            writer.write("Invalid author ID");
            resp.setStatus(400);
        }
    }
}
