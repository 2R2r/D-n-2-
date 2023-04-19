package com.example.demo.repository;

import com.example.demo.entity.Orders;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    Optional<User> findByAccountName(String accountName);

    Optional<User> findByEmail(String emmail);
}
