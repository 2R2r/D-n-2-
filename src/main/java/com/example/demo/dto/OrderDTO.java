package com.example.demo.dto;

import com.example.demo.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private int userId;

    private int[] productIds;

    private LocalDate orderDate;

    private Status status;

}
