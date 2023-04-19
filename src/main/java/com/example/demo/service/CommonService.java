package com.example.demo.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T> {

    List<T> getAll();

    void save(T value);

    void deleteById(int id);

    Optional<T> findById(int id);


}
