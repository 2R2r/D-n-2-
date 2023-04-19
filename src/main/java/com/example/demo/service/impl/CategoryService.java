package com.example.demo.service.impl;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryService implements CommonService<Category> {
  @Autowired
  private CategoryRepository repository;

    @Override
    public List<Category> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(Category value) {
        repository.save(value);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }
    @Override
    public Optional<Category> findById(int id) {
      return   repository.findById(id);
    }
}
