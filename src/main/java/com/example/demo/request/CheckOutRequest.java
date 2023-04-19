package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckOutRequest {

    @NotBlank(message = "First Name cannot be blank")
    String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    String lastName;

    @NotBlank(message = "Name On Card cannot be blank")
    String nameOnCard;

    @NotBlank(message = "Card Number cannot be blank")
    String cardNumber;

    @NotBlank(message = "Expiration cannot be blank")
    String expiration;

    @NotBlank(message = "Cvv cannot be blank")
    String cvv;
}
