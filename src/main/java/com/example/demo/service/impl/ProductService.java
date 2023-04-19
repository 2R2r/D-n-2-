package com.example.demo.service.impl;

import com.example.demo.dto.KhachHangDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.Role;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CommonService;
import com.example.demo.service.IProductService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

@Service

public class ProductService implements CommonService<Product>, IProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private UsersService usersService;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(Product value) {
        LocalDateTime currentTime = LocalDateTime.now();
        value.setCreated_at(LocalDate.from(currentTime));
        repository.save(value);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    // Get all products by category_id
    public List<Product> findAllProductById(int id) {
        return repository.findByCategoryId(id);
    }

    public List<Product> findAllByOrderByPriceAsc() {
        return repository.findAllByOrderByPriceAsc();
    }

    public List<Product> findAllByOrderByPriceDesc() {
        return repository.findAllByOrderByPriceDesc();
    }

    public List<Product> findAllByNameContaining(String name) {
        return repository.findAllByNameContaining(name);
    }

    public List<Product> findTop4ByCategoryId(int categoryId) {
        return repository.findTop4ByCategoryId(categoryId);
    }


    @Override
    public List<Product> getProductsByIds(int[] productIds) {
        return repository.findAllById(Arrays.stream(productIds).boxed().collect(Collectors.toList()));
    }
    public ProductDTO getProductDTO(int id) {
        Optional<Product> optionalProduct = findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .image(product.getImage())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .quantity(1)
                    .category_id(product.getCategory().getId())
                    .review(product.getReview())
                    .user_id(product.getCreated_by().getId())
                    .created_at(product.getCreated_at())
                    .totalPrice(product.getPrice())
                    .product(product)
                    .build();
        }
        return null;
    }

    public BigDecimal getTotalAllPrice(Collection<ProductDTO> list){
        BigDecimal total = new BigDecimal("0");
        for (ProductDTO product : list) {
            total = total.add(product.getTotalPrice());
        }
        return total;
    }



}
