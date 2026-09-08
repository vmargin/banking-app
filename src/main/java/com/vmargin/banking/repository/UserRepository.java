package com.vmargin.banking.repository;

import com.vmargin.banking.model.User;
import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    User save(User user) throws SQLException;

    Optional<User> findByMobileNumber(String mobileNumber) throws SQLException;
}
