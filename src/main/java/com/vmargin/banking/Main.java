package com.vmargin.banking;

import com.vmargin.banking.repository.JdbcUserRepository;
import com.vmargin.banking.repository.JdbcCashInRepository;
import com.vmargin.banking.repository.JdbcTransactionRepository;
import com.vmargin.banking.repository.JdbcTransferRepository;
import com.vmargin.banking.service.CashInService;
import com.vmargin.banking.service.LoginService;
import com.vmargin.banking.service.TransactionHistoryService;
import com.vmargin.banking.service.TransferService;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        LoginService loginService = new LoginService(new JdbcUserRepository());
        CashInService cashInService = new CashInService(new JdbcCashInRepository());
        TransferService transferService = new TransferService(new JdbcTransferRepository());
        TransactionHistoryService historyService = new TransactionHistoryService(
            new JdbcTransactionRepository()
        );
        configureLookAndFeel();
        SwingUtilities.invokeLater(
            () -> new LoginFrame(
                loginService,
                cashInService,
                transferService,
                historyService
            ).setVisible(true)
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
