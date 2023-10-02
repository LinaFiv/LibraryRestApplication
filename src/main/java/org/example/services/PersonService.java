package org.example.services;

import org.example.models.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    Person findById(UUID id);

    List<Person> findAll();

    Person create(Person person);

    Person updateById(UUID id, Person updatePerson);

    boolean delete(UUID id);
}
