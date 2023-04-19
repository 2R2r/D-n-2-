package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryId(int categoryId);

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByPriceDesc();

    List<Product> findAllByNameContaining(String name);

    List<Product> findTop4ByCategoryId(int  categoryId);

}
