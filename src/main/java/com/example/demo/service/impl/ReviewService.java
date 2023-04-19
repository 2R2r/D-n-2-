package com.example.demo.service.impl;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class ReviewService implements CommonService<Review> {
  @Autowired
  private ReviewRepository repository;

    @Override
    public List<Review> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(Review value) {
        repository.save(value);
    }


    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Review> findById(int id) {
      return   repository.findById(id);
    }

    public List<Review> findByProductId(int productId) {
      return repository.findByProductId(productId);
    }
}
