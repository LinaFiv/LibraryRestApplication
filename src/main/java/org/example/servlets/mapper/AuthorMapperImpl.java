package org.example.servlets.mapper;

import org.example.models.Author;
import org.example.servlets.DTO.AuthorRequestDTO;
import org.example.servlets.DTO.AuthorResponseDTO;

import java.util.List;
import java.util.UUID;

public class AuthorMapperImpl implements AuthorMapper {
    @Override
    public Author mapToEntity(AuthorRequestDTO authorRequestDTO) {
        Author author = new Author();
        author.setId(UUID.randomUUID());
        author.setFullName(authorRequestDTO.getFullName());
        return author;
    }

    @Override
    public AuthorResponseDTO mapToResponseDTO(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setFullName(author.getFullName());
        return dto;
    }

    @Override
    public List<AuthorResponseDTO> mapToList(List<Author> authorList) {
        return authorList.stream().map(this::mapToResponseDTO).toList();
    }
}
