package com.example.demo.repository;

import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    List<Orders> findByUser(User user);

    List<Orders> findByStatus(Status status);

    @Query("SELECT op FROM Orders op WHERE op.status = :status")
    List<Orders> findByOrderByStatus(@Param("status") Status status);

    @Query("SELECT op FROM Orders op WHERE op.user.lastName LIKE %:lastName%")
    List<Orders> findByUserNameContaining(@Param("lastName") String lastName);

}
