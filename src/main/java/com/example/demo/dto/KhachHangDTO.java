package com.example.demo.dto;

import com.example.demo.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangDTO {

    private String firstName;

    private String lastName;

    private LocalDate orderDate;
}
