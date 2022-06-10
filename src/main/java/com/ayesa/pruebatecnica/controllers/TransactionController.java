package com.ayesa.pruebatecnica.controllers;

import com.ayesa.pruebatecnica.requests.CreateAccountRequest;
import com.ayesa.pruebatecnica.requests.TransactionRequest;
import com.ayesa.pruebatecnica.responses.TransactionResponse;
import com.ayesa.pruebatecnica.services.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/account")
    public TransactionResponse createAccount(@RequestBody CreateAccountRequest createAccountRequest) {

        return transactionService.createAccount(createAccountRequest);
    }

    @PostMapping("/transaction")
    public TransactionResponse processTransactions(@RequestBody TransactionRequest transactionRequest) {

        return transactionService.processTransactions(transactionRequest);
    }
}
