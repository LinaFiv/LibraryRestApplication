package org.example.servlets.mapper;

import org.example.models.Author;
import org.example.servlets.DTO.AuthorRequestDTO;
import org.example.servlets.DTO.AuthorResponseDTO;

import java.util.List;

public interface AuthorMapper {
    Author mapToEntity(AuthorRequestDTO authorRequestDTO);

    AuthorResponseDTO mapToResponseDTO(Author author);

    List<AuthorResponseDTO> mapToList(List<Author> authorList);
}
