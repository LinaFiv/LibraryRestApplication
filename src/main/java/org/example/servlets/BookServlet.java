package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Book;
import org.example.services.BookService;
import org.example.services.impl.BookServiceImpl;
import org.example.servlets.DTO.BookRequestDTO;
import org.example.servlets.DTO.BookResponseDTO;
import org.example.servlets.mapper.BookMapper;
import org.example.servlets.mapper.BookMapperImpl;
import org.example.utils.ServletUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(urlPatterns = "/book")
public class BookServlet extends HttpServlet {
    private BookService bookService;
    private BookMapper bookMapper;

    public BookServlet() {
    }

    public BookServlet(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @Override
    public void init() throws ServletException {
        bookService = new BookServiceImpl();
        bookMapper = new BookMapperImpl();
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
            Book book = bookService.findById(UUID.fromString(id));

            if (book == null) {
                writer.write("Book not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            BookResponseDTO bookResponseDTO = bookMapper.mapToResponseDto(book);
            ObjectMapper objectMapper = new ObjectMapper();
            String bookJson = objectMapper.writeValueAsString(bookResponseDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            writer.write(bookJson);
        } catch (IllegalArgumentException e) {
            writer.write("Invalid book ID");
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = ServletUtil.getRequestBody(req.getReader());
        BookRequestDTO bookRequestDTO = objectMapper.readValue(requestBody, BookRequestDTO.class);

        Book book = bookMapper.mapToEntity(bookRequestDTO);
        Book createBook = bookService.create(book);
        BookResponseDTO bookResponseDTO = bookMapper.mapToResponseDto(createBook);
        String bookJson = objectMapper.writeValueAsString(bookResponseDTO);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        writer.write(bookJson);
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
            BookRequestDTO bookRequestDTO = objectMapper.readValue(requestBody, BookRequestDTO.class);

            if (bookService.findById(UUID.fromString(id)) == null) {
                writer.write("Book not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            Book book = bookMapper.mapToEntity(bookRequestDTO);
            Book updateBook = bookService.updateById(UUID.fromString(id), book);
            BookResponseDTO bookResponseDTO = bookMapper.mapToResponseDto(updateBook);
            String bookJson = objectMapper.writeValueAsString(bookResponseDTO);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            writer.write(bookJson);
        } catch (IllegalArgumentException e) {
            writer.write("Invalid book ID");
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
            if (bookService.findById(UUID.fromString(id)) == null) {
                writer.write("Book not found by ID " + id);
                resp.setStatus(404);
                return;
            }

            bookService.delete(UUID.fromString(id));
            writer.write("Book was deleted successfully by ID " + id);
            resp.setStatus(200);
        } catch (IllegalArgumentException e) {
            writer.write("Invalid book ID");
            resp.setStatus(400);
        }
    }
}
