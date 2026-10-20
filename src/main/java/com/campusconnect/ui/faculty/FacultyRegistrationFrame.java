
package com.campusconnect.ui.faculty;

import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.ui.login.LoginFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FacultyRegistrationFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField departmentField;
    private JTextField designationField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private BufferedImage backgroundImage;
    private BufferedImage logoImage;

    private final Color PRIMARY_COLOR = new Color(20, 150, 160);
    private final Color PRIMARY_DARK = new Color(15, 115, 125);
    private final Color DARK_TEXT = new Color(30, 45, 55);
    private final Color LIGHT_TEXT = new Color(100, 115, 125);
    private final Color BORDER_COLOR = new Color(205, 220, 225);

    public FacultyRegistrationFrame() {

        setTitle("CampusConnect - Faculty Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 700));
        setResizable(true);

        try {
            backgroundImage = ImageIO.read(
                    getClass().getResource("/campusconnect_background.png")
            );
        } catch (Exception e) {
            System.out.println("Background image not found.");
        }

        try {
            logoImage = ImageIO.read(
                    getClass().getResource("/campusconnect_logo.png")
            );
        } catch (Exception e) {
            System.out.println("Logo image not found.");
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);

        JPanel registrationCard = new JPanel(null);
        registrationCard.setBackground(new Color(255, 255, 255, 248));

        registrationCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 235), 1
                        ),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        backgroundPanel.add(registrationCard);

        // LOGO
        JLabel logoLabel;

        if (logoImage != null) {
            Image scaledLogo = logoImage.getScaledInstance(
                    115, 70, Image.SCALE_SMOOTH
            );
            logoLabel = new JLabel(new ImageIcon(scaledLogo));
        } else {
            logoLabel = new JLabel(
                    "CampusConnect", SwingConstants.CENTER
            );
            logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
            logoLabel.setForeground(PRIMARY_COLOR);
        }

        logoLabel.setBounds(145, 10, 230, 70);
        registrationCard.add(logoLabel);

        // TITLE
        JLabel titleLabel = new JLabel(
                "Faculty Registration", SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        titleLabel.setBounds(40, 78, 440, 32);
        registrationCard.add(titleLabel);

        JLabel subtitleLabel = new JLabel(
                "Register to join the CampusConnect faculty portal",
                SwingConstants.CENTER
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(LIGHT_TEXT);
        subtitleLabel.setBounds(25, 110, 470, 24);
        registrationCard.add(subtitleLabel);

        // FULL NAME
        addLabel(registrationCard, "FULL NAME", 40, 145, 200, 20);

        nameField = createTextField("Enter your full name");
        nameField.setBounds(40, 168, 450, 35);
        registrationCard.add(nameField);

        // EMAIL
        addLabel(registrationCard, "EMAIL ADDRESS", 40, 210, 200, 20);

        emailField = createTextField("Enter your email");
        emailField.setBounds(40, 233, 450, 35);
        registrationCard.add(emailField);

        // PASSWORD
        addLabel(registrationCard, "PASSWORD", 40, 275, 200, 20);

        passwordField = createPasswordField();
        passwordField.setToolTipText("Minimum 6 characters");
        passwordField.setBounds(40, 298, 450, 35);
        registrationCard.add(passwordField);

        // CONFIRM PASSWORD
        addLabel(registrationCard, "CONFIRM PASSWORD",
                40, 340, 220, 20);

        confirmPasswordField = createPasswordField();
        confirmPasswordField.setToolTipText("Re-enter your password");
        confirmPasswordField.setBounds(40, 363, 450, 35);
        registrationCard.add(confirmPasswordField);

        // DEPARTMENT
        addLabel(registrationCard, "DEPARTMENT", 40, 405, 200, 20);

        departmentField = createTextField("e.g. Computer Applications");
        departmentField.setBounds(40, 428, 215, 35);
        registrationCard.add(departmentField);

        // DESIGNATION
        addLabel(registrationCard, "DESIGNATION", 275, 405, 200, 20);

        designationField = createTextField("e.g. Assistant Professor");
        designationField.setBounds(275, 428, 215, 35);
        registrationCard.add(designationField);

        // REGISTER BUTTON
        JButton registerButton = new JButton("CREATE FACULTY ACCOUNT");
        stylePrimaryButton(registerButton);
        registerButton.setBounds(40, 480, 250, 42);
        registrationCard.add(registerButton);

        // BACK BUTTON
        JButton backButton = new JButton("BACK TO LOGIN");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1)
        );
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBounds(305, 480, 185, 42);
        registrationCard.add(backButton);

        // FOOTER
        JLabel footerLabel = new JLabel(
                "Your account will require Admin approval.",
                SwingConstants.CENTER
        );
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(LIGHT_TEXT);
        footerLabel.setBounds(40, 530, 450, 25);
        registrationCard.add(footerLabel);

        // ACTIONS
        registerButton.addActionListener(e -> registerFaculty());

        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        add(backgroundPanel);

        setSize(1100, 720);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // =========================================================
    // LABEL
    // =========================================================

    private void addLabel(
            JPanel panel, String text,
            int x, int y, int width, int height) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(new Color(65, 85, 95));
        label.setBounds(x, y, width, height);
        panel.add(label);
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createTextField(String tooltip) {

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        field.setToolTipText(tooltip);
        return field;
    }

    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    private JPasswordField createPasswordField() {

        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );
        return field;
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private void stylePrimaryButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // =========================================================
    // REGISTER FACULTY
    // =========================================================

    private void registerFaculty() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword =
                new String(confirmPasswordField.getPassword());
        String department = departmentField.getText().trim();
        String designation = designationField.getText().trim();

        // VALIDATION
        if (name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()
                || department.isEmpty() || designation.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Registration Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 6 characters.",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password and Confirm Password do not match.",
                    "Password Mismatch",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // NEW FACULTY REQUEST IS PENDING UNTIL ADMIN APPROVES IT.
        String sql =
                "INSERT INTO faculty " +
                        "(name, email, password, department, designation, status) " +
                        "VALUES (?, ?, ?, ?, ?, 'PENDING')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, department);
            statement.setString(5, designation);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Faculty registration submitted successfully!\n\n" +
                                "Your account is pending Admin approval.\n" +
                                "You can log in after your account is approved.",
                        "Registration Submitted",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
                new LoginFrame();
            }

        } catch (SQLException e) {

            String message = e.getMessage() == null
                    ? "" : e.getMessage().toLowerCase();

            if (message.contains("duplicate")
                    || message.contains("unique")) {

                JOptionPane.showMessageDialog(
                        this,
                        "This email is already registered.",
                        "Registration Failed",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error:\n" + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // =========================================================
    // BACKGROUND PANEL
    // =========================================================

    private class BackgroundPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (backgroundImage != null) {

                g.drawImage(
                        backgroundImage,
                        0, 0,
                        getWidth(), getHeight(),
                        this
                );

            } else {

                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(235, 247, 249));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        @Override
        public void doLayout() {

            super.doLayout();

            Component[] components = getComponents();

            if (components.length > 0) {

                Component card = components[0];

                int cardWidth = 530;
                int cardHeight = 590;

                int x = getWidth() - cardWidth - 80;
                int y = (getHeight() - cardHeight) / 2;

                if (x < 20) {
                    x = 20;
                }

                if (y < 20) {
                    y = 20;
                }

                card.setBounds(x, y, cardWidth, cardHeight);
            }
        }
    }

    // =========================================================
    // MAIN - DIRECT RUN FOR TESTING
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                FacultyRegistrationFrame::new
        );
    }

}
