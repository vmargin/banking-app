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
import java.math.BigDecimal;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(19, 78, 74);
    private static final Color ERROR_COLOR = new Color(185, 28, 28);
    private static final int WINDOW_WIDTH = 470;
    private static final int WINDOW_HEIGHT = 360;

    private final LoginService loginService;
    private final JTextField mobileField = new JTextField();
    private final JPasswordField pinField = new JPasswordField();
    private final JButton loginButton = new JButton("Log in");
    private final JLabel feedbackLabel = new JLabel(" ", SwingConstants.CENTER);

    public LoginFrame(LoginService loginService) {
        super("JCash Banking Simulator");
        this.loginService = loginService;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        showLoginScreen();
        setLocationRelativeTo(null);
    }

    private void showLoginScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));

        JLabel title = new JLabel("JCASH", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);

        JLabel subtitle = new JLabel(
            "Sign in to your banking dashboard",
            SwingConstants.CENTER
        );
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(subtitle);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(24, 16, 0, 16));
        form.add(fieldLabel("Mobile number"));
        form.add(mobileField);
        form.add(Box.createVerticalStrut(14));
        form.add(fieldLabel("PIN"));
        form.add(pinField);
        form.add(Box.createVerticalStrut(18));

        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setPreferredSize(new Dimension(0, 40));
        loginButton.addActionListener(event -> attemptLogin());
        form.add(loginButton);
        form.add(Box.createVerticalStrut(12));

        feedbackLabel.setForeground(ERROR_COLOR);
        feedbackLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        form.add(feedbackLabel);

        root.add(heading, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
        getRootPane().setDefaultButton(loginButton);
        revalidate();
        repaint();
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        return label;
    }

    private void attemptLogin() {
        String mobile = mobileField.getText();
        String pin = new String(pinField.getPassword());

        try {
            User user = loginService.login(mobile, pin);
            showDashboard(user);
        } catch (InvalidCredentialsException exception) {
            int remainingAttempts = loginService.getRemainingAttempts();
            showFeedback(
                exception.getMessage() + ". " + remainingAttempts + " attempts remaining.",
                ERROR_COLOR
            );
            pinField.selectAll();
            pinField.requestFocusInWindow();
        } catch (AccountLockedException exception) {
            showFeedback(exception.getMessage(), ERROR_COLOR);
            setLoginControlsEnabled(false);
        } catch (SQLException exception) {
            showFeedback(
                "Unable to reach the local database. Check the connection settings.",
                ERROR_COLOR
            );
        }
    }

    private void showDashboard(User user) {
        JPanel root = new JPanel(new BorderLayout(0, 24));
        root.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));

        JLabel greeting = new JLabel("Welcome, " + user.getFullName());
        greeting.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        JLabel description = new JLabel("Your persisted account balance");
        description.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        JPanel balanceCard = new JPanel(new GridLayout(2, 1, 0, 6));
        balanceCard.setBackground(PRIMARY_COLOR);
        balanceCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        JLabel balanceLabel = new JLabel(formatBalance(user.getBalance()));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 27));
        JLabel balanceHint = new JLabel("Cash-in and transfer are the next features.");
        balanceHint.setForeground(new Color(220, 252, 231));
        balanceCard.add(balanceLabel);
        balanceCard.add(balanceHint);

        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(event -> resetToLogin());

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(greeting);
        heading.add(Box.createVerticalStrut(6));
        heading.add(description);

        root.add(heading, BorderLayout.NORTH);
        root.add(balanceCard, BorderLayout.CENTER);
        root.add(logoutButton, BorderLayout.SOUTH);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private void resetToLogin() {
        mobileField.setText("");
        pinField.setText("");
        showLoginScreen();
        mobileField.requestFocusInWindow();
    }

    private void setLoginControlsEnabled(boolean enabled) {
        mobileField.setEnabled(enabled);
        pinField.setEnabled(enabled);
        loginButton.setEnabled(enabled);
    }

    private void showFeedback(String message, Color color) {
        feedbackLabel.setText(message);
        feedbackLabel.setForeground(color);
    }

    private String formatBalance(BigDecimal balance) {
        return "PHP " + balance.toPlainString();
    }
}
