package com.ayesa.pruebatecnica;

import static org.junit.Assert.assertSame;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ayesa.pruebatecnica.dtos.Account;
import com.ayesa.pruebatecnica.dtos.Transaction;
import com.ayesa.pruebatecnica.requests.CreateAccountRequest;
import com.ayesa.pruebatecnica.requests.TransactionRequest;
import com.ayesa.pruebatecnica.services.TransactionService;

import static com.ayesa.pruebatecnica.errors.RuleViolationError.*;

@SpringBootTest
public class PruebaTecnicaApplicationTests {

	private TransactionService transactionService = new TransactionService();

	public Account account = new Account(1, true, 100, new ArrayList<>());
	public Account accountTwo = new Account(2, true, -100, new ArrayList<>());
	public Account accountThree = new Account(3, false, 100, new ArrayList<>());

	public Transaction transaction = new Transaction(1, "Coca Cola", 10, LocalDateTime.now());
	public Transaction transactionTwo = new Transaction(1, "Apple", 200, LocalDateTime.now());
	public Transaction transactionThree = new Transaction(3, "Disney", 10, LocalDateTime.now());
	public Transaction transactionFourth = new Transaction(1, "Coca Cola", 11, LocalDateTime.now());
	public Transaction transactionFive = new Transaction(1, "Coca Cola", 12, LocalDateTime.now());
	public Transaction transactionSix = new Transaction(1, "Coca Cola", 13, LocalDateTime.now());


	public PruebaTecnicaApplicationTests() {
    }

	//Account
	@Test
	public void accountAlreadyInitializedTest() {
		transactionService.createAccount(new CreateAccountRequest(account));
		assertSame(ACCOUNT_ALREADY_INITIALIZED, transactionService.createAccount(new CreateAccountRequest(this.account)).getViolations().get(0));
	}

	@Test
	public void accountInvalidAvailableLimitTest() {
		assertSame(ACCOUNT_INVALID_AVAILABLE_LIMIT, transactionService.createAccount(new CreateAccountRequest(this.accountTwo)).getViolations().get(0));
	}

	//Transaction
	@Test
	public void accountNotInitializedTest() {
		assertSame(ACCOUNT_NOT_INITIALIZED, transactionService.processTransactions(new TransactionRequest(this.transactionThree)).getViolations().get(0));
	}

	@Test
	public void cardNotActiveTest() {
		transactionService.createAccount(new CreateAccountRequest(accountThree));
		assertSame(CARD_NOT_ACTIVE, transactionService.processTransactions(new TransactionRequest(this.transactionThree)).getViolations().get(0));
	}

	@Test
	public void insufficientLimitTest() {
		transactionService.createAccount(new CreateAccountRequest(account));
		assertSame(INSUFFICIENT_LIMIT, transactionService.processTransactions(new TransactionRequest(this.transactionTwo)).getViolations().get(0));
	}

	@Test
	public void highFrequencySmallIntervalTest() {
		transactionService.createAccount(new CreateAccountRequest(account));
		transactionService.processTransactions(new TransactionRequest(this.transaction)); //First Transaction
		transactionService.processTransactions(new TransactionRequest(this.transactionFourth)); //Second Transaction
		transactionService.processTransactions(new TransactionRequest(this.transactionFive)); //Third Transaction
		assertSame(HIGH_FREQUENCY_SMALL_INTERVAL, transactionService.processTransactions(new TransactionRequest(this.transactionSix)).getViolations().get(0));
	}

	@Test
	public void doubledTransactionTest() {
		transactionService.createAccount(new CreateAccountRequest(account));
		transactionService.processTransactions(new TransactionRequest(this.transaction)); //First Transaction
		assertSame(DOUBLED_TRANSACTION, transactionService.processTransactions(new TransactionRequest(this.transaction)).getViolations().get(0));
	}

}
