package org.example.repositories.mapper;

import org.example.models.Book;
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
class BookResultSetMapperImplTest {

    @Mock
    ResultSet resultSet;

    @Test
    void map() {
        String id = UUID.randomUUID().toString();
        try {
            when(resultSet.getString("id")).thenReturn(id);
            when(resultSet.getString("title")).thenReturn("Book");
            when(resultSet.getInt("year")).thenReturn(1900);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        BookResultSetMapper mapper = new BookResultSetMapperImpl();
        Book book = mapper.map(resultSet);

        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals("Book", book.getTitle());
        Assertions.assertEquals(1900, book.getYear());
    }
}