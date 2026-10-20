package com.campusconnect.ui.login;

import com.campusconnect.ui.admin.AdminDashboardFrame;
import com.campusconnect.ui.student.StudentRegistrationFrame;
import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.ui.student.DashboardFrame;
import com.campusconnect.ui.faculty.FacultyDashboardFrame;
import com.campusconnect.ui.faculty.FacultyRegistrationFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private BufferedImage backgroundImage;
    private JLabel logoLabel;

    private JButton studentTab;
    private JButton adminTab;
    private JButton facultyTab;
    private JButton loginButton;
    private JButton registerButton;

    private boolean adminMode = false;
    private boolean facultyMode = false;

    // =========================
    // COLORS
    // =========================

    private final Color PRIMARY_COLOR =
            new Color(20, 150, 160);

    private final Color DARK_BLUE =
            new Color(20, 55, 90);

    private final Color LIGHT_TEXT =
            new Color(100, 115, 125);

    private final Color BORDER_COLOR =
            new Color(195, 215, 220);

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LoginFrame() {

        setTitle("CampusConnect - Login");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(900, 600)
        );

        setResizable(true);

        // =================================================
        // LOAD BACKGROUND
        // =================================================

        try {

            backgroundImage =
                    ImageIO.read(
                            getClass().getResource(
                                    "/campusconnect_background.png"
                            )
                    );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Background image could not be loaded.\n\n"
                            + "Make sure this file exists:\n"
                            + "src/main/resources/campusconnect_background.png",
                    "Image Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // =================================================
        // BACKGROUND PANEL
        // =================================================

        BackgroundPanel backgroundPanel =
                new BackgroundPanel();

        backgroundPanel.setLayout(null);

        // =================================================
        // LOGIN CARD
        // =================================================

        JPanel loginCard =
                new JPanel();

        loginCard.setLayout(null);

        loginCard.setBackground(
                new Color(255, 255, 255, 245)
        );

        loginCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 225, 230),
                                1
                        ),
                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        // =================================================
        // LOGO
        // =================================================

        logoLabel =
                new JLabel();

        logoLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        logoLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        logoLabel.setBounds(
                125,
                20,
                170,
                100
        );

        loadLogo();

        loginCard.add(logoLabel);

        // =================================================
        // TITLE
        // =================================================

        JLabel titleLabel =
                new JLabel(
                        "CampusConnect",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        titleLabel.setForeground(
                DARK_BLUE
        );

        titleLabel.setBounds(
                35,
                115,
                350,
                40
        );

        loginCard.add(titleLabel);

        // =================================================
        // SUBTITLE
        // =================================================

        JLabel subtitleLabel =
                new JLabel(
                        "Welcome back! Please login to your account.",
                        SwingConstants.CENTER
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitleLabel.setForeground(
                LIGHT_TEXT
        );

        subtitleLabel.setBounds(
                25,
                153,
                370,
                25
        );

        loginCard.add(subtitleLabel);

        // =================================================
        // STUDENT TAB
        // =================================================

        studentTab =
                new JButton("STUDENT");

        studentTab.setBounds(
                25,
                190,
                120,
                38
        );

        styleTabButton(
                studentTab,
                true
        );

        loginCard.add(studentTab);

        // =================================================
        // ADMIN TAB
        // =================================================

        adminTab =
                new JButton("ADMIN");

        adminTab.setBounds(
                150,
                190,
                120,
                38
        );

        styleTabButton(
                adminTab,
                false
        );

        loginCard.add(adminTab);

        // =================================================
        // FACULTY TAB
        // =================================================

        facultyTab = new JButton("FACULTY");

        facultyTab.setBounds(
                275,
                190,
                120,
                38
        );

        styleTabButton(
                facultyTab,
                false
        );

        loginCard.add(facultyTab);

        // =================================================
        // EMAIL LABEL
        // =================================================

        JLabel emailLabel =
                new JLabel("EMAIL");

        emailLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        emailLabel.setForeground(
                new Color(65, 85, 95)
        );

        emailLabel.setBounds(
                45,
                245,
                100,
                25
        );

        loginCard.add(emailLabel);

        // =================================================
        // EMAIL FIELD
        // =================================================

        emailField =
                new JTextField();

        emailField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        emailField.setBounds(
                45,
                270,
                330,
                38
        );

        styleTextField(emailField);

        loginCard.add(emailField);

        // =================================================
        // PASSWORD LABEL
        // =================================================

        JLabel passwordLabel =
                new JLabel("PASSWORD");

        passwordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        passwordLabel.setForeground(
                new Color(65, 85, 95)
        );

        passwordLabel.setBounds(
                45,
                320,
                120,
                25
        );

        loginCard.add(passwordLabel);

        // =================================================
        // PASSWORD FIELD
        // =================================================

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        passwordField.setBounds(
                45,
                345,
                330,
                38
        );

        styleTextField(passwordField);

        loginCard.add(passwordField);

        // =================================================
        // LOGIN BUTTON
        // =================================================

        loginButton =
                new JButton("LOGIN");

        loginButton.setBounds(
                45,
                400,
                330,
                42
        );

        stylePrimaryButton(loginButton);

        loginCard.add(loginButton);

        // =================================================
        // REGISTER TEXT
        // =================================================

        JLabel registerText =
                new JLabel(
                        "Don't have an account?",
                        SwingConstants.CENTER
                );

        registerText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        registerText.setForeground(
                LIGHT_TEXT
        );

        registerText.setBounds(
                45,
                455,
                165,
                25
        );

        loginCard.add(registerText);

        // =================================================
        // REGISTER BUTTON
        // =================================================

        registerButton =
                new JButton("REGISTER");

        registerButton.setBounds(
                210,
                450,
                165,
                34
        );

        styleOutlineButton(registerButton);

        loginCard.add(registerButton);

        // =================================================
        // BUTTON ACTIONS
        // =================================================

        studentTab.addActionListener(e -> {

            adminMode = false;
            facultyMode = false;

            updateLoginMode();

        });

        adminTab.addActionListener(e -> {

            adminMode = true;
            facultyMode = false;

            updateLoginMode();

        });

        facultyTab.addActionListener(e -> {

            adminMode = false;
            facultyMode = true;

            updateLoginMode();

        });

        loginButton.addActionListener(
                e -> loginUser()
        );

        registerButton.addActionListener(e -> {

            if (facultyMode) {
                dispose();
                new FacultyRegistrationFrame();
            } else if (!adminMode) {
                dispose();
                new StudentRegistrationFrame();
            }

        });

        passwordField.addActionListener(
                e -> loginUser()
        );

        // =================================================
        // ADD LOGIN CARD
        // =================================================

        backgroundPanel.add(loginCard);

        add(backgroundPanel);

        // =================================================
        // WINDOW
        // =================================================

        setSize(
                1200,
                750
        );

        setLocationRelativeTo(null);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setVisible(true);
    }

    // =====================================================
    // LOAD LOGO
    // =====================================================

    private void loadLogo() {

        try {

            BufferedImage logoImage =
                    ImageIO.read(
                            getClass().getResource(
                                    "/campusconnect_logo.png"
                            )
                    );

            Image scaledLogo =
                    logoImage.getScaledInstance(
                            150,
                            90,
                            Image.SCALE_SMOOTH
                    );

            logoLabel.setIcon(
                    new ImageIcon(scaledLogo)
            );

        } catch (Exception e) {

            logoLabel.setText(
                    "CampusConnect"
            );

            logoLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            logoLabel.setForeground(
                    PRIMARY_COLOR
            );
        }
    }

    // =====================================================
    // UPDATE LOGIN MODE
    // =====================================================

    private void updateLoginMode() {

        styleTabButton(studentTab, !adminMode && !facultyMode);
        styleTabButton(adminTab, adminMode);
        styleTabButton(facultyTab, facultyMode);

        if (adminMode) {
            loginButton.setText("ADMIN LOGIN");
            registerButton.setVisible(false);
        } else if (facultyMode) {
            loginButton.setText("FACULTY LOGIN");
            registerButton.setVisible(true);
            registerButton.setText("FACULTY REGISTER");
        } else {
            loginButton.setText("LOGIN");
            registerButton.setVisible(true);
            registerButton.setText("REGISTER");
        }

        emailField.setText("");
        passwordField.setText("");
        emailField.requestFocus();
    }

    // =====================================================
    // TAB BUTTON STYLE
    // =====================================================

    private void styleTabButton(
            JButton button,
            boolean selected
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        if (selected) {

            button.setBackground(
                    PRIMARY_COLOR
            );

            button.setForeground(
                    Color.WHITE
            );

            button.setBorder(
                    BorderFactory.createLineBorder(
                            PRIMARY_COLOR
                    )
            );

        } else {

            button.setBackground(
                    Color.WHITE
            );

            button.setForeground(
                    PRIMARY_COLOR
            );

            button.setBorder(
                    BorderFactory.createLineBorder(
                            PRIMARY_COLOR
                    )
            );
        }
    }

    // =====================================================
    // TEXT FIELD STYLE
    // =====================================================

    private void styleTextField(
            JComponent field
    ) {

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );
    }

    // =====================================================
    // PRIMARY BUTTON STYLE
    // =====================================================

    private void stylePrimaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                PRIMARY_COLOR
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);
    }

    // =====================================================
    // OUTLINE BUTTON STYLE
    // =====================================================

    private void styleOutlineButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                PRIMARY_COLOR
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        PRIMARY_COLOR,
                        1
                )
        );
    }

    // =====================================================
    // BACKGROUND PANEL
    // =====================================================

    private class BackgroundPanel
            extends JPanel {

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            if (backgroundImage != null) {

                int width =
                        getWidth();

                int height =
                        getHeight();

                g.drawImage(
                        backgroundImage,
                        0,
                        0,
                        width,
                        height,
                        this
                );
            }
        }

        @Override
        public void doLayout() {

            super.doLayout();

            Component[] components =
                    getComponents();

            if (components.length > 0) {

                Component loginCard =
                        components[0];

                int cardWidth = 420;

                int cardHeight = 525;

                int x =
                        getWidth()
                                - cardWidth
                                - 100;

                int y =
                        (getHeight()
                                - cardHeight) / 2;

                if (x < 50) {
                    x = 50;
                }

                if (y < 20) {
                    y = 20;
                }

                loginCard.setBounds(
                        x,
                        y,
                        cardWidth,
                        cardHeight
                );
            }
        }
    }

    // =====================================================
    // LOGIN USER
    // =====================================================

    private void loginUser() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // =================================================
        // VALIDATION
        // =================================================

        if (
                email.isEmpty()
                        || password.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =================================================
        // ADMIN LOGIN
        // =================================================

        if (adminMode) {

            adminLogin(
                    email,
                    password
            );

            return;
        }

        if (facultyMode) {
            facultyLogin(email, password);
            return;
        }

        // =================================================
        // STUDENT LOGIN (BACKEND API)
        // =================================================

        try {
            String requestBody = "{\"email\":\"" + escapeJson(email)
                    + "\",\"password\":\"" + escapeJson(password) + "\"}";

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://campusconnect-backend-grn0.onrender.com/api/students/login"))                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            String responseBody = response.body();

            if (response.statusCode() == 200) {
                String studentName = extractJsonString(responseBody, "name");
                String studentEmail = extractJsonString(responseBody, "email");

                if (studentName == null || studentEmail == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Login response from server was not in the expected format.",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!\n\nWelcome, " + studentName + "!",
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                DashboardFrame dashboard = new DashboardFrame(studentEmail);
                dashboard.setLocationRelativeTo(null);
                dashboard.setVisible(true);

            } else {
                String errorMessage = extractJsonString(responseBody, "detail");
                if (errorMessage == null) {
                    errorMessage = extractJsonString(responseBody, "message");
                }
                if (errorMessage == null || errorMessage.isBlank()) {
                    errorMessage = "Invalid student email or password.";
                }

                JOptionPane.showMessageDialog(
                        this,
                        errorMessage,
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                this,
                "Could not connect to CampusConnect backend.\n"
                        + "Please check your internet connection and "
                        + "make sure the Render backend is available.\n\n"
                        + "Details: " + e.getMessage(),
                "Connection Error",
                JOptionPane.ERROR_MESSAGE
            );

    } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(
                    this,
                    "Login request was interrupted. Please try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "The login API address is invalid.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Escapes special characters before putting values into a JSON request.
    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Reads a JSON string field without requiring an additional JSON library.
    private String extractJsonString(String json, String key) {
        if (json == null || key == null) {
            return null;
        }

        String marker = "\"" + key + "\"";
        int keyIndex = json.indexOf(marker);
        if (keyIndex < 0) {
            return null;
        }

        int colonIndex = json.indexOf(':', keyIndex + marker.length());
        if (colonIndex < 0) {
            return null;
        }

        int index = colonIndex + 1;
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
        if (index >= json.length() || json.charAt(index) != '\"') {
            return null;
        }

        index++;
        StringBuilder value = new StringBuilder();
        boolean escaped = false;

        while (index < json.length()) {
            char current = json.charAt(index++);

            if (escaped) {
                switch (current) {
                    case '\"' -> value.append('\"');
                    case '\\' -> value.append('\\');
                    case '/' -> value.append('/');
                    case 'b' -> value.append('\b');
                    case 'f' -> value.append('\f');
                    case 'n' -> value.append('\n');
                    case 'r' -> value.append('\r');
                    case 't' -> value.append('\t');
                    case 'u' -> {
                        if (index + 4 <= json.length()) {
                            try {
                                value.append((char) Integer.parseInt(json.substring(index, index + 4), 16));
                                index += 4;
                            } catch (NumberFormatException ex) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                    default -> value.append(current);
                }
                escaped = false;
            } else if (current == '\\') {
                escaped = true;
            } else if (current == '\"') {
                return value.toString();
            } else {
                value.append(current);
            }
        }

        return null;
    }

    // =====================================================
    // ADMIN LOGIN
    // =====================================================

    private void adminLogin(
            String email,
            String password
    ) {

        String sql =
                "SELECT admin_id, name FROM admins " +
                        "WHERE email = ? AND password = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    email
            );

            statement.setString(
                    2,
                    password
            );

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                String adminName =
                        resultSet.getString(
                                "name"
                        );

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, "
                                + adminName
                                + "!",
                        "Admin Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                new AdminDashboardFrame(
                        adminName
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid admin email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // FACULTY LOGIN
    // =====================================================

    private void facultyLogin(String email, String password) {

        String sql =
                "SELECT name, status FROM faculty " +
                        "WHERE email = ? AND password = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid faculty email or password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                String facultyName = resultSet.getString("name");
                String status = resultSet.getString("status");

                if ("PENDING".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Your faculty account is awaiting admin approval.",
                            "Approval Pending",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                if (!"APPROVED".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Your faculty account is not approved or is inactive.",
                            "Access Denied",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, " + facultyName + "!",
                        "Faculty Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
                new FacultyDashboardFrame(facultyName, email);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                LoginFrame::new
        );
    }
}
