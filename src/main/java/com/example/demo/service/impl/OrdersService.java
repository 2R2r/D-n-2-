package com.example.demo.service.impl;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.service.CommonService;
import com.example.demo.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service

public class OrdersService implements CommonService<Orders>, IOrdersService {
  @Autowired
  private OrdersRepository repository;

    @Autowired
    private ProductService productService ;

    @Autowired
    private CategoryService categoryService ;

    @Autowired
    private UsersService usersService ;

    @Override
    public List<Orders> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(Orders value) {
        repository.save(value);
    }


    @Override
    public void deleteById(int id) {
        repository.deleteById(id);

    }

    @Override
    public Optional<Orders> findById(int id) {
      return   repository.findById(id);
    }

    @Override
    public List<Orders> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<Orders> findByStatus(Status status) {
        return repository.findByStatus(status);
    }

    public Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setImage(productDTO.getImage());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(categoryService.findById(productDTO.getCategory_id()).get());
        product.setReview(productDTO.getReview());
        product.setCreated_by(usersService.findById(productDTO.getUser_id()).get());
        product.setCreated_at(productDTO.getCreated_at());
        return product;
    }

    public Set<Product> convertToProduct(Set<ProductDTO> productsDTO) {
        Set<Product> products = new HashSet<>();
        for (ProductDTO productDTO : productsDTO) {
            Product product = convertToProduct(productDTO);
            products.add(product);
        }
        return products;
    }

    @Override
    public Orders createOrder(User user, Set productDTO2) {
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(Status.InProgress);
        repository.save(order);
        return order;
    }

    @Override
    public void updateOrderStatus(int id, Status status) {
        Optional<Orders> orders = repository.findById(id);
        if(orders.isPresent()) {
            orders.get().setStatus(status);
            repository.save(orders.get());
        }
    }

    public List<Orders> findByOrderByStatus(Status status){
        return repository.findByOrderByStatus(status);
    }

    public List<Orders> findByUserNameContaining(String name){
        return repository.findByUserNameContaining(name);
    }

}
