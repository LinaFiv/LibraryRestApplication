package org.example.repositories;

import java.util.List;
import java.util.UUID;

public interface Repository<T, K> {
    List<T> findAll();

    T findById(K id);

    T save(T t);

    T updateById(UUID id, T t);

    boolean deleteById(K id);
}
