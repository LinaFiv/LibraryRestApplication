package org.example.servlets.mapper;

import org.example.models.Person;
import org.example.servlets.DTO.PersonRequestDTO;
import org.example.servlets.DTO.PersonResponseDTO;

import java.util.List;
import java.util.UUID;

public class PersonMapperImpl implements PersonMapper {
    @Override
    public Person mapToEntity(PersonRequestDTO personRequestDTO) {
        Person person = new Person();
        person.setId(UUID.randomUUID());
        person.setFullName(personRequestDTO.getFullName());
        person.setYearOfBirth(personRequestDTO.getYearOfBirth());
        return person;
    }

    @Override
    public PersonResponseDTO mapToResponseDTO(Person person) {
        PersonResponseDTO dto = new PersonResponseDTO();
        dto.setId(person.getId());
        dto.setFullName(person.getFullName());
        dto.setYearOfBirth(person.getYearOfBirth());
        return dto;
    }

    @Override
    public List<PersonResponseDTO> mapToList(List<Person> personList) {
        return personList.stream().map(this::mapToResponseDTO).toList();
    }
}
