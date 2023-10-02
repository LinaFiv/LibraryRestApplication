package org.example.servlets.mapper;

import org.example.models.Person;
import org.example.servlets.DTO.PersonRequestDTO;
import org.example.servlets.DTO.PersonResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class PersonMapperImplTest {

    PersonMapper personMapper = new PersonMapperImpl();

    @Test
    void mapToEntity() {
        PersonRequestDTO personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setFullName("Person");
        personRequestDTO.setYearOfBirth(1900);

        Person person = personMapper.mapToEntity(personRequestDTO);

        Assertions.assertNotNull(person.getId());
        Assertions.assertEquals(personRequestDTO.getFullName(), person.getFullName());
        Assertions.assertEquals(personRequestDTO.getYearOfBirth(), person.getYearOfBirth());
    }

    @Test
    void mapToResponseDTO() {
        Person person = new Person();
        UUID id = UUID.randomUUID();
        person.setId(id);
        person.setFullName("Person");
        person.setYearOfBirth(1900);

        PersonResponseDTO personResponseDTO = personMapper.mapToResponseDTO(person);

        Assertions.assertEquals(id, personResponseDTO.getId());
        Assertions.assertEquals(person.getFullName(), personResponseDTO.getFullName());
        Assertions.assertEquals(person.getYearOfBirth(), personResponseDTO.getYearOfBirth());
    }
}