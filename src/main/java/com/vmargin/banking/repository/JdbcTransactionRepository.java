package com.vmargin.banking.repository;

import com.vmargin.banking.model.Transaction;
import com.vmargin.banking.model.TransactionType;
import com.vmargin.banking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcTransactionRepository implements TransactionRepository {

    private static final String SAVE_SQL = """
        INSERT INTO transactions (user_id, type, amount, details, occurred_at)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id
        """;

    private static final String FIND_BY_USER_SQL = """
        SELECT id, user_id, type, amount, details, occurred_at
        FROM transactions
        WHERE user_id = ?
        ORDER BY occurred_at DESC, id DESC
        """;

    @Override
    public Transaction save(Transaction transaction) throws SQLException {
        Objects.requireNonNull(transaction, "Transaction is required");

        try (
            Connection connection = DatabaseConnection.open();
            PreparedStatement statement = connection.prepareStatement(SAVE_SQL)
        ) {
            statement.setLong(1, transaction.getUserId());
            statement.setString(2, transaction.getType().name());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setString(4, transaction.getDetails());
            statement.setTimestamp(5, Timestamp.valueOf(transaction.getOccurredAt()));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Transaction ID was not generated");
                }
                return new Transaction(
                    resultSet.getLong(1),
                    transaction.getUserId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getDetails(),
                    transaction.getOccurredAt()
                );
            }
        }
    }

    @Override
    public List<Transaction> findByUserId(long userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }

        List<Transaction> transactions = new ArrayList<>();
        try (
            Connection connection = DatabaseConnection.open();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_SQL)
        ) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                        resultSet.getLong("id"),
                        resultSet.getLong("user_id"),
                        TransactionType.valueOf(resultSet.getString("type")),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getString("details"),
                        resultSet.getTimestamp("occurred_at").toLocalDateTime()
                    ));
                }
            }
        }
        return List.copyOf(transactions);
    }
}
