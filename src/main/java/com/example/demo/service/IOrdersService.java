package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface IOrdersService {

    List<Orders> findByUser(User user);

    List<Orders> findByStatus(Status status);

    Orders createOrder( User user, Set product);

     void updateOrderStatus(int id, Status status);
}
