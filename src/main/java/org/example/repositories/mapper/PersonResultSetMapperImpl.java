package org.example.repositories.mapper;

import org.example.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PersonResultSetMapperImpl implements PersonResultSetMapper {
    @Override
    public Person map(ResultSet resultSet) {
        Person person = new Person();
        try {
            person.setId(UUID.fromString(resultSet.getString("id")));
            person.setFullName(resultSet.getString("fullname"));
            person.setYearOfBirth(resultSet.getInt("year_of_birth"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }
}
