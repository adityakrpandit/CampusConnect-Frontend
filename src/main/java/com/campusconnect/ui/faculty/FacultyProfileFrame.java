package com.campusconnect.ui.faculty;

import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FacultyProfileFrame extends JPanel {

    private final String facultyEmail;
    private int facultyId;
    private String photoPath;
    private String originalPhotoPath;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField departmentField;
    private JTextField designationField;
    private JLabel facultyIdValue;
    private JLabel statusValue;
    private JLabel joinedValue;
    private JLabel photoLabel;
    private JLabel profileNameLabel;
    private JLabel profileDesignationLabel;
    private JLabel pageMessage;
    private JButton editButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton photoButton;
    private boolean editing;

    private String originalName;
    private String originalDepartment;
    private String originalDesignation;

    private final Color NAVY = new Color(19, 57, 96);
    private final Color NAVY_LIGHT = new Color(31, 76, 118);
    private final Color BACKGROUND = new Color(243, 247, 251);
    private final Color TEAL = new Color(20, 150, 160);
    private final Color TEXT = new Color(58, 77, 96);
    private final Color MUTED = new Color(112, 130, 148);
    private final Color BORDER = new Color(224, 232, 239);
    private final Color WHITE = Color.WHITE;

    public FacultyProfileFrame(String facultyEmail) {
        this.facultyEmail = facultyEmail == null ? "" : facultyEmail.trim();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        initializeUI();
        loadFacultyProfile();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(cardBorder(20, 24, 20, 24));
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("My Faculty Profile");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Manage your personal and professional information");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(7));
        titleBox.add(subtitle);
        JLabel secureBadge = new JLabel("●  FACULTY ACCOUNT");
        secureBadge.setOpaque(true);
        secureBadge.setBackground(new Color(224, 246, 246));
        secureBadge.setForeground(new Color(13, 119, 129));
        secureBadge.setFont(new Font("Arial", Font.BOLD, 11));
        secureBadge.setBorder(new EmptyBorder(10, 12, 10, 12));
        header.add(titleBox, BorderLayout.CENTER);
        header.add(secureBadge, BorderLayout.EAST);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel profileCard = new JPanel(new BorderLayout(22, 10));
        profileCard.setBackground(WHITE);
        profileCard.setBorder(cardBorder(24, 26, 24, 26));
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        photoLabel = new JLabel("F", SwingConstants.CENTER);
        photoLabel.setOpaque(true);
        photoLabel.setBackground(new Color(224, 246, 246));
        photoLabel.setForeground(TEAL);
        photoLabel.setFont(new Font("Arial", Font.BOLD, 34));
        photoLabel.setPreferredSize(new Dimension(112, 112));
        photoLabel.setBorder(BorderFactory.createLineBorder(new Color(207, 232, 235), 1));

        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        profileNameLabel = new JLabel("Faculty Member");
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        profileNameLabel.setForeground(NAVY);
        profileDesignationLabel = new JLabel("Faculty • CampusConnect");
        profileDesignationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        profileDesignationLabel.setForeground(MUTED);
        JLabel emailHint = new JLabel(facultyEmail);
        emailHint.setFont(new Font("Arial", Font.PLAIN, 13));
        emailHint.setForeground(TEXT);
        photoButton = createButton("Change Photo", NAVY_LIGHT);
        photoButton.setPreferredSize(new Dimension(145, 36));
        photoButton.setMaximumSize(new Dimension(145, 36));
        photoButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        photoButton.addActionListener(e -> choosePhoto());
        identity.add(profileNameLabel);
        identity.add(Box.createVerticalStrut(7));
        identity.add(profileDesignationLabel);
        identity.add(Box.createVerticalStrut(7));
        identity.add(emailHint);
        identity.add(Box.createVerticalStrut(14));
        identity.add(photoButton);
        profileCard.add(photoLabel, BorderLayout.WEST);
        profileCard.add(identity, BorderLayout.CENTER);

        JPanel detailsCard = new JPanel(new BorderLayout(0, 18));
        detailsCard.setBackground(WHITE);
        detailsCard.setBorder(cardBorder(24, 26, 22, 26));
        detailsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel detailsTitle = new JLabel("Personal & Professional Details");
        detailsTitle.setFont(new Font("Arial", Font.BOLD, 19));
        detailsTitle.setForeground(NAVY);

        JPanel fields = new JPanel(new GridLayout(0, 2, 22, 18));
        fields.setOpaque(false);
        facultyIdValue = createValueLabel("Loading...");
        statusValue = createValueLabel("Loading...");
        joinedValue = createValueLabel("Loading...");
        nameField = createTextField();
        emailField = createTextField();
        departmentField = createTextField();
        designationField = createTextField();
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 244, 248));

        fields.add(createFieldPanel("FACULTY ID", facultyIdValue));
        fields.add(createFieldPanel("ACCOUNT STATUS", statusValue));
        fields.add(createFieldPanel("FULL NAME", nameField));
        fields.add(createFieldPanel("EMAIL ADDRESS", emailField));
        fields.add(createFieldPanel("DEPARTMENT / FACULTY", departmentField));
        fields.add(createFieldPanel("DESIGNATION", designationField));
        fields.add(createFieldPanel("REGISTRATION DATE", joinedValue));
        fields.add(createInfoPanel("PROFILE PHOTO", "Upload a clear professional photo using Change Photo."));

        pageMessage = new JLabel(" ");
        pageMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        pageMessage.setForeground(MUTED);
        JPanel actions = new JPanel(new BorderLayout(10, 10));
        actions.setOpaque(false);
        actions.add(pageMessage, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        editButton = createButton("Edit Profile", TEAL);
        saveButton = createButton("Save Changes", NAVY);
        cancelButton = createButton("Cancel", new Color(105, 120, 135));
        buttons.add(editButton);
        buttons.add(saveButton);
        buttons.add(cancelButton);
        actions.add(buttons, BorderLayout.SOUTH);
        detailsCard.add(detailsTitle, BorderLayout.NORTH);
        detailsCard.add(fields, BorderLayout.CENTER);
        detailsCard.add(actions, BorderLayout.SOUTH);

        JLabel footer = new JLabel("CampusConnect  •  One Platform for Every Student Need");
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        footer.setBorder(new EmptyBorder(4, 2, 0, 0));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(profileCard);
        body.add(Box.createVerticalStrut(18));
        body.add(detailsCard);
        body.add(Box.createVerticalStrut(14));
        body.add(footer);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        editButton.addActionListener(e -> startEditing());
        saveButton.addActionListener(e -> saveProfile());
        cancelButton.addActionListener(e -> cancelEditing());
        setFormEnabled(false);
        photoButton.setEnabled(false);
    }

    private javax.swing.border.Border cardBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(top, left, bottom, right));
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(TEXT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setPreferredSize(new Dimension(200, 39));
        return field;
    }

    private JPanel createFieldPanel(String labelText, JComponent value) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        value.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(label);
        panel.add(Box.createVerticalStrut(7));
        panel.add(value);
        return panel;
    }

    private JPanel createInfoPanel(String label, String value) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel heading = new JLabel(label);
        heading.setFont(new Font("Arial", Font.BOLD, 11));
        heading.setForeground(MUTED);
        JLabel detail = new JLabel("<html><body style='width:260px'>" + value + "</body></html>");
        detail.setFont(new Font("Arial", Font.PLAIN, 12));
        detail.setForeground(TEXT);
        panel.add(heading);
        panel.add(Box.createVerticalStrut(8));
        panel.add(detail);
        return panel;
    }

    private JButton createButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(145, 42));
        return button;
    }

    private void loadFacultyProfile() {
        if (facultyEmail.isEmpty()) {
            showError("Faculty email is missing. Please log in again.");
            setFormEnabled(false);
            return;
        }
        String sql = "SELECT faculty_id, name, email, department, designation, status, created_at, photo_path " +
                "FROM faculty WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, facultyEmail);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    showError("No faculty account was found for this email.");
                    setFormEnabled(false);
                    return;
                }
                facultyId = rs.getInt("faculty_id");
                nameField.setText(nullToEmpty(rs.getString("name")));
                emailField.setText(nullToEmpty(rs.getString("email")));
                departmentField.setText(nullToEmpty(rs.getString("department")));
                designationField.setText(nullToEmpty(rs.getString("designation")));
                facultyIdValue.setText(String.valueOf(facultyId));
                String status = nullToEmpty(rs.getString("status"));
                statusValue.setText(status);
                statusValue.setForeground("APPROVED".equalsIgnoreCase(status)
                        ? new Color(32, 145, 87) : TEAL);
                Timestamp createdAt = rs.getTimestamp("created_at");
                joinedValue.setText(createdAt == null ? "Not available" :
                        new SimpleDateFormat("dd MMM yyyy").format(createdAt));
                photoPath = rs.getString("photo_path");
                originalPhotoPath = photoPath;
                updateIdentity();
                showPhoto(photoPath);
                saveOriginalValues();
                setFormEnabled(false);
                pageMessage.setForeground(new Color(32, 145, 87));
                pageMessage.setText("Profile loaded successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Unable to load faculty profile. Check that photo_path column exists: " + ex.getMessage());
            setFormEnabled(false);
        }
    }

    private void updateIdentity() {
        profileNameLabel.setText(nameField.getText().trim().isEmpty() ? "Faculty Member" : nameField.getText().trim());
        profileDesignationLabel.setText((designationField.getText().trim().isEmpty() ? "Faculty" : designationField.getText().trim())
                + "  •  " + (departmentField.getText().trim().isEmpty() ? "Department not set" : departmentField.getText().trim()));
        String name = nameField.getText().trim();
        photoLabel.setText(name.isEmpty() ? "F" : name.substring(0, 1).toUpperCase());
    }

    private void showPhoto(String path) {
        if (path == null || path.trim().isEmpty()) {
            photoLabel.setIcon(null);
            updateIdentity();
            return;
        }
        File file = new File(path);
        if (!file.isFile()) {
            photoLabel.setIcon(null);
            return;
        }
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
        photoLabel.setText("");
        photoLabel.setIcon(new ImageIcon(image));
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose Faculty Profile Photo");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files (JPG, JPEG, PNG)", "jpg", "jpeg", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            photoPath = file.getAbsolutePath();
            showPhoto(photoPath);
            pageMessage.setForeground(MUTED);
            pageMessage.setText("Photo selected. Click Save Changes to save it.");
        }
    }

    private void startEditing() {
        saveOriginalValues();
        editing = true;
        setFormEnabled(true);
        photoButton.setEnabled(true);
        emailField.setEditable(false);
        pageMessage.setForeground(MUTED);
        pageMessage.setText("Update your details and save your changes.");
    }

    private void saveProfile() {
        if (!editing) return;
        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String designation = designationField.getText().trim();
        if (name.isEmpty() || department.isEmpty() || designation.isEmpty()) {
            showWarning("Name, department and designation cannot be empty.");
            return;
        }
        if (name.length() > 100 || department.length() > 100 || designation.length() > 100) {
            showWarning("Each field must contain no more than 100 characters.");
            return;
        }
        String sql = "UPDATE faculty SET name = ?, department = ?, designation = ?, photo_path = ? " +
                "WHERE faculty_id = ? AND email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, department);
            statement.setString(3, designation);
            if (photoPath == null || photoPath.trim().isEmpty()) statement.setNull(4, java.sql.Types.VARCHAR);
            else statement.setString(4, photoPath);
            statement.setInt(5, facultyId);
            statement.setString(6, facultyEmail);
            statement.executeUpdate();
            editing = false;
            saveOriginalValues();
            originalPhotoPath = photoPath;
            setFormEnabled(false);
            photoButton.setEnabled(false);
            updateIdentity();
            pageMessage.setForeground(new Color(32, 145, 87));
            pageMessage.setText("Profile updated successfully.");
            JOptionPane.showMessageDialog(this, "Your faculty profile has been updated.",
                    "Profile Updated", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Unable to save profile: " + ex.getMessage());
        }
    }

    private void cancelEditing() {
        nameField.setText(originalName);
        departmentField.setText(originalDepartment);
        designationField.setText(originalDesignation);
        photoPath = originalPhotoPath;
        showPhoto(photoPath);
        updateIdentity();
        editing = false;
        setFormEnabled(false);
        photoButton.setEnabled(false);
        pageMessage.setForeground(MUTED);
        pageMessage.setText("Changes discarded.");
    }

    private void saveOriginalValues() {
        originalName = nameField.getText();
        originalDepartment = departmentField.getText();
        originalDesignation = designationField.getText();
    }

    private void setFormEnabled(boolean enabled) {
        nameField.setEditable(enabled);
        departmentField.setEditable(enabled);
        designationField.setEditable(enabled);
        nameField.setBackground(enabled ? WHITE : new Color(240, 244, 248));
        departmentField.setBackground(enabled ? WHITE : new Color(240, 244, 248));
        designationField.setBackground(enabled ? WHITE : new Color(240, 244, 248));
        editButton.setVisible(!enabled);
        saveButton.setVisible(enabled);
        cancelButton.setVisible(enabled);
        if (photoButton != null) photoButton.setEnabled(enabled);
    }

    private String nullToEmpty(String value) { return value == null ? "" : value; }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        pageMessage.setForeground(new Color(190, 55, 55));
        pageMessage.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrame("Faculty Profile Preview") {{
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(1100, 780);
            setLocationRelativeTo(null);
            setContentPane(new FacultyProfileFrame("faculty@example.com"));
            setVisible(true);
        }});
    }
}
