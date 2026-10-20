package com.campusconnect.ui.faculty;

import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.util.StudyMaterialStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class FacultyStudyMaterialsFrame extends JPanel {

    // =========================================================
    // COLORS - MATCH ADMIN MANAGE STUDY MATERIALS
    // =========================================================

    private static final Color PAGE_BG = new Color(232, 247, 251);
    private static final Color WHITE = Color.WHITE;
    private static final Color NAVY = new Color(24, 67, 108);
    private static final Color TEAL = new Color(24, 168, 181);
    private static final Color BLUE = new Color(55, 105, 180);
    private static final Color GREEN = new Color(27, 157, 91);
    private static final Color RED = new Color(215, 55, 55);
    private static final Color TEXT = new Color(38, 57, 76);
    private static final Color MUTED = new Color(96, 117, 137);
    private static final Color BORDER = new Color(205, 222, 232);

    private static final String RESOURCE_FOLDER =
            "src/main/resources/study_materials";

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTable materialsTable;
    private DefaultTableModel tableModel;

    private JTextField titleField;
    private JTextField subjectField;
    private JTextField descriptionField;
    private JTextField fileField;
    private JTextField searchField;

    private JLabel totalMaterialsValue;
    private JLabel subjectCountValue;
    private JLabel latestMaterialValue;

    private final String facultyName;
    private final String facultyEmail;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public FacultyStudyMaterialsFrame(
            String facultyName,
            String facultyEmail) {

        this.facultyName = facultyName;
        this.facultyEmail = facultyEmail;

        setLayout(new BorderLayout());
        buildUI();
        loadMaterials();
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PAGE_BG);
        content.setBorder(new EmptyBorder(15, 35, 25, 35));

        content.add(createStatsPanel());
        content.add(Box.createVerticalStrut(15));
        content.add(createMaterialsCard());
        content.add(Box.createVerticalStrut(15));
        content.add(createAddMaterialCard());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scrollPane, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);

        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(14, 35, 14, 35)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setBackground(WHITE);

        JPanel iconBox = createIconBox("▤", TEAL, 68);
        left.add(iconBox);
        left.add(Box.createHorizontalStrut(16));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);

        JLabel brand = new JLabel("CAMPUSCONNECT");
        brand.setFont(new Font("Arial", Font.BOLD, 13));
        brand.setForeground(TEAL);

        JLabel title = new JLabel("Manage Study Materials");
        title.setFont(new Font("Arial", Font.BOLD, 29));
        title.setForeground(NAVY);

        JLabel subtitle = new JLabel(
                "View, organize and manage learning resources");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JLabel facultyLabel = new JLabel(
                "Faculty Portal  |  " + facultyName + "  |  " + facultyEmail);
        facultyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        facultyLabel.setForeground(MUTED);

        titlePanel.add(brand);
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitle);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(facultyLabel);

        left.add(titlePanel);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setBackground(WHITE);

        JPanel ccBox = createIconBox("CC", NAVY, 52);

        JLabel portalLabel = new JLabel(
                "<html><b>CampusConnect</b>" +
                        "<br><font color='#60758A'>Faculty Portal</font></html>");
        portalLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton refreshButton = createButton(
                "Refresh", TEAL, WHITE, 100, 38);

        refreshButton.addActionListener(e ->
                loadMaterials(searchField.getText().trim()));

        right.add(ccBox);
        right.add(portalLabel);
        right.add(Box.createHorizontalStrut(12));
        right.add(refreshButton);

        header.add(right, BorderLayout.EAST);

        return header;
    }

    // =========================================================
    // ICON BOX
    // =========================================================

    private JPanel createIconBox(
            String icon,
            Color background,
            int size) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(background);
        panel.setPreferredSize(new Dimension(size, size));
        panel.setMinimumSize(new Dimension(size, size));
        panel.setMaximumSize(new Dimension(size, size));

        JLabel label = new JLabel(icon);
        label.setForeground(WHITE);
        label.setFont(new Font(
                "Arial", Font.BOLD, size >= 65 ? 29 : 19));

        panel.add(label);
        return panel;
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private JPanel createStatsPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(1000, 90));
        panel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 90));

        totalMaterialsValue = createStatValue();
        subjectCountValue = createStatValue();
        latestMaterialValue = createStatValue();

        panel.add(createStatCard(
                "▤", "TOTAL MATERIALS", totalMaterialsValue, TEAL));

        panel.add(createStatCard(
                "◈", "TOTAL SUBJECTS", subjectCountValue, BLUE));

        panel.add(createStatCard(
                "★", "LATEST MATERIAL", latestMaterialValue, NAVY));

        return panel;
    }

    private JLabel createStatValue() {

        JLabel label = new JLabel("0");
        label.setFont(new Font("Arial", Font.BOLD, 21));
        return label;
    }

    private JPanel createStatCard(
            String icon,
            String title,
            JLabel value,
            Color accent) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(235, 248, 251));
        iconLabel.setForeground(accent);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setPreferredSize(new Dimension(55, 55));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(WHITE);
        text.setBorder(new EmptyBorder(3, 12, 0, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(MUTED);

        value.setForeground(accent);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(5));
        text.add(value);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // MATERIALS LIBRARY
    // =========================================================

    private JPanel createMaterialsCard() {

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 20, 14, 20)
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(WHITE);

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.X_AXIS));
        heading.setBackground(WHITE);

        JLabel icon = new JLabel("▤");
        icon.setFont(new Font("Arial", Font.BOLD, 24));
        icon.setForeground(TEAL);

        heading.add(icon);
        heading.add(Box.createHorizontalStrut(9));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(WHITE);

        JLabel title = new JLabel("Study Materials Library");
        title.setFont(new Font("Arial", Font.BOLD, 21));
        title.setForeground(NAVY);

        JLabel subtitle = new JLabel(
                "Admin and faculty resources available in the library");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        text.add(title);
        text.add(Box.createVerticalStrut(2));
        text.add(subtitle);

        heading.add(text);
        top.add(heading, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(WHITE);

        JLabel searchIcon = new JLabel("⌕");
        searchIcon.setFont(new Font("Arial", Font.BOLD, 22));
        searchIcon.setForeground(NAVY);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(230, 34));
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setToolTipText(
                "Search title, subject, description or uploader");

        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(0, 8, 0, 8)
        ));

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        loadMaterials(searchField.getText().trim());
                    }

                    public void removeUpdate(DocumentEvent e) {
                        loadMaterials(searchField.getText().trim());
                    }

                    public void changedUpdate(DocumentEvent e) {
                        loadMaterials(searchField.getText().trim());
                    }
                });

        JButton refreshButton = createButton(
                "Refresh", BLUE, WHITE, 100, 34);

        refreshButton.addActionListener(e ->
                loadMaterials(searchField.getText().trim()));

        searchPanel.add(searchIcon);
        searchPanel.add(searchField);
        searchPanel.add(refreshButton);

        top.add(searchPanel, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        String[] columns = {
                "ID", "TITLE", "SUBJECT", "DESCRIPTION",
                "FILE", "Owner ID"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        materialsTable = new JTable(tableModel);
        materialsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        materialsTable.setForeground(TEXT);
        materialsTable.setBackground(WHITE);
        materialsTable.setRowHeight(42);
        materialsTable.setShowGrid(true);
        materialsTable.setGridColor(new Color(220, 231, 238));
        materialsTable.setSelectionBackground(new Color(222, 241, 247));
        materialsTable.setSelectionForeground(NAVY);
        materialsTable.setAutoCreateRowSorter(true);
        materialsTable.setFillsViewportHeight(true);
        materialsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        materialsTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12));
        materialsTable.getTableHeader().setForeground(WHITE);
        materialsTable.getTableHeader().setBackground(NAVY);
        materialsTable.getTableHeader().setPreferredSize(
                new Dimension(0, 42));
        materialsTable.getTableHeader().setReorderingAllowed(false);

        materialsTable.getColumnModel().getColumn(0)
                .setPreferredWidth(55);
        materialsTable.getColumnModel().getColumn(1)
                .setPreferredWidth(220);
        materialsTable.getColumnModel().getColumn(2)
                .setPreferredWidth(150);
        materialsTable.getColumnModel().getColumn(3)
                .setPreferredWidth(350);
        materialsTable.getColumnModel().getColumn(4)
                .setPreferredWidth(280);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        materialsTable.getColumnModel().getColumn(0)
                .setCellRenderer(centerRenderer);

        // Owner ID remains in the model for permission checks,
        // but is hidden from the user.
        materialsTable.removeColumn(
                materialsTable.getColumnModel().getColumn(5));

        JScrollPane tableScroll = new JScrollPane(materialsTable);
        tableScroll.setPreferredSize(new Dimension(1000, 300));
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER));

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setBackground(WHITE);

        JButton openButton = createButton(
                "Open Selected File", BLUE, WHITE, 155, 40);
        openButton.addActionListener(e -> openSelectedFile());

        JButton deleteButton = createButton(
                "Delete My Material", RED, WHITE, 160, 40);
        deleteButton.addActionListener(e -> deleteOwnMaterial());

        JButton clearButton = createButton(
                "Clear Search", NAVY, WHITE, 120, 40);
        clearButton.addActionListener(e -> searchField.setText(""));

        actions.add(openButton);
        actions.add(deleteButton);
        actions.add(clearButton);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(WHITE);
        center.add(tableScroll, BorderLayout.CENTER);
        center.add(actions, BorderLayout.SOUTH);

        card.add(center, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // ADD MATERIAL CARD
    // =========================================================

    private JPanel createAddMaterialCard() {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 20, 16, 20)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);

        JPanel heading = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0));
        heading.setBackground(WHITE);

        JLabel icon = new JLabel("＋");
        icon.setFont(new Font("Arial", Font.BOLD, 25));
        icon.setForeground(TEAL);

        heading.add(icon);
        heading.add(Box.createHorizontalStrut(8));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);

        JLabel title = new JLabel("Add New Study Material");
        title.setFont(new Font("Arial", Font.BOLD, 21));
        title.setForeground(NAVY);

        JLabel subtitle = new JLabel(
                "Upload a learning resource to the shared library");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitle);

        heading.add(titlePanel);
        header.add(heading, BorderLayout.WEST);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        titleField = new JTextField();
        subjectField = new JTextField();
        descriptionField = new JTextField();
        fileField = new JTextField();
        fileField.setEditable(true);
        fileField.setToolTipText(
                "Enter an online URL or use Browse to select a local file"
        );

        addField(fields, gbc, "Title", titleField, 0, 0);
        addField(fields, gbc, "Subject", subjectField, 1, 0);
        addField(fields, gbc, "Description", descriptionField, 0, 1);
        addField(fields, gbc, "File Path", fileField, 1, 1);

        JButton browseButton = createButton(
                "Browse...", TEAL, WHITE, 105, 35);
        browseButton.addActionListener(e -> chooseFile());

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        fields.add(browseButton, gbc);

        JButton clearFormButton = createButton(
                "Clear Form", NAVY, WHITE, 120, 40);
        clearFormButton.addActionListener(e -> clearForm());

        JButton addButton = createButton(
                "Add Material", GREEN, WHITE, 140, 40);
        addButton.addActionListener(e -> addMaterial());

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttons.setBackground(WHITE);
        buttons.add(clearFormButton);
        buttons.add(addButton);

        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setBackground(WHITE);
        body.add(fields, BorderLayout.CENTER);
        body.add(buttons, BorderLayout.SOUTH);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            String label,
            JTextField field,
            int column,
            int row) {

        JPanel wrapper = new JPanel(new BorderLayout(0, 5));
        wrapper.setBackground(WHITE);

        JLabel caption = new JLabel(label);
        caption.setFont(new Font("Arial", Font.BOLD, 12));
        caption.setForeground(NAVY);

        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(200, 34));

        wrapper.add(caption, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);

        gbc.gridx = column;
        gbc.gridy = row;
        gbc.weightx = 1;

        panel.add(wrapper, gbc);
    }

    // =========================================================
    // BUTTON FACTORY
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground,
            int width,
            int height) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));

        return button;
    }

    // =========================================================
    // BROWSE FILE
    // =========================================================


    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Study Material");

        File storageFolder = new File(
                System.getProperty("user.dir"),
                "src/main/resources/study_materials"
        );

        if (storageFolder.exists() && storageFolder.isDirectory()) {
            chooser.setCurrentDirectory(storageFolder);
        }

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            if (!selectedFile.isFile()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a valid file.",
                        "Invalid File",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {
                String relativePath =
                        StudyMaterialStorage.importLocalFile(selectedFile);

                fileField.setText(relativePath);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to copy the selected file.\n"
                                + ex.getMessage(),
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    // =========================================================
    // LOAD MATERIALS
    // =========================================================

    private void loadMaterials() {
        loadMaterials("");
    }

    private void loadMaterials(String search) {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        Set<String> subjects = new HashSet<>();
        String latestTitle = "None";

        String sql =
                "SELECT sm.material_id, sm.title, sm.subject, " +
                        "sm.description, sm.file_path, sm.faculty_id " +
                        "FROM study_materials sm " +
                        "WHERE sm.title LIKE ? OR sm.subject LIKE ? " +
                        "OR sm.description LIKE ? " +
                        "ORDER BY sm.material_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String pattern = "%" + search + "%";

            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Object ownerId = rs.getObject("faculty_id");
                    String title = rs.getString("title");
                    String subject = rs.getString("subject");

                    if (subject != null && !subject.trim().isEmpty()) {
                        subjects.add(subject.trim().toLowerCase());
                    }

                    if (tableModel.getRowCount() == 0) {
                        latestTitle = title == null ? "None" : title;
                    }

                    tableModel.addRow(new Object[]{
                            rs.getInt("material_id"),
                            title,
                            subject,
                            rs.getString("description"),
                            rs.getString("file_path"),
                            ownerId
                    });
                }
            }

            totalMaterialsValue.setText(
                    String.valueOf(tableModel.getRowCount()));
            subjectCountValue.setText(String.valueOf(subjects.size()));
            latestMaterialValue.setText(latestTitle);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load study materials.\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // GET CURRENT FACULTY ID
    // =========================================================

    private int getCurrentFacultyId(Connection connection)
            throws SQLException {

        String sql =
                "SELECT faculty_id FROM faculty " +
                        "WHERE email = ? AND status = 'APPROVED'";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, facultyEmail);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("faculty_id");
                }
            }
        }

        throw new SQLException(
                "Approved faculty account not found: " + facultyEmail);
    }

    // =========================================================
    // ADD MATERIAL
    // =========================================================


    private void addMaterial() {

        String title = titleField.getText().trim();
        String subject = subjectField.getText().trim();
        String description = descriptionField.getText().trim();
        String path = fileField.getText().trim();

        if (title.isEmpty() || subject.isEmpty()
                || description.isEmpty() || path.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please complete all fields and select a file.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Check whether the resource is an online URL.
        boolean isOnlineResource =
                path.matches("(?i)^https?://\\S+$");

        // Validate local files only.
        if (!isOnlineResource) {
            File file;

            try {
                file = StudyMaterialStorage.resolveLocalFile(path);
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to resolve selected file:\n" + ex.getMessage(),
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (file == null || !file.exists() || !file.isFile()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selected file does not exist:\n" + path,
                        "Invalid File",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        String sql =
                "INSERT INTO study_materials " +
                        "(title, subject, description, file_path, faculty_id) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {

            int facultyId = getCurrentFacultyId(connection);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, title);
                statement.setString(2, subject);
                statement.setString(3, description);
                statement.setString(4, path.replace("\\", "/"));
                statement.setInt(5, facultyId);

                statement.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Study material added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();
            loadMaterials(searchField.getText().trim());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to add material.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // DELETE OWN MATERIAL ONLY
    // =========================================================

    private void deleteOwnMaterial() {

        int selectedRow = materialsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a material first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow =
                materialsTable.convertRowIndexToModel(selectedRow);

        Object ownerId = tableModel.getValueAt(modelRow, 5);

        if (ownerId == null || ownerId.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "This material belongs to Admin. You cannot delete it.",
                    "Permission Denied",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int materialId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 0).toString());

        String materialTitle = String.valueOf(
                tableModel.getValueAt(modelRow, 1));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete your material?\n\n" + materialTitle,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM study_materials " +
                        "WHERE material_id = ? AND faculty_id = " +
                        "(SELECT faculty_id FROM faculty " +
                        "WHERE email = ? AND status = 'APPROVED')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, materialId);
            statement.setString(2, facultyEmail);

            int deleted = statement.executeUpdate();

            if (deleted > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Your material was deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadMaterials(searchField.getText().trim());

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "You can delete only materials uploaded by your account.",
                        "Permission Denied",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete material.\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // OPEN SELECTED FILE OR WEB LINK
    // =========================================================


    private void openSelectedFile() {

        int selectedRow = materialsTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a material first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow =
                materialsTable.convertRowIndexToModel(selectedRow);

        Object pathValue = tableModel.getValueAt(modelRow, 4);

        if (pathValue == null || pathValue.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No file path or URL is saved for this material.",
                    "Missing Path",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String path = pathValue.toString().trim();

        try {
            // Open online URLs in the default browser.
            if (path.startsWith("https://") || path.startsWith("http://")) {
                if (!Desktop.isDesktopSupported()) {
                    throw new UnsupportedOperationException(
                            "Desktop integration is not available."
                    );
                }

                Desktop.getDesktop().browse(URI.create(path));
                return;
            }

            File file = new File(path);
            File projectDirectory = new File(System.getProperty("user.dir"));

            // First try the saved path as absolute or project-relative.
            if (!file.isAbsolute()) {
                file = new File(projectDirectory, path);
            }

            // If not found, try the shared study_materials folder.
            if (!file.exists()) {
                File resourceFile = new File(
                        projectDirectory,
                        "src/main/resources/study_materials"
                );

                file = new File(
                        resourceFile,
                        path.replace("/", File.separator)
                );
            }

            // Resolve canonical path before checking and opening.
            file = file.getCanonicalFile();

            if (!file.exists() || !file.isFile()) {
                JOptionPane.showMessageDialog(
                        this,
                        "File not found on this computer.\n\n"
                                + "Saved path: " + path + "\n"
                                + "Checked path: " + file.getAbsolutePath()
                                + "\n\nPlease verify that the file exists "
                                + "inside the study_materials folder.",
                        "File Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!Desktop.isDesktopSupported()) {
                throw new UnsupportedOperationException(
                        "Desktop file opening is not supported."
                );
            }

            Desktop.getDesktop().open(file);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not open the selected file.\n\n"
                            + "Saved path: " + path + "\n"
                            + "Reason: " + e.getMessage(),
                    "Open File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {
        titleField.setText("");
        subjectField.setText("");
        descriptionField.setText("");
        fileField.setText("");
    }

    // =========================================================
    // DIRECT RUN FOR TESTING
    // =========================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CampusConnect - Manage Study Materials");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1100, 700));
            frame.setSize(1200, 750);
            frame.setContentPane(new FacultyStudyMaterialsFrame(
                    "Test Faculty",
                    "faculty.test@campusconnect.com"
            ));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
