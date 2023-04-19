package com.example.demo.service.impl;

import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderProductRepository;
import com.example.demo.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderProductService implements CommonService<OrderProduct> {
    @Autowired
    private OrderProductRepository repository;
    @Override
    public List<OrderProduct> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(OrderProduct value) {
        repository.save(value);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<OrderProduct> findById(int id) {
        return repository.findById(id);
    }

    public BigDecimal totalAllProductInOrder(Set<Product> set){
        BigDecimal total = new BigDecimal("0");
        for (Product p : set) {
            long quantity = (long) p.getQuantity();
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
            return total;
        }

    public BigDecimal totalAllProductInOrder(List<OrderProduct> list){
        BigDecimal total = new BigDecimal("0");

        for (int i = 0; i < list.size(); i++) {
            long quantity = (long) list.get(i).getQuantity();
            total = total.add(list.get(i).getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
        return total;
    }

    public List<OrderProduct> findByOrders_Id(int id){
        return repository.findByOrders_Id(id);
    }

    int findTotalQuantityByOrderId( int orderId){
        return repository.findTotalQuantityByOrderId(orderId);
    }



}
