package com.ayesa.pruebatecnica.services;

import com.ayesa.pruebatecnica.dtos.Account;
import com.ayesa.pruebatecnica.requests.CreateAccountRequest;
import com.ayesa.pruebatecnica.requests.TransactionRequest;
import com.ayesa.pruebatecnica.responses.TransactionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.ayesa.pruebatecnica.errors.RuleViolationError.*;

@Service
public class TransactionService {

    private final Map<Integer, Account> accounts = new HashMap<>();

    public TransactionResponse createAccount(CreateAccountRequest createAccountRequest) {

        TransactionResponse response = new TransactionResponse();
        response.setAccount(createAccountRequest.getAccount());

        if (accounts.containsKey(createAccountRequest.getAccount().getId())) {
            response.setAccount(accounts.get(createAccountRequest.getAccount().getId()));
            response.getViolations().add(ACCOUNT_ALREADY_INITIALIZED);
        } else if (createAccountRequest.getAccount().getAvailableLimit() < 0) {
            response.getViolations().add(ACCOUNT_INVALID_AVAILABLE_LIMIT);
        } else {
            accounts.put(
                    createAccountRequest.getAccount().getId(),
                    createAccountRequest.getAccount()
            );
        }

        return response;
    }

    public TransactionResponse processTransactions(TransactionRequest transactionRequest) {

        TransactionResponse response = new TransactionResponse();

        Account ownerAccount = accounts.get(transactionRequest.getTransaction().getId());


        if (ownerAccount == null) {
            response.getViolations().add(ACCOUNT_NOT_INITIALIZED);
        } else if (!ownerAccount.getActiveCard()) {
            response.getViolations().add(CARD_NOT_ACTIVE);
        } else if (ownerAccount.getAvailableLimit() < transactionRequest.getTransaction().getAmount()) {
            response.getViolations().add(INSUFFICIENT_LIMIT);
        } else if (ownerAccount.getTransactions()
                               .stream()
                               .filter(
                                    transaction -> {
                                        LocalDateTime maxDate = transactionRequest.getTransaction().getTime();
                                        LocalDateTime minDate = transactionRequest.getTransaction().getTime().minusMinutes(2);
                                        return (transaction.getTime().isAfter(minDate) && transaction.getTime().isBefore(maxDate))
                                                || transaction.getTime().equals(minDate) || transaction.getTime().equals(maxDate);

                                    }
                                )
                                .count() >= 3) {
            response.getViolations().add(HIGH_FREQUENCY_SMALL_INTERVAL);
        } else if (ownerAccount.getTransactions()
                               .stream()
                               .anyMatch(
                                    transaction -> {
                                        LocalDateTime maxDate = transactionRequest.getTransaction().getTime();
                                        LocalDateTime minDate = transactionRequest.getTransaction().getTime().minusMinutes(2);
                                        return  transaction.getAmount().equals(transactionRequest.getTransaction().getAmount()) &&
                                                transaction.getMerchant().equals(transactionRequest.getTransaction().getMerchant()) &&
                                                ((transaction.getTime().isAfter(minDate) && transaction.getTime().isBefore(maxDate))
                                                || transaction.getTime().equals(minDate) || transaction.getTime().equals(maxDate));
                                    }
                                )) {
            response.getViolations().add(DOUBLED_TRANSACTION);
        } else {
            ownerAccount.setAvailableLimit(ownerAccount.getAvailableLimit() - transactionRequest.getTransaction().getAmount());
            ownerAccount.getTransactions().add(transactionRequest.getTransaction());
            accounts.put(ownerAccount.getId(), ownerAccount);
        }

        response.setAccount(ownerAccount);

        return response;
    }
}
