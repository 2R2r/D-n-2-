package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private int id;

    private String name;

    private String image;

    private BigDecimal price =  new BigDecimal("0");

    private String description;

    private int quantity;

    private int category_id;

    private List<Review> review;

    private int user_id;

    private LocalDate created_at;

    private BigDecimal totalPrice =  new BigDecimal("0");

    private Product product;

}
