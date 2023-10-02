package org.example.repositories.mapper;

import org.example.models.Person;
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
class PersonResultSetMapperImplTest {

    @Mock
    ResultSet resultSet;

    @Test
    void map() {
        String id = UUID.randomUUID().toString();
        try {
            when(resultSet.getString("id")).thenReturn(id);
            when(resultSet.getString("fullname")).thenReturn("Person");
            when(resultSet.getInt("year_of_birth")).thenReturn(1900);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PersonResultSetMapperImpl mapper = new PersonResultSetMapperImpl();
        Person person = mapper.map(resultSet);

        Assertions.assertNotNull(person.getId());
        Assertions.assertEquals("Person", person.getFullName());
        Assertions.assertEquals(1900, person.getYearOfBirth());
    }
}