package com.example.demo.repository;

import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct,Integer> {

    List<OrderProduct> findByOrders_Id(int id);

    @Query("SELECT op.orders.id, SUM(op.quantity) as total_quantity "
            + "FROM OrderProduct op "
            + "INNER JOIN op.product p "
            + "WHERE op.orders.id = :orderId "
            + "GROUP BY op.orders.id")
    int findTotalQuantityByOrderId(@Param("orderId") int orderId);


}
