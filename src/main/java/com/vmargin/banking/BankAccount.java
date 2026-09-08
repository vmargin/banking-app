package com.vmargin.banking;
import java.math.BigDecimal;

public class BankAccount {
    private String accountId;
    private String holderName;
    private BigDecimal balance;

    public BankAccount(String accountId, String holderName, BigDecimal balance) {

        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID is required");
        }
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Holder name is required");
        }
        if (balance == null) {
            throw new IllegalArgumentException("Opening balance is required");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                "Opening balance cannot be negative");
        }
        if (balance.scale() > 2) {
            throw new IllegalArgumentException(
                "Opening balance cannot have more than 2 decimals");
        }

        this.accountId = accountId;
        this.holderName = holderName;
        this.balance = balance;

    }

    public String getAccountId() {
        return accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public static void main(String[] args) {

        BankAccount bankAccount = new BankAccount("ACC-001", "Valkenburgh Margin", new BigDecimal("1000.00"));
        System.out.println("ACC ID: " + bankAccount.getAccountId());
        System.out.println("Holder Name: " + bankAccount.getHolderName());
        System.out.println("Balance: PHP " + bankAccount.getBalance());

    }
}
