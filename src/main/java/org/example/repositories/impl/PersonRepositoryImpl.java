package org.example.repositories.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.models.Person;
import org.example.repositories.PersonRepository;
import org.example.repositories.mapper.PersonResultSetMapper;
import org.example.repositories.mapper.PersonResultSetMapperImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PersonRepositoryImpl implements PersonRepository {

    private final String SELECT_ENTITY_BY_ID = "SELECT * FROM person WHERE id = '%s'";
    private final String SELECT_PEOPLE = "SELECT * FROM person";
    private final String INSERT_ENTITY = "INSERT INTO person (id, fullname, year_of_birth) VALUES ('%s', '%s', %d)";
    private final String UPDATE_ENTITY = "UPDATE person SET fullname = '%s', year_of_birth = %d WHERE id = '%s'";
    private final String DELETE_ENTITY_BY_ID = "DELETE FROM person WHERE id = '%s'";

    private final PersonResultSetMapper resultSetMapper;
    private final ConnectionManager connectionManager;

    public PersonRepositoryImpl() {
        this.resultSetMapper = new PersonResultSetMapperImpl();
        this.connectionManager = new ConnectionManagerImpl();
    }

    public PersonRepositoryImpl(ConnectionManager connectionManager) {
        this.resultSetMapper = new PersonResultSetMapperImpl();
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Person> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PEOPLE);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.next()) {
                return null;
            }
            return resultSetMapper.mapToList(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person findById(UUID id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(SELECT_ENTITY_BY_ID, id.toString()));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (!resultSet.next()) {
                return null;
            }
            return resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person save(Person person) {
        UUID id = UUID.randomUUID();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(INSERT_ENTITY, id, person.getFullName(), person.getYearOfBirth()))) {
            preparedStatement.execute();
            return findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person updateById(UUID id, Person updatePerson) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(UPDATE_ENTITY, updatePerson.getFullName(), updatePerson.getYearOfBirth(), id))) {
            preparedStatement.execute();
            return findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(DELETE_ENTITY_BY_ID, id))) {
            preparedStatement.execute();
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
