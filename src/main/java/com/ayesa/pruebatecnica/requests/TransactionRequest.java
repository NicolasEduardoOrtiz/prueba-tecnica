package com.ayesa.pruebatecnica.requests;

import com.ayesa.pruebatecnica.dtos.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {

    private Transaction transaction;
}
