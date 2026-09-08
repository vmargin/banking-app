package com.vmargin.banking;

import com.vmargin.banking.repository.JdbcUserRepository;
import com.vmargin.banking.repository.JdbcCashInRepository;
import com.vmargin.banking.service.CashInService;
import com.vmargin.banking.service.LoginService;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        LoginService loginService = new LoginService(new JdbcUserRepository());
        CashInService cashInService = new CashInService(new JdbcCashInRepository());
        configureLookAndFeel();
        SwingUtilities.invokeLater(
            () -> new LoginFrame(loginService, cashInService).setVisible(true)
        );
    }

    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException exception) {
            // The default Swing look and feel is an acceptable fallback.
        }
    }
}
