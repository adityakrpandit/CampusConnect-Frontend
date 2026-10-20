package com.campusconnect.ui.login;

import com.campusconnect.ui.admin.AdminDashboardFrame;
import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public AdminLoginFrame() {

        // =========================
        // FRAME SETTINGS
        // =========================

        setTitle("CampusConnect - Admin Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        // Minimum size when restored
        setMinimumSize(new Dimension(750, 550));

        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        // =========================
        // LOGIN PANEL
        // =========================

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setPreferredSize(
                new Dimension(500, 400)
        );
        loginPanel.setMinimumSize(
                new Dimension(500, 400)
        );
        loginPanel.setBackground(Color.WHITE);

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel =
                new JLabel("CampusConnect");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        titleLabel.setBounds(
                140, 35, 300, 40
        );

        loginPanel.add(titleLabel);

        // =========================
        // SUBTITLE
        // =========================

        JLabel subtitleLabel =
                new JLabel("Admin Login");

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 18)
        );

        subtitleLabel.setBounds(
                200, 80, 150, 30
        );

        loginPanel.add(subtitleLabel);

        // =========================
        // EMAIL
        // =========================

        JLabel emailLabel =
                new JLabel("Email:");

        emailLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        emailLabel.setBounds(
                80, 135, 100, 30
        );

        loginPanel.add(emailLabel);

        emailField = new JTextField();

        emailField.setBounds(
                190, 135, 230, 35
        );

        loginPanel.add(emailField);

        // =========================
        // PASSWORD
        // =========================

        JLabel passwordLabel =
                new JLabel("Password:");

        passwordLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        passwordLabel.setBounds(
                80, 190, 100, 30
        );

        loginPanel.add(passwordLabel);

        passwordField =
                new JPasswordField();

        passwordField.setBounds(
                190, 190, 230, 35
        );

        loginPanel.add(passwordField);

        // =========================
        // LOGIN BUTTON
        // =========================

        JButton loginButton =
                new JButton("Admin Login");

        loginButton.setBounds(
                190, 250, 140, 35
        );

        loginPanel.add(loginButton);

        // =========================
        // BACK BUTTON
        // =========================

        JButton backButton =
                new JButton("Back");

        backButton.setBounds(
                190, 300, 140, 35
        );

        loginPanel.add(backButton);

        // =========================
        // LOGIN ACTION
        // =========================

        loginButton.addActionListener(e ->
                adminLogin()
        );

        // =========================
        // BACK ACTION
        // =========================

        backButton.addActionListener(e ->
                dispose()
        );

        // =========================
        // ADD LOGIN PANEL
        // =========================

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        mainPanel.add(
                loginPanel,
                gbc
        );

        // =========================
        // ADD MAIN PANEL
        // =========================

        add(mainPanel);

        // =========================
        // INITIAL SIZE
        // =========================

        setSize(750, 550);
        setLocationRelativeTo(null);

        // =========================
        // OPEN MAXIMIZED
        // =========================

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        // =========================
        // SHOW WINDOW
        // =========================

        setVisible(true);
    }

    // =========================
    // ADMIN LOGIN
    // =========================

    private void adminLogin() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // =========================
        // EMPTY FIELD VALIDATION
        // =========================

        if (email.isEmpty() ||
                password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =========================
        // SQL QUERY
        // =========================

        String sql =
                "SELECT admin_id, name FROM admins " +
                        "WHERE email = ? AND password = ?";

        // =========================
        // DATABASE CONNECTION
        // =========================

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet =
                    statement.executeQuery();

            // =========================
            // LOGIN SUCCESS
            // =========================

            if (resultSet.next()) {

                String adminName =
                        resultSet.getString("name");

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, " + adminName + "!",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                new AdminDashboardFrame(
                        adminName
                );

            } else {

                // =========================
                // LOGIN FAILED
                // =========================

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid admin email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            // =========================
            // DATABASE ERROR
            // =========================

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " +
                            e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                AdminLoginFrame::new
        );
    }
}