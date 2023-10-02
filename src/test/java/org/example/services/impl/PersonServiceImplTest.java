package org.example.services.impl;

import org.example.models.Person;
import org.example.repositories.PersonRepository;
import org.example.services.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    PersonRepository personRepository;
    PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonServiceImpl(personRepository);
    }

    @Test
    void findByIdTest() {
        Person person = new Person("Person", 1900);
        UUID id = UUID.randomUUID();
        person.setId(id);

        when(personRepository.findById(id)).thenReturn(person);

        Person personById = personService.findById(id);

        verify(personRepository, times(1)).findById(id);
        assertEquals(person, personById);
    }

    @Test
    void findAllTest() {
        Person person1 = new Person("Person1", 1900);
        UUID id1 = UUID.randomUUID();
        person1.setId(id1);

        Person person2 = new Person("Person2", 1900);
        UUID id2 = UUID.randomUUID();
        person2.setId(id2);

        Person person3 = new Person("Person3", 1900);
        UUID id3 = UUID.randomUUID();
        person3.setId(id3);

        List<Person> people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);

        when(personRepository.findAll()).thenReturn(people);

        List<Person> personList = personService.findAll();

        verify(personRepository, times(1)).findAll();
        Assertions.assertEquals(3, personList.size());
    }

    @Test
    void createTest() {
        Person personCreate = new Person("Person Create", 1900);
        UUID id = UUID.randomUUID();

        when(personRepository.save(personCreate)).then((Answer<Person>) invocationOnMock -> {
            Person person = invocationOnMock.getArgument(0);
            person.setId(id);
            return person;
        });

        Person person = personService.create(personCreate);

        verify(personRepository, times(1)).save(personCreate);
        Assertions.assertNotNull(personCreate.getId());
        Assertions.assertEquals(id, personCreate.getId());
        Assertions.assertEquals(person, personCreate);
    }

    @Test
    void updateByIdTest() {
        Person person = new Person("Person", 1900);
        UUID id = UUID.randomUUID();
        person.setId(id);

        when(personRepository.updateById(id, person)).then((Answer<Person>) invocationOnMock -> {
            Person personMockArgument = invocationOnMock.getArgument(1);
            personMockArgument.setFullName("Update Person");
            personMockArgument.setYearOfBirth(2000);
            return personMockArgument;
        });

        Person updateById = personService.updateById(id, person);

        verify(personRepository, times(1)).updateById(id, person);
        assertEquals(person, updateById);
        assertEquals("Update Person", updateById.getFullName());
        assertEquals(2000, updateById.getYearOfBirth());
    }

    @Test
    void deleteTest() {
        Person person = new Person("Person", 1900);
        UUID id = UUID.randomUUID();
        person.setId(id);

        when(personRepository.deleteById(id)).thenReturn(true);

        boolean delete = personService.delete(id);

        verify(personRepository, times(1)).deleteById(id);
        assertTrue(delete);
    }
}