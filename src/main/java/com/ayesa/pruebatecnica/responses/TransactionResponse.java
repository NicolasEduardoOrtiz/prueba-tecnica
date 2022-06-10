package com.ayesa.pruebatecnica.responses;

import com.ayesa.pruebatecnica.dtos.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponse {

    private Account account;
    private List<String> violations = new ArrayList<>();
}
