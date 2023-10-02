package org.example.repositories.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.models.Author;
import org.example.repositories.AuthorRepository;
import org.example.repositories.mapper.AuthorResultSetMapper;
import org.example.repositories.mapper.AuthorResultSetMapperImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthorRepositoryImpl implements AuthorRepository {

    private final String SELECT_ENTITY_BY_ID = "SELECT * FROM author WHERE id = '%s'";
    private final String SELECT_AUTHOR = "SELECT * FROM author";
    private final String INSERT_ENTITY = "INSERT INTO author (id, fullname) VALUES ('%s', '%s')";
    private final String UPDATE_ENTITY = "UPDATE author SET fullname = '%s' WHERE id = '%s'";
    private final String DELETE_ENTITY_BY_ID = "DELETE FROM author WHERE id = '%s'";

    AuthorResultSetMapper resultSetMapper;
    ConnectionManager connectionManager;


    public AuthorRepositoryImpl() {
        this.resultSetMapper = new AuthorResultSetMapperImpl();
        this.connectionManager = new ConnectionManagerImpl();
    }

    public AuthorRepositoryImpl(ConnectionManager connectionManager) {
        this.resultSetMapper = new AuthorResultSetMapperImpl();
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Author> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_AUTHOR);
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
    public Author findById(UUID id) {
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
    public Author save(Author author) {
        UUID id = UUID.randomUUID();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(INSERT_ENTITY, id, author.getFullName()))) {
            preparedStatement.execute();
            return findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Author updateById(UUID id, Author updateAuthor) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(UPDATE_ENTITY, updateAuthor.getFullName(), id))) {
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
