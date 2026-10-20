package com.campusconnect.ui.student;

import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditStudentProfileFrame extends JFrame {

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BACKGROUND =
            new Color(244, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color DARK_BLUE =
            new Color(22, 55, 100);

    private static final Color TEAL =
            new Color(25, 160, 175);

    private static final Color BLUE =
            new Color(45, 105, 220);

    private static final Color TEXT_DARK =
            new Color(25, 40, 60);

    private static final Color TEXT_GRAY =
            new Color(95, 110, 130);

    // =====================================================
    // FIELDS
    // =====================================================

    private JTextField nameField;
    private JTextField emailField;
    private JTextField courseField;
    private JTextField semesterField;

    private JTextField enrollmentField;
    private JTextField phoneField;

    private JTextArea bioArea;
    private JTextArea skillsArea;

    private JTextField githubField;
    private JTextField linkedinField;
    private JTextField profilePhotoField;

    private final String studentEmail;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EditStudentProfileFrame(String email) {

        this.studentEmail = email;

        // =================================================
        // FRAME SETTINGS
        // =================================================

        setTitle(
                "CampusConnect - Edit Profile"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(900, 700)
        );

        setLayout(
                new BorderLayout()
        );

        // =================================================
        // MAIN PANEL
        // =================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        // =================================================
        // HEADER
        // =================================================

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                WHITE
        );

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(
                WHITE
        );

        JLabel titleLabel =
                new JLabel(
                        "Edit Student Profile"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        titleLabel.setForeground(
                TEXT_DARK
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Update your personal and academic information."
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitleLabel.setForeground(
                TEXT_GRAY
        );

        titlePanel.add(
                titleLabel
        );

        titlePanel.add(
                Box.createVerticalStrut(6)
        );

        titlePanel.add(
                subtitleLabel
        );

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =================================================
        // FORM PANEL
        // =================================================

        JPanel formContainer =
                new JPanel(
                        new BorderLayout()
                );

        formContainer.setBackground(
                BACKGROUND
        );

        formContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(
                WHITE
        );

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        215,
                                        222,
                                        232
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        7,
                        8,
                        7,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        int row = 0;

        // =================================================
        // BASIC INFORMATION
        // =================================================

        JLabel basicTitle =
                createSectionTitle(
                        "Basic Information"
                );

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;

        formPanel.add(
                basicTitle,
                gbc
        );

        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Full Name"),
                gbc
        );

        nameField =
                createTextField();

        nameField.setEditable(false);

        nameField.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        gbc.gridx = 1;

        formPanel.add(
                nameField,
                gbc
        );

        row++;

        // Email
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Email"),
                gbc
        );

        emailField =
                createTextField();

        emailField.setEditable(false);

        emailField.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        gbc.gridx = 1;

        formPanel.add(
                emailField,
                gbc
        );

        row++;

        // Course
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Course"),
                gbc
        );

        courseField =
                createTextField();

        courseField.setEditable(false);

        courseField.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        gbc.gridx = 1;

        formPanel.add(
                courseField,
                gbc
        );

        row++;

        // Semester
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Semester"),
                gbc
        );

        semesterField =
                createTextField();

        semesterField.setEditable(false);

        semesterField.setBackground(
                new Color(
                        238,
                        241,
                        245
                )
        );

        gbc.gridx = 1;

        formPanel.add(
                semesterField,
                gbc
        );

        row++;

        // =================================================
        // ADDITIONAL INFORMATION
        // =================================================

        JLabel additionalTitle =
                createSectionTitle(
                        "Additional Information"
                );

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;

        formPanel.add(
                additionalTitle,
                gbc
        );

        gbc.gridwidth = 1;

        // Enrollment
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Enrollment No."),
                gbc
        );

        enrollmentField =
                createTextField();

        gbc.gridx = 1;

        formPanel.add(
                enrollmentField,
                gbc
        );

        row++;

        // Phone
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Phone"),
                gbc
        );

        phoneField =
                createTextField();

        gbc.gridx = 1;

        formPanel.add(
                phoneField,
                gbc
        );

        row++;

        // =================================================
        // PROFILE PHOTO
        // =================================================

        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Profile Photo"),
                gbc
        );

        JPanel photoPanel =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        photoPanel.setBackground(
                WHITE
        );

        profilePhotoField =
                createTextField();

        JButton browseButton =
                new JButton(
                        "Browse"
                );

        browseButton.setFocusPainted(
                false
        );

        browseButton.setBackground(
                BLUE
        );

        browseButton.setForeground(
                WHITE
        );

        browseButton.addActionListener(
                e -> chooseProfilePhoto()
        );

        photoPanel.add(
                profilePhotoField,
                BorderLayout.CENTER
        );

        photoPanel.add(
                browseButton,
                BorderLayout.EAST
        );

        gbc.gridx = 1;

        formPanel.add(
                photoPanel,
                gbc
        );

        row++;

        // =================================================
        // BIO
        // =================================================

        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Bio"),
                gbc
        );

        bioArea =
                new JTextArea(
                        3,
                        20
                );

        configureTextArea(
                bioArea
        );

        JScrollPane bioScrollPane =
                new JScrollPane(
                        bioArea
                );

        gbc.gridx = 1;

        formPanel.add(
                bioScrollPane,
                gbc
        );

        row++;

        // =================================================
        // SKILLS
        // =================================================

        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("Skills"),
                gbc
        );

        skillsArea =
                new JTextArea(
                        3,
                        20
                );

        configureTextArea(
                skillsArea
        );

        JScrollPane skillsScrollPane =
                new JScrollPane(
                        skillsArea
                );

        gbc.gridx = 1;

        formPanel.add(
                skillsScrollPane,
                gbc
        );

        row++;

        // =================================================
        // SOCIAL LINKS
        // =================================================

        JLabel socialTitle =
                createSectionTitle(
                        "Social Links"
                );

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;

        formPanel.add(
                socialTitle,
                gbc
        );

        gbc.gridwidth = 1;

        // GitHub
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("GitHub URL"),
                gbc
        );

        githubField =
                createTextField();

        gbc.gridx = 1;

        formPanel.add(
                githubField,
                gbc
        );

        row++;

        // LinkedIn
        gbc.gridx = 0;
        gbc.gridy = row;

        formPanel.add(
                createLabel("LinkedIn URL"),
                gbc
        );

        linkedinField =
                createTextField();

        gbc.gridx = 1;

        formPanel.add(
                linkedinField,
                gbc
        );

        // =================================================
        // SCROLL FORM
        // =================================================

        JScrollPane formScrollPane =
                new JScrollPane(
                        formPanel
                );

        formScrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        formScrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        formContainer.add(
                formScrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                formContainer,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                15
                        )
                );

        buttonPanel.setBackground(
                WHITE
        );

        JButton cancelButton =
                createButton(
                        "Cancel",
                        TEXT_GRAY
                );

        JButton saveButton =
                createButton(
                        "Save Changes",
                        TEAL
                );

        cancelButton.addActionListener(
                e -> dispose()
        );

        saveButton.addActionListener(
                e -> saveProfile()
        );

        buttonPanel.add(
                cancelButton
        );

        buttonPanel.add(
                saveButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =================================================
        // ADD MAIN PANEL
        // =================================================

        add(
                mainPanel
        );

        // =================================================
        // LOAD DATA
        // =================================================

        loadStudentData();

        // =================================================
        // WINDOW
        // =================================================

        setSize(
                1000,
                750
        );

        setLocationRelativeTo(
                null
        );

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setVisible(
                true
        );
    }

    // =====================================================
    // CREATE LABEL
    // =====================================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                TEXT_DARK
        );

        return label;
    }

    // =====================================================
    // CREATE SECTION TITLE
    // =====================================================

    private JLabel createSectionTitle(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        label.setForeground(
                DARK_BLUE
        );

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        0,
                        5,
                        0
                )
        );

        return label;
    }

    // =====================================================
    // CREATE TEXT FIELD
    // =====================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        field.setPreferredSize(
                new Dimension(
                        350,
                        35
                )
        );

        return field;
    }

    // =====================================================
    // CONFIGURE TEXT AREA
    // =====================================================

    private void configureTextArea(
            JTextArea area
    ) {

        area.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        area.setLineWrap(
                true
        );

        area.setWrapStyleWord(
                true
        );
    }

    // =====================================================
    // CREATE BUTTON
    // =====================================================

    private JButton createButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                background
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        22,
                        10,
                        22
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =====================================================
    // LOAD STUDENT DATA
    // =====================================================

    private void loadStudentData() {

        String sql =
                "SELECT name, email, course, semester, " +
                        "phone, enrollment_no, profile_photo, " +
                        "bio, skills_summary, github_url, linkedin_url " +
                        "FROM students " +
                        "WHERE email = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    studentEmail
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    nameField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "name"
                                    )
                            )
                    );

                    emailField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "email"
                                    )
                            )
                    );

                    courseField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "course"
                                    )
                            )
                    );

                    int semester =
                            resultSet.getInt(
                                    "semester"
                            );

                    if (resultSet.wasNull()) {

                        semesterField.setText("");

                    } else {

                        semesterField.setText(
                                String.valueOf(
                                        semester
                                )
                        );
                    }

                    enrollmentField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "enrollment_no"
                                    )
                            )
                    );

                    phoneField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "phone"
                                    )
                            )
                    );

                    profilePhotoField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "profile_photo"
                                    )
                            )
                    );

                    bioArea.setText(
                            safeValue(
                                    resultSet.getString(
                                            "bio"
                                    )
                            )
                    );

                    skillsArea.setText(
                            safeValue(
                                    resultSet.getString(
                                            "skills_summary"
                                    )
                            )
                    );

                    githubField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "github_url"
                                    )
                            )
                    );

                    linkedinField.setText(
                            safeValue(
                                    resultSet.getString(
                                            "linkedin_url"
                                    )
                            )
                    );
                }
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
    // SAFE VALUE
    // =====================================================

    private String safeValue(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    // =====================================================
    // CHOOSE PROFILE PHOTO
    // =====================================================

    private void chooseProfilePhoto() {

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Select Profile Photo"
        );

        int result =
                fileChooser.showOpenDialog(
                        this
                );

        if (
                result ==
                        JFileChooser.APPROVE_OPTION
        ) {

            File selectedFile =
                    fileChooser.getSelectedFile();

            profilePhotoField.setText(
                    selectedFile.getAbsolutePath()
            );
        }
    }

    // =====================================================
    // SAVE PROFILE
    // =====================================================

    private void saveProfile() {

        String enrollment =
                enrollmentField
                        .getText()
                        .trim();

        String phone =
                phoneField
                        .getText()
                        .trim();

        String photo =
                profilePhotoField
                        .getText()
                        .trim();

        String bio =
                bioArea
                        .getText()
                        .trim();

        String skills =
                skillsArea
                        .getText()
                        .trim();

        String github =
                githubField
                        .getText()
                        .trim();

        String linkedin =
                linkedinField
                        .getText()
                        .trim();

        // =================================================
        // VALIDATION
        // =================================================

        if (
                enrollment.length() > 50
                        ||
                        phone.length() > 20
                        ||
                        photo.length() > 255
                        ||
                        bio.length() > 500
                        ||
                        skills.length() > 500
                        ||
                        github.length() > 255
                        ||
                        linkedin.length() > 255
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Some information is too long.\n" +
                            "Please check the field limits.",
                    "Invalid Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =================================================
        // UPDATE QUERY
        // =================================================

        String sql =
                "UPDATE students SET " +
                        "enrollment_no = ?, " +
                        "phone = ?, " +
                        "profile_photo = ?, " +
                        "bio = ?, " +
                        "skills_summary = ?, " +
                        "github_url = ?, " +
                        "linkedin_url = ? " +
                        "WHERE email = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    emptyToNull(enrollment)
            );

            statement.setString(
                    2,
                    emptyToNull(phone)
            );

            statement.setString(
                    3,
                    emptyToNull(photo)
            );

            statement.setString(
                    4,
                    emptyToNull(bio)
            );

            statement.setString(
                    5,
                    emptyToNull(skills)
            );

            statement.setString(
                    6,
                    emptyToNull(github)
            );

            statement.setString(
                    7,
                    emptyToNull(linkedin)
            );

            statement.setString(
                    8,
                    studentEmail
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Profile updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Profile could not be updated.",
                        "Update Failed",
                        JOptionPane.WARNING_MESSAGE
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
    // EMPTY STRING TO NULL
    // =====================================================

    private String emptyToNull(
            String value
    ) {

        return value.isEmpty()
                ? null
                : value;
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new EditStudentProfileFrame(
                        "aditya@gmail.com"
                )
        );
    }
}
