package com.vmargin.banking;

import com.vmargin.banking.model.User;
import com.vmargin.banking.service.LoginService;
import com.vmargin.banking.service.exception.AccountLockedException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final LoginService loginService;
    private final JTextField mobileField = new JTextField(16);
    private final JPasswordField pinField = new JPasswordField(16);
    private final JLabel feedbackLabel = new JLabel(" ");

    public LoginFrame(LoginService loginService) {
        super("JCash Banking Simulator - Login");
        this.loginService = loginService;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(buildLoginPanel());
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;
        addRow(panel, constraints, 0, "Mobile number:", mobileField);
        addRow(panel, constraints, 1, "PIN:", pinField);

        JButton loginButton = new JButton("Log in");
        loginButton.addActionListener(event -> attemptLogin());
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(loginButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        feedbackLabel.setForeground(Color.RED);
        panel.add(feedbackLabel, constraints);
        getRootPane().setDefaultButton(loginButton);
        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints constraints, int row,
                        String labelText, Component field) {
        constraints.gridx = 0;
        constraints.gridy = row;
        panel.add(new JLabel(labelText), constraints);
        constraints.gridx = 1;
        panel.add(field, constraints);
    }

    private void attemptLogin() {
        try {
            User user = loginService.login(
                mobileField.getText(), new String(pinField.getPassword())
            );
            showDashboard(user);
        } catch (AccountLockedException exception) {
            feedbackLabel.setText(exception.getMessage());
            mobileField.setEnabled(false);
            pinField.setEnabled(false);
        } catch (SQLException | IllegalArgumentException exception) {
            feedbackLabel.setText(exception.getMessage());
            pinField.setText("");
        }
    }

    private void showDashboard(User user) {
        JPanel dashboard = new JPanel(new BorderLayout(8, 8));
        dashboard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        dashboard.add(new JLabel("Welcome, " + user.getFullName()), BorderLayout.NORTH);
        JLabel balanceLabel = new JLabel(formatBalance(user.getBalance()));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(24f));
        dashboard.add(balanceLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(event -> {
            setContentPane(buildLoginPanel());
            setTitle("JCash Banking Simulator - Login");
            pack();
            setLocationRelativeTo(null);
        });
        dashboard.add(logoutButton, BorderLayout.SOUTH);
        setContentPane(dashboard);
        setTitle("JCash Banking Simulator - Dashboard");
        pack();
        setLocationRelativeTo(null);
    }

    private String formatBalance(BigDecimal balance) {
        return "Current balance: PHP " + balance.toPlainString();
    }
}
