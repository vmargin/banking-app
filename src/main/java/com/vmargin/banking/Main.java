package com.vmargin.banking;

import com.vmargin.banking.repository.JdbcUserRepository;
import com.vmargin.banking.service.LoginService;

import javax.swing.SwingUtilities;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        LoginService loginService = new LoginService(new JdbcUserRepository());
        SwingUtilities.invokeLater(
            () -> new LoginFrame(loginService).setVisible(true)
        );
    }
}
