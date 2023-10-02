package org.example.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ResultSetMapper<E> {
    E map(ResultSet resultSet);

    default List<E> mapToList(ResultSet resultSet) {
        List<E> entityList = new ArrayList<>();
        try {
            do {
                entityList.add(map(resultSet));
            }
            while (resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entityList;
    }
}
