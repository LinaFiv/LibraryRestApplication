package org.example.servlets.mapper;

import org.example.models.Book;
import org.example.servlets.DTO.BookRequestDTO;
import org.example.servlets.DTO.BookResponseDTO;

import java.util.List;
import java.util.UUID;

public class BookMapperImpl implements BookMapper {
    @Override
    public Book mapToEntity(BookRequestDTO bookRequestDTO) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle(bookRequestDTO.getTitle());
        book.setYear(bookRequestDTO.getYear());
        return book;
    }

    @Override
    public BookResponseDTO mapToResponseDto(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setYear(book.getYear());
        return dto;
    }

    @Override
    public List<BookResponseDTO> mapToList(List<Book> bookList) {
        return bookList.stream().map(this::mapToResponseDto).toList();
    }
}