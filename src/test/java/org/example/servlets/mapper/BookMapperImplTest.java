package org.example.servlets.mapper;

import org.example.models.Book;
import org.example.servlets.DTO.BookRequestDTO;
import org.example.servlets.DTO.BookResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class BookMapperImplTest {

    BookMapper bookMapper = new BookMapperImpl();

    @Test
    void mapToEntity() {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("Book");
        bookRequestDTO.setYear(1900);

        Book book = bookMapper.mapToEntity(bookRequestDTO);

        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals(bookRequestDTO.getTitle(), book.getTitle());
        Assertions.assertEquals(bookRequestDTO.getYear(), book.getYear());
    }

    @Test
    void mapToResponseDto() {
        Book book = new Book();
        UUID id = UUID.randomUUID();
        book.setId(id);
        book.setTitle("Book");
        book.setYear(1900);

        BookResponseDTO bookResponseDTO = bookMapper.mapToResponseDto(book);

        Assertions.assertEquals(id, bookResponseDTO.getId());
        Assertions.assertEquals(book.getTitle(), bookResponseDTO.getTitle());
        Assertions.assertEquals(book.getYear(), bookResponseDTO.getYear());
    }
}