package org.example.repositories.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.models.Person;
import org.example.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
class PersonRepositoryImplTest {

    PersonRepository personRepository;

    @Container
    static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("library_db")
                    .withUsername("postgres")
                    .withInitScript("init.sql")
                    .withPassword("postgres");

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ConnectionManager connectionManager = new ConnectionManagerImpl(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        personRepository = new PersonRepositoryImpl(connectionManager);
    }

    @Test
    void findAll() {
        List<Person> people = new ArrayList<>(List.of(
                personRepository.save(new Person("Person1", 1990)),
                personRepository.save(new Person("Person2", 1990))));

        List<Person> foundAll = personRepository.findAll();

        Assertions.assertEquals(2, foundAll.size());
        Assertions.assertEquals(people, foundAll);
    }

    @Test
    void findById() {
        Person person = personRepository.save(new Person("Person", 1990));
        Person foundPerson = personRepository.findById(person.getId());

        Assertions.assertEquals(person, foundPerson);
    }

    @Test
    void save() {
        Person createPerson = personRepository.save(new Person("Person", 1990));

        Assertions.assertNotNull(createPerson.getId());
        Assertions.assertEquals("Person", createPerson.getFullName());
        Assertions.assertEquals(1990, createPerson.getYearOfBirth());
    }

    @Test
    void updateById() {
        Person person = personRepository.save(new Person("Person", 1990));
        Person personNew = new Person("New Name", 1995);
        Person updatePerson = personRepository.updateById(person.getId(), personNew);

        Assertions.assertEquals(person.getId(), updatePerson.getId());
        Assertions.assertEquals("New Name", updatePerson.getFullName());
        Assertions.assertEquals(1995, updatePerson.getYearOfBirth());
    }

    @Test
    void deleteById() {
        Person person = personRepository.save(new Person("Person", 1990));
        boolean delete = personRepository.deleteById(person.getId());

        Assertions.assertTrue(delete);
    }
}