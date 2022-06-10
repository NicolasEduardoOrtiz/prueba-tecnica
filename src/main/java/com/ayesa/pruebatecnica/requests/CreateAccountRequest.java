package com.ayesa.pruebatecnica.requests;

import com.ayesa.pruebatecnica.dtos.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateAccountRequest {

    private Account account;
}
