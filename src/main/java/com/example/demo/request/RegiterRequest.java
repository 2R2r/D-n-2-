package com.example.demo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegiterRequest {

 @NotBlank(message = "FirstName cannot be blank")
 private String firstName;

 @NotBlank(message = "LastName cannot be blank")
 private String lastName;

@NotBlank(message = "AccountName cannot be blank")
   private String accountName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must true format")
    private String email;

    @Size(min = 5, max = 30, message = "Password length must be between 5 and 30 characters")
    private String password;

}
