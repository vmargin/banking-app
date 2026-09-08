package com.vmargin.banking;

import com.vmargin.banking.model.User;
import com.vmargin.banking.service.LoginService;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
    private final JTextField mobileField = new JTextField();
    private final JPasswordField pinField = new JPasswordField();
    private final JButton loginButton = new JButton("Log in");
    private final JLabel feedbackLabel = new JLabel(" ", SwingConstants.CENTER);

    private User currentUser;

    public LoginFrame(LoginService loginService) {
        super("JCash Banking App");
        this.loginService = loginService;
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

        JLabel description = new JLabel("Here is your current account overview.");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(MUTED_COLOR);

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
        actions.add(createActionButton("Cash In", "Add funds", "Cash-in"));
        actions.add(createActionButton("Transfer", "Send money", "Transfer"));
        actions.add(createActionButton("History", "View activity", "Transaction history"));
        return actions;
    }

    private JButton createActionButton(String title, String description, String featureName) {
        JButton button = new JButton("<html><b>" + title + "</b><br><small>"
            + description + "</small></html>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        styleSecondaryButton(button);
        button.addActionListener(event -> showFeaturePreview(featureName));
        return button;
    }

    private void showFeaturePreview(String featureName) {
        JPanel root = createScreenRoot();
        root.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        root.add(createDashboardHeader(), BorderLayout.NORTH);

        JPanel content = verticalPanel();
        content.setBorder(BorderFactory.createEmptyBorder(56, 28, 0, 28));
        JLabel title = new JLabel(featureName);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_COLOR);
        JLabel message = new JLabel(
            "This interface is reserved for the next banking feature."
        );
        message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        message.setForeground(MUTED_COLOR);
        JButton backButton = new JButton("Back to dashboard");
        stylePrimaryButton(backButton);
        backButton.addActionListener(event -> showDashboard());

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(message);
        content.add(Box.createVerticalStrut(28));
        content.add(backButton);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
        refreshScreen();
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

    private void refreshScreen() {
        getContentPane().revalidate();
        getContentPane().repaint();
        setLocationRelativeTo(null);
    }

    private String formatBalance(BigDecimal balance) {
        return "PHP " + balance.toPlainString();
    }
}
