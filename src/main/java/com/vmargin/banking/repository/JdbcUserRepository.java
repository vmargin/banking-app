package com.vmargin.banking.repository;

import com.vmargin.banking.model.BankAccount;
import com.vmargin.banking.model.User;
import com.vmargin.banking.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private static final String SAVE_USER_SQL = """
        INSERT INTO users (mobile_number, pin, full_name, balance)
        VALUES (?, ?, ?, ?)
        ON CONFLICT (mobile_number)
        DO UPDATE SET
            pin = EXCLUDED.pin,
            full_name = EXCLUDED.full_name,
            balance = EXCLUDED.balance
        """;

    private static final String FIND_BY_MOBILE_SQL = """
        SELECT id, mobile_number, pin, full_name, balance
        FROM users
        WHERE mobile_number = ?
        """;

    @Override
    public User save(User user) throws SQLException {
        Objects.requireNonNull(user, "User is required");

        try (
            Connection connection = DatabaseConnection.open();
            PreparedStatement statement = connection.prepareStatement(SAVE_USER_SQL)
        ) {
            statement.setString(1, user.getMobileNumber());
            statement.setString(2, user.getPinForPersistence());
            statement.setString(3, user.getFullName());
            statement.setBigDecimal(4, user.getBalance());
            statement.executeUpdate();
        }

        return findByMobileNumber(user.getMobileNumber())
            .orElseThrow(() -> new IllegalStateException("Saved user cannot be found"));
    }

    @Override
    public Optional<User> findByMobileNumber(String mobileNumber) throws SQLException {
        if (mobileNumber == null || mobileNumber.isBlank()) {
            throw new IllegalArgumentException("Mobile number is required");
        }

        try (
            Connection connection = DatabaseConnection.open();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_MOBILE_SQL)
        ) {
            statement.setString(1, mobileNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                long id = resultSet.getLong("id");
                String fullName = resultSet.getString("full_name");
                String accountId = "ACC-" + id;

                BankAccount bankAccount = new BankAccount(
                    accountId,
                    fullName,
                    resultSet.getBigDecimal("balance")
                );

                User user = new User(
                    id,
                    resultSet.getString("mobile_number"),
                    resultSet.getString("pin"),
                    fullName,
                    bankAccount
                );

                return Optional.of(user);
            }
        }
    }
}
