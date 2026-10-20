package com.campusconnect.ui.student;

import com.campusconnect.ui.login.LoginFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.time.Duration;

public class StudentRegistrationFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private JComboBox<String> courseComboBox;
    private JComboBox<String> semesterComboBox;

    private BufferedImage backgroundImage;
    private BufferedImage logoImage;

    // =========================
    // COLORS
    // =========================

    private final Color PRIMARY_COLOR =
            new Color(20, 150, 160);

    private final Color PRIMARY_DARK =
            new Color(15, 115, 125);

    private final Color DARK_TEXT =
            new Color(30, 45, 55);

    private final Color LIGHT_TEXT =
            new Color(100, 115, 125);

    private final Color BORDER_COLOR =
            new Color(205, 220, 225);

    public StudentRegistrationFrame() {

        // =========================
        // FRAME SETTINGS
        // =========================

        setTitle("CampusConnect - Student Registration");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(900, 650)
        );

        setResizable(true);

        // =========================
        // LOAD BACKGROUND
        // =========================

        try {

            backgroundImage =
                    ImageIO.read(
                            getClass().getResource(
                                    "/campusconnect_background.png"
                            )
                    );

        } catch (Exception e) {

            System.out.println(
                    "Background image not found."
            );
        }

        // =========================
        // LOAD LOGO
        // =========================

        try {

            logoImage =
                    ImageIO.read(
                            getClass().getResource(
                                    "/campusconnect_logo.png"
                            )
                    );

        } catch (Exception e) {

            System.out.println(
                    "Logo image not found."
            );
        }

        // =========================
        // BACKGROUND PANEL
        // =========================

        BackgroundPanel backgroundPanel =
                new BackgroundPanel();

        backgroundPanel.setLayout(null);

        // =========================
        // REGISTRATION CARD
        // =========================

        JPanel registrationCard =
                new JPanel();

        registrationCard.setLayout(null);

        registrationCard.setBackground(
                new Color(255, 255, 255, 248)
        );

        registrationCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 235),
                                1
                        ),
                        new EmptyBorder(
                                10, 10, 10, 10
                        )
                )
        );

        backgroundPanel.add(
                registrationCard
        );

        // =====================================================
        // LOGO
        // =====================================================

        JLabel logoLabel;

        if (logoImage != null) {

            Image scaledLogo =
                    logoImage.getScaledInstance(
                            130,
                            85,
                            Image.SCALE_SMOOTH
                    );

            logoLabel =
                    new JLabel(
                            new ImageIcon(scaledLogo)
                    );

        } else {

            logoLabel =
                    new JLabel(
                            "CampusConnect",
                            SwingConstants.CENTER
                    );

            logoLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            22
                    )
            );

            logoLabel.setForeground(
                    PRIMARY_COLOR
            );
        }

        logoLabel.setBounds(
                145,
                20,
                230,
                85
        );

        registrationCard.add(
                logoLabel
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel(
                        "Create Your Account",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        titleLabel.setForeground(
                DARK_TEXT
        );

        titleLabel.setBounds(
                50,
                105,
                420,
                35
        );

        registrationCard.add(
                titleLabel
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitleLabel =
                new JLabel(
                        "Join CampusConnect and start your journey",
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
                50,
                140,
                420,
                25
        );

        registrationCard.add(
                subtitleLabel
        );

        // =====================================================
        // NAME
        // =====================================================

        JLabel nameLabel =
                createLabel("FULL NAME");

        nameLabel.setBounds(
                50,
                180,
                180,
                22
        );

        registrationCard.add(
                nameLabel
        );

        nameField =
                createTextField(
                        "Enter your full name"
                );

        nameField.setBounds(
                50,
                205,
                420,
                38
        );

        registrationCard.add(
                nameField
        );

        // =====================================================
        // EMAIL
        // =====================================================

        JLabel emailLabel =
                createLabel("EMAIL ADDRESS");

        emailLabel.setBounds(
                50,
                255,
                180,
                22
        );

        registrationCard.add(
                emailLabel
        );

        emailField =
                createTextField(
                        "Enter your email"
                );

        emailField.setBounds(
                50,
                280,
                420,
                38
        );

        registrationCard.add(
                emailField
        );

        // =====================================================
        // PASSWORD
        // =====================================================

        JLabel passwordLabel =
                createLabel("PASSWORD");

        passwordLabel.setBounds(
                50,
                330,
                180,
                22
        );

        registrationCard.add(
                passwordLabel
        );

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        passwordField.setBorder(
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

        passwordField.setToolTipText(
                "Enter your password"
        );

        passwordField.setBounds(
                50,
                355,
                420,
                38
        );

        registrationCard.add(
                passwordField
        );

        // =====================================================
        // COURSE
        // =====================================================

        JLabel courseLabel =
                createLabel("COURSE");

        courseLabel.setBounds(
                50,
                405,
                180,
                22
        );

        registrationCard.add(
                courseLabel
        );

        courseComboBox =
                new JComboBox<>(
                        new String[]{
                                "Select Course",
                                "BCA",
                                "B.Tech",
                                "BBA",
                                "B.Com",
                                "MCA",
                                "MBA",
                                "Other"
                        }
                );

        styleComboBox(
                courseComboBox
        );

        courseComboBox.setBounds(
                50,
                430,
                200,
                38
        );

        registrationCard.add(
                courseComboBox
        );

        // =====================================================
        // SEMESTER
        // =====================================================

        JLabel semesterLabel =
                createLabel("SEMESTER");

        semesterLabel.setBounds(
                270,
                405,
                180,
                22
        );

        registrationCard.add(
                semesterLabel
        );

        semesterComboBox =
                new JComboBox<>(
                        new String[]{
                                "Select Semester",
                                "Semester 1",
                                "Semester 2",
                                "Semester 3",
                                "Semester 4",
                                "Semester 5",
                                "Semester 6",
                                "Semester 7",
                                "Semester 8"
                        }
                );

        styleComboBox(
                semesterComboBox
        );

        semesterComboBox.setBounds(
                270,
                430,
                200,
                38
        );

        registrationCard.add(
                semesterComboBox
        );

        // =====================================================
        // REGISTER BUTTON
        // =====================================================

        JButton registerButton =
                new JButton("CREATE ACCOUNT");

        registerButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        registerButton.setForeground(
                Color.WHITE
        );

        registerButton.setBackground(
                PRIMARY_COLOR
        );

        registerButton.setFocusPainted(false);

        registerButton.setBorderPainted(false);

        registerButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        registerButton.setBounds(
                50,
                485,
                200,
                42
        );

        registrationCard.add(
                registerButton
        );

        // =====================================================
        // BACK BUTTON
        // =====================================================

        JButton backButton =
                new JButton("BACK TO LOGIN");

        backButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        backButton.setForeground(
                PRIMARY_COLOR
        );

        backButton.setBackground(
                Color.WHITE
        );

        backButton.setFocusPainted(false);

        backButton.setBorder(
                BorderFactory.createLineBorder(
                        PRIMARY_COLOR,
                        1
                )
        );

        backButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        backButton.setBounds(
                270,
                485,
                200,
                42
        );

        registrationCard.add(
                backButton
        );

        // =====================================================
        // FOOTER TEXT
        // =====================================================

        JLabel footerLabel =
                new JLabel(
                        "Already have an account? Login to continue.",
                        SwingConstants.CENTER
                );

        footerLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        footerLabel.setForeground(
                LIGHT_TEXT
        );

        footerLabel.setBounds(
                50,
                535,
                420,
                22
        );

        registrationCard.add(
                footerLabel
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        registerButton.addActionListener(
                e -> registerStudent()
        );

        backButton.addActionListener(e -> {

            dispose();

            new LoginFrame();
        });

        // =====================================================
        // ADD BACKGROUND
        // =====================================================

        add(backgroundPanel);

        // =====================================================
        // WINDOW
        // =====================================================

        setSize(
                1100,
                700
        );

        setLocationRelativeTo(null);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setVisible(true);
    }

    // =====================================================
    // CREATE LABEL
    // =====================================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(
                new Color(65, 85, 95)
        );

        return label;
    }

    // =====================================================
    // CREATE TEXT FIELD
    // =====================================================

    private JTextField createTextField(
            String tooltip
    ) {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

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

        field.setToolTipText(
                tooltip
        );

        return field;
    }

    // =====================================================
    // COMBO BOX STYLE
    // =====================================================

    private void styleComboBox(
            JComboBox<String> comboBox
    ) {

        comboBox.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        comboBox.setBackground(
                Color.WHITE
        );

        comboBox.setForeground(
                DARK_TEXT
        );
    }

    // =====================================================
    // REGISTER STUDENT
    // =====================================================

    private void registerStudent() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        String password = new String(passwordField.getPassword());

        String course = courseComboBox.getSelectedItem().toString();
        String selectedSemester =
                semesterComboBox.getSelectedItem().toString();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Registration Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validate course and semester selection
        if (course.equals("Select Course")
                || selectedSemester.equals("Select Semester")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select your course and semester.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validate email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Backend requires a password of at least 8 characters
        if (password.length() < 8 || password.length() > 72) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain 8 to 72 characters.",
                    "Invalid Password",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Convert "Semester 3" to integer 3
        int semester;

        try {
            semester = Integer.parseInt(
                    selectedSemester.replace("Semester ", "").trim()
            );
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a valid semester.",
                    "Invalid Semester",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Build JSON request for Spring Boot
        String json = "{"
                + "\"name\":\"" + escapeJson(name) + "\","
                + "\"email\":\"" + escapeJson(email) + "\","
                + "\"password\":\"" + escapeJson(password) + "\","
                + "\"course\":\"" + escapeJson(course) + "\","
                + "\"semester\":" + semester
                + "}";

        // Send registration request to the backend
        try {

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://campusconnect-backend-grn0.onrender.com/api/students/register"
                    ))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            int statusCode = response.statusCode();

            if (statusCode == 201) {

                JOptionPane.showMessageDialog(
                        this,
                        "Account created successfully!\n\n"
                                + "Welcome to CampusConnect, " + name + "!",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");

                dispose();
                new LoginFrame();

            } else if (statusCode == 409) {

                JOptionPane.showMessageDialog(
                        this,
                        "This email or enrollment number is already registered.",
                        "Registration Failed",
                        JOptionPane.WARNING_MESSAGE
                );

            } else if (statusCode == 400) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration details are invalid.\n"
                                + "Please check your information and try again.\n\n"
                                + response.body(),
                        "Invalid Registration Details",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. HTTP status: " + statusCode
                                + "\n" + response.body(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IOException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to the CampusConnect backend.\n"
                            + "Please make sure the backend is running.\n\n"
                            + exception.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration request was interrupted.",
                    "Request Interrupted",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Escape special characters before placing text inside JSON
    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }



    private class BackgroundPanel
            extends JPanel {

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            if (backgroundImage != null) {

                g.drawImage(
                        backgroundImage,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );

            } else {

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setColor(
                        new Color(
                                235,
                                247,
                                249
                        )
                );

                g2.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );
            }
        }

        @Override
        public void doLayout() {

            super.doLayout();

            Component[] components =
                    getComponents();

            if (components.length > 0) {

                Component card =
                        components[0];

                // ==========================================
                // REGISTRATION CARD SIZE
                // ==========================================

                int cardWidth = 520;
                int cardHeight = 590;

                // ==========================================
                // RIGHT SIDE POSITION
                // ==========================================

                int rightMargin = 80;

                int x =
                        getWidth()
                                - cardWidth
                                - rightMargin;

                int y =
                        (getHeight()
                                - cardHeight) / 2;

                // ==========================================
                // KEEP CARD INSIDE WINDOW
                // ==========================================

                if (x < 20) {
                    x = 20;
                }

                if (y < 20) {
                    y = 20;
                }

                card.setBounds(
                        x,
                        y,
                        cardWidth,
                        cardHeight
                );
            }
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                StudentRegistrationFrame::new
        );
    }
}
