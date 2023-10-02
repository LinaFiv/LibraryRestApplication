package org.example.services.impl;

import org.example.models.Person;
import org.example.repositories.PersonRepository;
import org.example.repositories.impl.PersonRepositoryImpl;
import org.example.services.PersonService;

import java.util.List;
import java.util.UUID;

public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl() {
        this.personRepository = new PersonRepositoryImpl();
    }

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person findById(UUID id) {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Person create(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person updateById(UUID id, Person updatePerson) {
        return personRepository.updateById(id, updatePerson);
    }

    @Override
    public boolean delete(UUID id) {
        return personRepository.deleteById(id);
    }
}
