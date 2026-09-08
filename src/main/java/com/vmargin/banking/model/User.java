package com.vmargin.banking.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final long id;
    private final String mobileNumber;
    private final String pin;
    private final String fullName;
    private final BankAccount bankAccount;
    private final List<Transaction> transactions;

    public User(
        long id,
        String mobileNumber,
        String pin,
        String fullName,
        BankAccount bankAccount
    ) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (mobileNumber == null || mobileNumber.isBlank()) {
            throw new IllegalArgumentException("Mobile number is required");
        }
        if (pin == null || pin.isBlank()) {
            throw new IllegalArgumentException("PIN is required");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }

        this.id = id;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
        this.fullName = fullName;
        this.bankAccount = Objects.requireNonNull(
            bankAccount,
            "Bank account is required"
        );
        this.transactions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPinForPersistence() {
        return pin;
    }

    public String getFullName() {
        return fullName;
    }

    public BigDecimal getBalance() {
        return bankAccount.getBalance();
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public void addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction is required");

        if (transaction.getUserId() != id) {
            throw new IllegalArgumentException(
                "Transaction user ID must match this user"
            );
        }

        transactions.add(transaction);
    }
}
