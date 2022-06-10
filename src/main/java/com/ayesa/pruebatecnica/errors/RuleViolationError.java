package com.ayesa.pruebatecnica.errors;

public class RuleViolationError {

    //Account
    public static final String ACCOUNT_ALREADY_INITIALIZED = "account-already-initialized";
    public static final String ACCOUNT_INVALID_AVAILABLE_LIMIT = "account-invalid-available-limit";
    //Transaction
    public static final String ACCOUNT_NOT_INITIALIZED = "account-not-initialized";
    public static final String CARD_NOT_ACTIVE = "card-not-active";
    public static final String INSUFFICIENT_LIMIT = "insufficient-limit";
    public static final String HIGH_FREQUENCY_SMALL_INTERVAL = "high-frequency-small-interval";
    public static final String DOUBLED_TRANSACTION = "doubled-transaction";
    
}
