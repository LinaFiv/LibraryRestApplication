package org.example.repositories.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.models.Book;
import org.example.repositories.BookRepository;
import org.example.repositories.mapper.BookResultSetMapper;
import org.example.repositories.mapper.BookResultSetMapperImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class BookRepositoryImpl implements BookRepository {

    private final String SELECT_ENTITY_BY_ID = "SELECT * FROM book WHERE id = '%s'";
    private final String SELECT_BOOKS = "SELECT * FROM book";
    private final String INSERT_ENTITY = "INSERT INTO book (id, title, year) VALUES ('%s', '%s', %d)";
    private final String UPDATE_ENTITY = "UPDATE book SET title = '%s', year = %d WHERE id = '%s'";
    private final String DELETE_ENTITY_BY_ID = "DELETE FROM book WHERE id = '%s'";

    private final BookResultSetMapper resultSetMapper;
    private final ConnectionManager connectionManager;

    public BookRepositoryImpl() {
        this.resultSetMapper = new BookResultSetMapperImpl();
        this.connectionManager = new ConnectionManagerImpl();
    }

    public BookRepositoryImpl(ConnectionManager connectionManager) {
        this.resultSetMapper = new BookResultSetMapperImpl();
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Book> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS);
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
    public Book findById(UUID id) {
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
    public Book save(Book book) {
        UUID id = UUID.randomUUID();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(INSERT_ENTITY, id, book.getTitle(), book.getYear()))) {
            preparedStatement.execute();
            return findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book updateById(UUID id, Book updateBook) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(String
                     .format(UPDATE_ENTITY, updateBook.getTitle(), updateBook.getYear(), id))) {
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
