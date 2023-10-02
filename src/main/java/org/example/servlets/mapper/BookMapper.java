package org.example.servlets.mapper;

import org.example.models.Book;
import org.example.servlets.DTO.BookRequestDTO;
import org.example.servlets.DTO.BookResponseDTO;

import java.util.List;

public interface BookMapper {
    Book mapToEntity(BookRequestDTO bookRequestDTO);

    BookResponseDTO mapToResponseDto(Book book);

    List<BookResponseDTO> mapToList(List<Book> bookList);
}
