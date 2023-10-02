package org.example.repositories.mapper;

import org.example.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookResultSetMapperImpl implements BookResultSetMapper {

    @Override
    public Book map(ResultSet resultSet) {
        Book book = new Book();

        try {
            book.setId(UUID.fromString(resultSet.getString("id")));
            book.setTitle(resultSet.getString("title"));
            book.setYear(resultSet.getInt("year"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
}
