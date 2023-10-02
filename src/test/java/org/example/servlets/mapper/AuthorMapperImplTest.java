package org.example.servlets.mapper;

import org.example.models.Author;
import org.example.servlets.DTO.AuthorRequestDTO;
import org.example.servlets.DTO.AuthorResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class AuthorMapperImplTest {
    AuthorMapper authorMapper = new AuthorMapperImpl();

    @Test
    void mapToEntityTest() {
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setFullName("Author");

        Author author = authorMapper.mapToEntity(authorRequestDTO);

        Assertions.assertNotNull(author.getId());
        Assertions.assertEquals(authorRequestDTO.getFullName(), author.getFullName());
    }

    @Test
    void mapToResponseDTOTest() {
        Author author = new Author();
        UUID id = UUID.randomUUID();
        author.setId(id);
        author.setFullName("Author");

        AuthorResponseDTO authorResponseDTO = authorMapper.mapToResponseDTO(author);

        Assertions.assertEquals(author.getFullName(), authorResponseDTO.getFullName());
        Assertions.assertEquals(id, authorResponseDTO.getId());
    }
}