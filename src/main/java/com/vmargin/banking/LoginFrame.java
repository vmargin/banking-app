package com.vmargin.banking;

import com.vmargin.banking.model.User;
import com.vmargin.banking.service.CashInService;
import com.vmargin.banking.service.LoginService;
import com.vmargin.banking.service.TransactionHistoryService;
import com.vmargin.banking.service.TransferService;
import com.vmargin.banking.service.exception.AccountLockedException;
import com.vmargin.banking.service.exception.InvalidCredentialsException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoginFrame extends JFrame {

    private static final Color CANVAS_COLOR = new Color(247, 249, 248);
    private static final Color SURFACE_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(19, 78, 74);
    private static final Color PRIMARY_LIGHT_COLOR = new Color(220, 252, 231);
    private static final Color TEXT_COLOR = new Color(16, 42, 42);
    private static final Color MUTED_COLOR = new Color(94, 106, 106);
    private static final Color BORDER_COLOR = new Color(214, 223, 222);
    private static final Color ERROR_COLOR = new Color(185, 28, 28);
    private static final int LOGIN_WIDTH = 470;
    private static final int LOGIN_HEIGHT = 390;
    private static final int DASHBOARD_WIDTH = 650;
    private static final int DASHBOARD_HEIGHT = 500;

    private final LoginService loginService;
    private final CashInService cashInService;
    private final TransferService transferService;
    private final TransactionHistoryService historyService;
    private final JTextField mobileField = new JTextField();
    private final JPasswordField pinField = new JPasswordField();
    private final JButton loginButton = new JButton("Log in");
    private final JLabel feedbackLabel = new JLabel(" ", SwingConstants.CENTER);

    private User currentUser;

    public LoginFrame(
        LoginService loginService,
        CashInService cashInService,
        TransferService transferService,
        TransactionHistoryService historyService
    ) {
        super("JCash Banking App");
        this.loginService = loginService;
        this.cashInService = cashInService;
        this.transferService = transferService;
        this.historyService = historyService;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        showLoginScreen();
        setLocationRelativeTo(null);
    }

    private void showLoginScreen() {
        JPanel root = createScreenRoot();
        root.setBorder(BorderFactory.createEmptyBorder(32, 44, 32, 44));

        JLabel title = new JLabel("JCASH", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COLOR);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your local banking dashboard", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_COLOR);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        JPanel heading = verticalPanel();
        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(subtitle);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE_COLOR);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(24, 16, 20, 16)
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 8, 12);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        form.add(fieldLabel("Mobile number"), constraints);

        constraints.insets = new Insets(0, 0, 8, 0);
        constraints.weightx = 1;
        constraints.gridx = 1;
        form.add(mobileField, constraints);

        constraints.insets = new Insets(0, 0, 22, 12);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        form.add(fieldLabel("PIN"), constraints);

        constraints.insets = new Insets(0, 0, 22, 0);
        constraints.weightx = 1;
        constraints.gridx = 1;
        form.add(pinField, constraints);

        stylePrimaryButton(loginButton);
        loginButton.addActionListener(event -> attemptLogin());
        constraints.insets = new Insets(0, 0, 12, 0);
        constraints.gridx = 1;
        constraints.gridy = 2;
        form.add(loginButton, constraints);

        feedbackLabel.setForeground(ERROR_COLOR);
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        form.add(feedbackLabel, constraints);

        root.add(heading, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
        setSize(LOGIN_WIDTH, LOGIN_HEIGHT);
        getRootPane().setDefaultButton(loginButton);
        mobileField.requestFocusInWindow();
        refreshScreen();
    }

    private void attemptLogin() {
        String mobile = mobileField.getText();
        String pin = new String(pinField.getPassword());

        try {
            currentUser = loginService.login(mobile, pin);
            showDashboard();
        } catch (InvalidCredentialsException exception) {
            int remainingAttempts = loginService.getRemainingAttempts();
            showFeedback(
                exception.getMessage() + ". " + remainingAttempts + " attempts remaining."
            );
            pinField.selectAll();
            pinField.requestFocusInWindow();
        } catch (AccountLockedException exception) {
            showFeedback(exception.getMessage());
            setLoginControlsEnabled(false);
        } catch (SQLException exception) {
            showFeedback("Unable to reach the local database. Check the connection settings.");
        } catch (IllegalStateException exception) {
            showFeedback(
                "Database configuration is missing. Set BANKING_DB_USER and "
                    + "BANKING_DB_PASSWORD in the run configuration."
            );
        }
    }

    private void showDashboard() {
        JPanel root = createScreenRoot();
        root.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        root.add(createDashboardHeader(), BorderLayout.NORTH);
        root.add(createDashboardContent(), BorderLayout.CENTER);
        setContentPane(root);
        setSize(DASHBOARD_WIDTH, DASHBOARD_HEIGHT);
        refreshScreen();
    }

    private JPanel createDashboardHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel brand = new JLabel("JCASH");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setForeground(PRIMARY_COLOR);

        JButton logoutButton = new JButton("Log out");
        styleSecondaryButton(logoutButton);
        logoutButton.addActionListener(event -> resetToLogin());

        header.add(brand, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);
        return header;
    }

    private JPanel createDashboardContent() {
        JPanel content = verticalPanel();
        content.setBorder(BorderFactory.createEmptyBorder(26, 0, 0, 0));

        JLabel greeting = new JLabel("Welcome back, " + currentUser.getFullName());
        greeting.setFont(new Font("Segoe UI", Font.BOLD, 25));
        greeting.setForeground(TEXT_COLOR);
        greeting.setAlignmentX(LEFT_ALIGNMENT);

        JLabel description = new JLabel("Here is your current account overview.");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(MUTED_COLOR);
        description.setAlignmentX(LEFT_ALIGNMENT);

        content.add(greeting);
        content.add(Box.createVerticalStrut(6));
        content.add(description);
        content.add(Box.createVerticalStrut(22));
        content.add(createBalanceCard());
        content.add(Box.createVerticalStrut(24));
        content.add(sectionLabel("Quick actions"));
        content.add(Box.createVerticalStrut(10));
        content.add(createQuickActions());
        return content;
    }

    private JPanel createBalanceCard() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(PRIMARY_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(24, 26, 24, 26)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 128));

        JLabel label = new JLabel("AVAILABLE BALANCE");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(PRIMARY_LIGHT_COLOR);

        JLabel balance = new JLabel(formatBalance(currentUser.getBalance()));
        balance.setFont(new Font("Segoe UI", Font.BOLD, 30));
        balance.setForeground(Color.WHITE);

        JLabel hint = new JLabel("Balance loaded from your local PostgreSQL account.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(PRIMARY_LIGHT_COLOR);

        card.add(label, BorderLayout.NORTH);
        card.add(balance, BorderLayout.CENTER);
        card.add(hint, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createQuickActions() {
        JPanel actions = new JPanel(new GridLayout(1, 3, 12, 0));
        actions.setOpaque(false);
        actions.add(createActionButton("Cash In", "Add funds", "cash-in"));
        actions.add(createActionButton("Transfer", "Send money", "transfer"));
        actions.add(createActionButton("History", "View activity", "history"));
        return actions;
    }

    private JButton createActionButton(String title, String description, String featureName) {
        JButton button = new JButton("<html><b>" + title + "</b><br><small>"
            + description + "</small></html>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        styleSecondaryButton(button);
        button.addActionListener(event -> openQuickAction(featureName));
        return button;
    }

    private void openQuickAction(String featureName) {
        switch (featureName) {
            case "cash-in" -> showCashInScreen();
            case "transfer" -> showTransferScreen();
            case "history" -> showHistoryScreen();
            default -> throw new IllegalArgumentException("Unknown quick action");
        }
    }

    private void showCashInScreen() {
        JTextField amountField = new JTextField();
        JTextField detailsField = new JTextField("Cash-in");
        JButton submitButton = new JButton("Confirm cash-in");
        stylePrimaryButton(submitButton);
        submitButton.addActionListener(event -> {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                cashInService.cashIn(currentUser, amount, detailsField.getText());
                showDashboard();
            } catch (NumberFormatException exception) {
                showMessageDialog("Enter a valid amount, for example 500.00.");
            } catch (IllegalArgumentException exception) {
                showMessageDialog(exception.getMessage());
            } catch (SQLException exception) {
                showMessageDialog("Cash-in could not be saved. No balance was changed.");
            }
        });
        showFormScreen(
            "Cash in",
            "Add funds to your account and record the activity.",
            new String[] {"Amount (PHP)", "Details"},
            new JTextField[] {amountField, detailsField},
            submitButton
        );
        amountField.requestFocusInWindow();
    }

    private void showTransferScreen() {
        JTextField mobileNumberField = new JTextField();
        JTextField amountField = new JTextField();
        JButton submitButton = new JButton("Confirm transfer");
        stylePrimaryButton(submitButton);
        submitButton.addActionListener(event -> {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                transferService.transfer(currentUser, mobileNumberField.getText(), amount);
                showDashboard();
            } catch (NumberFormatException exception) {
                showMessageDialog("Enter a valid amount, for example 500.00.");
            } catch (IllegalArgumentException exception) {
                showMessageDialog(exception.getMessage());
            } catch (SQLException exception) {
                showMessageDialog("Transfer could not be saved. No balance was changed.");
            }
        });
        showFormScreen(
            "Transfer",
            "Send money securely to another local JCash account.",
            new String[] {"Recipient mobile number", "Amount (PHP)"},
            new JTextField[] {mobileNumberField, amountField},
            submitButton
        );
        mobileNumberField.requestFocusInWindow();
    }

    private void showHistoryScreen() {
        JPanel root = createScreenRoot();
        root.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        root.add(createDashboardHeader(), BorderLayout.NORTH);

        JPanel content = verticalPanel();
        content.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
        JLabel title = new JLabel("Transaction history");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(LEFT_ALIGNMENT);
        JLabel message = new JLabel("Your most recent account activity appears first.");
        message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        message.setForeground(MUTED_COLOR);
        message.setAlignmentX(LEFT_ALIGNMENT);
        JButton backButton = new JButton("Back to dashboard");
        stylePrimaryButton(backButton);
        backButton.addActionListener(event -> showDashboard());

        content.add(title);
        content.add(Box.createVerticalStrut(6));
        content.add(message);
        content.add(Box.createVerticalStrut(18));
        content.add(createHistoryTable());
        content.add(Box.createVerticalStrut(18));
        content.add(backButton);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setSize(DASHBOARD_WIDTH, DASHBOARD_HEIGHT);
        refreshScreen();
    }

    private JScrollPane createHistoryTable() {
        String[] columns = {"Type", "Amount", "Details", "When"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            List<com.vmargin.banking.model.Transaction> transactions = historyService.getHistory(
                currentUser
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
            for (com.vmargin.banking.model.Transaction transaction : transactions) {
                tableModel.addRow(new Object[] {
                    formatTransactionType(transaction.getType().name()),
                    formatBalance(transaction.getAmount()),
                    transaction.getDetails(),
                    transaction.getOccurredAt().format(formatter)
                });
            }
        } catch (SQLException exception) {
            showMessageDialog("Transaction history could not be loaded.");
        }

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(SURFACE_COLOR);
        table.getTableHeader().setForeground(TEXT_COLOR);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.setPreferredSize(new Dimension(DASHBOARD_WIDTH - 60, 230));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
        return scrollPane;
    }

    private void showFormScreen(
        String titleText,
        String subtitleText,
        String[] fieldNames,
        JTextField[] fields,
        JButton submitButton
    ) {
        JPanel root = createScreenRoot();
        root.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        root.add(createDashboardHeader(), BorderLayout.NORTH);

        JPanel content = verticalPanel();
        content.setBorder(BorderFactory.createEmptyBorder(30, 24, 0, 24));
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(LEFT_ALIGNMENT);
        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setForeground(MUTED_COLOR);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SURFACE_COLOR);
        form.setAlignmentX(LEFT_ALIGNMENT);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(22, 20, 20, 20)
        ));
        addFormFields(form, fieldNames, fields, submitButton);

        content.add(title);
        content.add(Box.createVerticalStrut(6));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(22));
        content.add(form);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        setSize(DASHBOARD_WIDTH, DASHBOARD_HEIGHT);
        refreshScreen();
    }

    private void addFormFields(
        JPanel form,
        String[] fieldNames,
        JTextField[] fields,
        JButton submitButton
    ) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        for (int index = 0; index < fields.length; index++) {
            constraints.gridy = index;
            constraints.gridx = 0;
            constraints.weightx = 0;
            constraints.insets = new Insets(0, 0, 14, 16);
            form.add(fieldLabel(fieldNames[index]), constraints);
            constraints.gridx = 1;
            constraints.weightx = 1;
            constraints.insets = new Insets(0, 0, 14, 0);
            form.add(fields[index], constraints);
        }
        constraints.gridy = fields.length;
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(4, 0, 0, 0);
        form.add(submitButton, constraints);
    }

    private JPanel createScreenRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CANVAS_COLOR);
        return root;
    }

    private JPanel verticalPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(SURFACE_COLOR);
        button.setForeground(PRIMARY_COLOR);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(9, 16, 9, 16)
        ));
        button.setFocusPainted(true);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(SURFACE_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        button.setFocusPainted(true);
    }

    private void resetToLogin() {
        currentUser = null;
        mobileField.setText("");
        pinField.setText("");
        setLoginControlsEnabled(true);
        feedbackLabel.setText(" ");
        showLoginScreen();
    }

    private void setLoginControlsEnabled(boolean enabled) {
        mobileField.setEnabled(enabled);
        pinField.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }

    private void showFeedback(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setForeground(ERROR_COLOR);
    }

    private void showMessageDialog(String message) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            message,
            "Cash-in",
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
    }

    private void refreshScreen() {
        getContentPane().revalidate();
        getContentPane().repaint();
        setLocationRelativeTo(null);
    }

    private String formatBalance(BigDecimal balance) {
        return "PHP " + balance.toPlainString();
    }

    private String formatTransactionType(String type) {
        return switch (type) {
            case "CASH_IN" -> "Cash in";
            case "TRANSFER_SENT" -> "Transfer sent";
            case "TRANSFER_RECEIVED" -> "Transfer received";
            default -> type;
        };
    }
}
