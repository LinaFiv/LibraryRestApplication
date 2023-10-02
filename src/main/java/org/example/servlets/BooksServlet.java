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
import org.example.servlets.DTO.BookResponseDTO;
import org.example.servlets.mapper.BookMapper;
import org.example.servlets.mapper.BookMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/books")
public class BooksServlet extends HttpServlet {
    private BookService bookService;
    private BookMapper bookMapper;

    public BooksServlet() {
    }

    public BooksServlet(BookService bookService, BookMapper bookMapper) {
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
        List<Book> books = bookService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        List<BookResponseDTO> dtoList = bookMapper.mapToList(books);
        String booksJson = objectMapper.writeValueAsString(dtoList);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        writer.println(booksJson);
    }
}
