package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String accountName;

    private String email;

    private Role role;

}
