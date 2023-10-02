package org.example.repositories.mapper;

import org.example.models.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorResultSetMapperImplTest {

    @Mock
    ResultSet resultSet;

    @Test
    void map() {
        String id = UUID.randomUUID().toString();
        try {
            when(resultSet.getString("id")).thenReturn(id);
            when(resultSet.getString("fullname")).thenReturn("Author");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        AuthorResultSetMapper mapper = new AuthorResultSetMapperImpl();
        Author author = mapper.map(resultSet);

        Assertions.assertNotNull(author.getId());
        Assertions.assertEquals("Author", author.getFullName());
    }
}