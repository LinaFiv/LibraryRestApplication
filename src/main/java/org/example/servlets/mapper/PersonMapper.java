package org.example.servlets.mapper;

import org.example.models.Person;
import org.example.servlets.DTO.PersonRequestDTO;
import org.example.servlets.DTO.PersonResponseDTO;

import java.util.List;

public interface PersonMapper {

    Person mapToEntity(PersonRequestDTO personRequestDTO);

    PersonResponseDTO mapToResponseDTO(Person person);

    List<PersonResponseDTO> mapToList(List<Person> personList);
}
