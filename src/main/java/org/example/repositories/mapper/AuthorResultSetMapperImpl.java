package org.example.repositories.mapper;

import org.example.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorResultSetMapperImpl implements AuthorResultSetMapper {
    @Override
    public Author map(ResultSet resultSet) {
        Author author = new Author();
        try {
            author.setId(UUID.fromString(resultSet.getString("id")));
            author.setFullName(resultSet.getString("fullname"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }
}
