
package com.campusconnect;

import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsFrame extends JPanel {

    private JTable studentsTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField courseField;
    private JTextField semesterField;
    private JTextField cgpaField;
    private JTextField searchField;

    private JLabel studentCountLabel;
    private JLabel visibleCountLabel;
    private final Runnable onClose;

    // =========================================================
    // CAMPUSCONNECT COLORS
    // =========================================================

    private static final Color NAVY =
            new Color(19, 57, 96);

    private static final Color NAVY_DARK =
            new Color(10, 42, 72);

    private static final Color NAVY_LIGHT =
            new Color(31, 78, 121);

    private static final Color TEAL =
            new Color(24, 160, 174);

    private static final Color TEAL_DARK =
            new Color(17, 133, 146);

    // Login / Announcement inspired background
    private static final Color BG_TOP =
            new Color(239, 249, 253);

    private static final Color BG_BOTTOM =
            new Color(215, 240, 247);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color TEXT_DARK =
            new Color(25, 43, 61);

    private static final Color TEXT_GRAY =
            new Color(103, 119, 136);

    private static final Color BORDER =
            new Color(207, 225, 233);

    private static final Color GREEN =
            new Color(42, 157, 91);

    private static final Color RED =
            new Color(202, 70, 70);

    private static final Color ORANGE =
            new Color(224, 137, 36);

    private static final Color BLUE =
            new Color(55, 105, 180);


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public StudentsFrame() {
        this(() -> { });
    }

    public StudentsFrame(Runnable onClose) {
        this.onClose = onClose == null ? () -> { } : onClose;
        setLayout(new BorderLayout());
        setBackground(BG_TOP);

        // =====================================================
        // MAIN BACKGROUND
        // =====================================================

        BackgroundPanel mainPanel =
                new BackgroundPanel();

        mainPanel.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // HEADER
        // =====================================================

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(0, 18)
                );

        contentPanel.setOpaque(false);

        contentPanel.setBorder(
                new EmptyBorder(
                        22,
                        30,
                        25,
                        30
                )
        );

        // =====================================================
        // TOP AREA
        // =====================================================

        contentPanel.add(
                createTopSection(),
                BorderLayout.NORTH
        );

        // =====================================================
        // TABLE
        // =====================================================

        contentPanel.add(
                createTablePanel(),
                BorderLayout.CENTER
        );

        // =====================================================
        // ADD STUDENT
        // =====================================================

        contentPanel.add(
                createAddStudentPanel(),
                BorderLayout.SOUTH
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        add(mainPanel, BorderLayout.CENTER);

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadStudents();
    }


    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(WHITE);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                32,
                                18,
                                32
                        )
                )
        );

        // =====================================================
        // LEFT
        // =====================================================

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel smallBrand =
                new JLabel(
                        "CAMPUSCONNECT"
                );

        smallBrand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        smallBrand.setForeground(TEAL);

        JLabel title =
                new JLabel(
                        "Manage Students"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        title.setForeground(
                TEXT_DARK
        );

        JLabel subtitle =
                new JLabel(
                        "Manage student accounts, academic details and records"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                TEXT_GRAY
        );

        left.add(
                smallBrand
        );

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(
                title
        );

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(
                subtitle
        );

        // =====================================================
        // RIGHT BRAND
        // =====================================================

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                4
                        )
                );

        right.setOpaque(false);

        JLabel logo =
                new JLabel(
                        "CC",
                        SwingConstants.CENTER
                );

        logo.setPreferredSize(
                new Dimension(
                        48,
                        48
                )
        );

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        logo.setForeground(
                WHITE
        );

        logo.setBackground(
                TEAL
        );

        logo.setOpaque(true);

        JLabel brand =
                new JLabel(
                        "CampusConnect"
                );

        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        brand.setForeground(
                NAVY
        );

        right.add(logo);
        right.add(brand);

        header.add(
                left,
                BorderLayout.WEST
        );

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }


    // =========================================================
    // TOP SECTION
    // =========================================================

    private JPanel createTopSection() {

        JPanel container =
                new JPanel(
                        new BorderLayout(
                                18,
                                0
                        )
                );

        container.setOpaque(false);

        // =====================================================
        // WELCOME CARD
        // =====================================================

        JPanel welcomeCard =
                createCardPanel();

        welcomeCard.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );

        JLabel icon =
                createIconBox(
                        "ST",
                        TEAL
                );

        welcomeCard.add(
                icon,
                BorderLayout.WEST
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel heading =
                new JLabel(
                        "Student Management"
                );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        heading.setForeground(
                TEXT_DARK
        );

        JLabel description =
                new JLabel(
                        "Search, view and manage all registered students."
                );

        description.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        description.setForeground(
                TEXT_GRAY
        );

        text.add(
                heading
        );

        text.add(
                Box.createVerticalStrut(5)
        );

        text.add(
                description
        );

        welcomeCard.add(
                text,
                BorderLayout.CENTER
        );


        // =====================================================
        // TOTAL STUDENTS
        // =====================================================

        JPanel countCard =
                createStatCard(
                        "TOTAL STUDENTS",
                        "0",
                        TEAL,
                        "ST"
                );

        studentCountLabel =
                findStatValueLabel(
                        countCard
                );


        // =====================================================
        // VISIBLE RECORDS
        // =====================================================

        JPanel visibleCard =
                createStatCard(
                        "VISIBLE RECORDS",
                        "0",
                        NAVY_LIGHT,
                        "VIEW"
                );

        visibleCountLabel =
                findStatValueLabel(
                        visibleCard
                );


        // =====================================================
        // SEARCH
        // =====================================================

        JPanel searchCard =
                createSearchCard();

        JPanel rightArea =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        rightArea.setOpaque(false);

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        stats.setOpaque(false);

        stats.add(
                countCard
        );

        stats.add(
                visibleCard
        );

        rightArea.add(
                searchCard,
                BorderLayout.CENTER
        );

        rightArea.add(
                stats,
                BorderLayout.SOUTH
        );

        container.add(
                welcomeCard,
                BorderLayout.WEST
        );

        container.add(
                rightArea,
                BorderLayout.CENTER
        );

        return container;
    }


    // =========================================================
    // SEARCH CARD
    // =========================================================

    private JPanel createSearchCard() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        JLabel searchLabel =
                new JLabel(
                        "Search"
                );

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        searchLabel.setForeground(
                NAVY
        );

        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        searchField.setToolTipText(
                "Search by name, email, course or semester"
        );

        styleField(
                searchField
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                searchStudents();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                searchStudents();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                searchStudents();
                            }
                        }
                );

        JButton refreshButton =
                createButton(
                        "Refresh",
                        BLUE
                );

        refreshButton.addActionListener(
                e -> loadStudents()
        );

        card.add(
                searchLabel,
                BorderLayout.WEST
        );

        card.add(
                searchField,
                BorderLayout.CENTER
        );

        card.add(
                refreshButton,
                BorderLayout.EAST
        );

        return card;
    }


    // =========================================================
    // TABLE PANEL
    // =========================================================

    private JPanel createTablePanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        // =====================================================
        // TABLE HEADING
        // =====================================================

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Registered Students"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(
                TEXT_DARK
        );

        JLabel subtitle =
                new JLabel(
                        "Student account and academic information"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                TEXT_GRAY
        );

        heading.add(
                title,
                BorderLayout.WEST
        );

        heading.add(
                subtitle,
                BorderLayout.EAST
        );

        panel.add(
                heading,
                BorderLayout.NORTH
        );


        // =====================================================
        // TABLE MODEL
        // =====================================================

        String[] columns = {
                "ID",
                "Name",
                "Email",
                "Course",
                "Semester",
                "CGPA"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        studentsTable =
                new JTable(
                        tableModel
                );

        studentsTable.setRowHeight(
                42
        );

        studentsTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        studentsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        studentsTable.setShowGrid(
                false
        );

        studentsTable.setIntercellSpacing(
                new Dimension(
                        0,
                        0
                )
        );

        studentsTable.setSelectionBackground(
                new Color(
                        225,
                        243,
                        246
                )
        );

        studentsTable.setSelectionForeground(
                TEXT_DARK
        );

        studentsTable.setFillsViewportHeight(
                true
        );


        // =====================================================
        // TABLE HEADER
        // =====================================================

        studentsTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        studentsTable
                .getTableHeader()
                .setForeground(
                        WHITE
                );

        studentsTable
                .getTableHeader()
                .setBackground(
                        NAVY
                );

        studentsTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                44
                        )
                );


        // =====================================================
        // CENTER RENDERER
        // =====================================================

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        Component component =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        if (!isSelected) {

                            if (row % 2 == 0) {
                                component.setBackground(
                                        WHITE
                                );
                            } else {
                                component.setBackground(
                                        new Color(
                                                247,
                                                251,
                                                253
                                        )
                                );
                            }
                        }

                        return component;
                    }
                };

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        studentsTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        centerRenderer
                );

        studentsTable
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        centerRenderer
                );


        // =====================================================
        // COLUMN WIDTH
        // =====================================================

        studentsTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        studentsTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(210);

        studentsTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(320);

        studentsTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(170);

        studentsTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);


        // =====================================================
        // SCROLL PANE
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        studentsTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =====================================================
        // TABLE BUTTONS
        // =====================================================

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttons.setOpaque(false);

        JButton editButton =
                createButton(
                        "Edit Selected",
                        ORANGE
                );

        editButton.addActionListener(
                e -> editSelectedStudent()
        );

        JButton deleteButton =
                createButton(
                        "Delete Selected",
                        RED
                );

        deleteButton.addActionListener(
                e -> deleteSelectedStudent()
        );

        buttons.add(
                editButton
        );

        buttons.add(
                deleteButton
        );

        panel.add(
                buttons,
                BorderLayout.SOUTH
        );

        return panel;
    }


    // =========================================================
    // ADD STUDENT PANEL
    // =========================================================

    private JPanel createAddStudentPanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );


        // =====================================================
        // HEADING
        // =====================================================

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setOpaque(false);

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Add New Student"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(
                TEXT_DARK
        );

        JLabel subtitle =
                new JLabel(
                        "Create a new CampusConnect student account"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                TEXT_GRAY
        );

        left.add(
                title
        );

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(
                subtitle
        );

        JLabel badge =
                new JLabel(
                        "NEW STUDENT"
                );

        badge.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        badge.setForeground(
                TEAL
        );

        heading.add(
                left,
                BorderLayout.WEST
        );

        heading.add(
                badge,
                BorderLayout.EAST
        );

        panel.add(
                heading,
                BorderLayout.NORTH
        );


        // =====================================================
        // FORM
        // =====================================================

        JPanel form =
                new JPanel(
                        new GridLayout(
                                1,
                                6,
                                12,
                                0
                        )
                );

        form.setOpaque(false);

        nameField =
                createTextField(
                        "Enter student name"
                );

        emailField =
                createTextField(
                        "Enter email address"
                );

        passwordField =
                new JPasswordField();

        styleField(
                passwordField
        );

        passwordField.setToolTipText(
                "Create student password"
        );

        courseField =
                createTextField(
                        "e.g. BCA"
                );

        semesterField =
                createTextField(
                        "e.g. 3"
                );

        cgpaField =
                createTextField(
                        "e.g. 8.4 (optional)"
                );
        cgpaField.setToolTipText("CGPA from 0.00 to 10.00; leave blank if not available");

        form.add(
                createFieldPanel(
                        "Student Name",
                        nameField
                )
        );

        form.add(
                createFieldPanel(
                        "Email",
                        emailField
                )
        );

        form.add(
                createFieldPanel(
                        "Password",
                        passwordField
                )
        );

        form.add(
                createFieldPanel(
                        "Course",
                        courseField
                )
        );

        form.add(
                createFieldPanel(
                        "Semester",
                        semesterField
                )
        );

        form.add(
                createFieldPanel(
                        "CGPA",
                        cgpaField
                )
        );

        panel.add(
                form,
                BorderLayout.CENTER
        );


        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttons.setOpaque(false);

        JButton clearButton =
                createButton(
                        "Clear",
                        TEXT_GRAY
                );

        clearButton.addActionListener(
                e -> clearFields()
        );

        JButton addButton =
                createButton(
                        "Add Student",
                        GREEN
                );

        addButton.addActionListener(
                e -> addStudent()
        );

        JButton closeButton =
                createButton(
                        "Close",
                        NAVY
                );

        closeButton.addActionListener(
                e -> onClose.run()
        );

        buttons.add(
                clearButton
        );

        buttons.add(
                addButton
        );

        buttons.add(
                closeButton
        );

        panel.add(
                buttons,
                BorderLayout.SOUTH
        );

        return panel;
    }


    // =========================================================
    // CARD PANEL
    // =========================================================

    private JPanel createCardPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                16,
                                18,
                                16,
                                18
                        )
                )
        );

        return panel;
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            String value,
            Color accent,
            String iconText
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        card.setBackground(
                WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        JLabel icon =
                createIconBox(
                        iconText,
                        accent
                );

        icon.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        titleLabel.setForeground(
                TEXT_GRAY
        );

        JLabel valueLabel =
                new JLabel(
                        value
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        valueLabel.setForeground(
                accent
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(2)
        );

        text.add(
                valueLabel
        );

        card.add(
                icon,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        return card;
    }


    // =========================================================
    // FIND STAT VALUE LABEL
    // =========================================================

    private JLabel findStatValueLabel(
            JPanel panel
    ) {

        for (
                Component component :
                panel.getComponents()
        ) {

            if (
                    component instanceof JPanel
            ) {

                JPanel inner =
                        (JPanel) component;

                for (
                        Component child :
                        inner.getComponents()
                ) {

                    if (
                            child instanceof JLabel
                    ) {

                        JLabel label =
                                (JLabel) child;

                        if (
                                label.getFont()
                                        .getSize()
                                        >= 18
                        ) {

                            return label;
                        }
                    }
                }
            }
        }

        return new JLabel("0");
    }


    // =========================================================
    // ICON BOX
    // =========================================================

    private JLabel createIconBox(
            String text,
            Color color
    ) {

        JLabel icon =
                new JLabel(
                        text,
                        SwingConstants.CENTER
                );

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        icon.setForeground(
                WHITE
        );

        icon.setBackground(
                color
        );

        icon.setOpaque(
                true
        );

        icon.setPreferredSize(
                new Dimension(
                        48,
                        48
                )
        );

        return icon;
    }


    // =========================================================
    // FIELD PANEL
    // =========================================================

    private JPanel createFieldPanel(
            String labelText,
            JComponent field
    ) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(
                TEXT_GRAY
        );

        panel.add(
                label
        );

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(
                field
        );

        return panel;
    }


    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createTextField(
            String tooltip
    ) {

        JTextField field =
                new JTextField();

        styleField(
                field
        );

        field.setToolTipText(
                tooltip
        );

        return field;
    }


    // =========================================================
    // FIELD STYLE
    // =========================================================

    private void styleField(
            JComponent field
    ) {

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );
    }


    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                color
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        17,
                        10,
                        17
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }


    // =========================================================
    // LOAD STUDENTS
    // =========================================================

    private void loadStudents() {

        if (
                tableModel == null
        ) {
            return;
        }

        tableModel.setRowCount(
                0
        );

        String sql =
                "SELECT student_id, name, email, course, semester, cgpa " +
                        "FROM students ORDER BY student_id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (
                    resultSet.next()
            ) {

                tableModel.addRow(
                        new Object[]{
                                resultSet.getInt(
                                        "student_id"
                                ),

                                resultSet.getString(
                                        "name"
                                ),

                                resultSet.getString(
                                        "email"
                                ),

                                resultSet.getString(
                                        "course"
                                ),

                                resultSet.getInt(
                                        "semester"
                                ),
                                resultSet.getObject("cgpa") == null ? "—" : resultSet.getBigDecimal("cgpa")
                        }
                );
            }

            updateStudentCount();

        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: "
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // SEARCH STUDENTS
    // =========================================================

    private void searchStudents() {

        if (
                searchField == null
                        || tableModel == null
        ) {
            return;
        }

        String keyword =
                searchField.getText()
                        .trim()
                        .toLowerCase();

        if (
                keyword.isEmpty()
        ) {

            loadStudents();

            return;
        }

        tableModel.setRowCount(
                0
        );

        String sql =
                "SELECT student_id, name, email, course, semester, cgpa " +
                        "FROM students " +
                        "WHERE LOWER(name) LIKE ? " +
                        "OR LOWER(email) LIKE ? " +
                        "OR LOWER(course) LIKE ? " +
                        "OR CAST(semester AS CHAR) LIKE ? " +
                        "OR CAST(cgpa AS CHAR) LIKE ? " +
                        "ORDER BY student_id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String search =
                    "%" + keyword + "%";

            statement.setString(
                    1,
                    search
            );

            statement.setString(
                    2,
                    search
            );

            statement.setString(
                    3,
                    search
            );

            statement.setString(
                    4,
                    search
            );

            statement.setString(
                    5,
                    search
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    tableModel.addRow(
                            new Object[]{
                                    resultSet.getInt(
                                            "student_id"
                                    ),

                                    resultSet.getString(
                                            "name"
                                    ),

                                    resultSet.getString(
                                            "email"
                                    ),

                                    resultSet.getString(
                                            "course"
                                    ),

                                    resultSet.getInt(
                                            "semester"
                                    )
                            }
                    );
                }
            }

            updateVisibleCount();

        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search Error: "
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // UPDATE COUNTS
    // =========================================================

    private void updateStudentCount() {

        int count =
                tableModel.getRowCount();

        if (
                studentCountLabel != null
        ) {

            studentCountLabel.setText(
                    String.valueOf(
                            count
                    )
            );
        }

        updateVisibleCount();
    }


    private void updateVisibleCount() {

        if (
                visibleCountLabel != null
                        && tableModel != null
        ) {

            visibleCountLabel.setText(
                    String.valueOf(
                            tableModel.getRowCount()
                    )
            );
        }
    }


    // =========================================================
    // ADD STUDENT
    // =========================================================

    private void addStudent() {

        String name =
                nameField.getText()
                        .trim();

        String email =
                emailField.getText()
                        .trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String course =
                courseField.getText()
                        .trim();

        String semesterText =
                semesterField.getText()
                        .trim();

        String cgpaText = cgpaField.getText().trim();
        java.math.BigDecimal cgpa = null;
        if (!cgpaText.isEmpty()) {
            try {
                cgpa = new java.math.BigDecimal(cgpaText);
                if (cgpa.compareTo(java.math.BigDecimal.ZERO) < 0 || cgpa.compareTo(new java.math.BigDecimal("10.00")) > 0 || cgpa.scale() > 2) {
                    JOptionPane.showMessageDialog(this, "CGPA must be between 0.00 and 10.00 with at most 2 decimal places.", "Invalid CGPA", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid CGPA.", "Invalid CGPA", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }


        // =====================================================
        // VALIDATION
        // =====================================================

        if (
                name.isEmpty()
                        || email.isEmpty()
                        || password.isEmpty()
                        || course.isEmpty()
                        || semesterText.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (
                !email.contains("@")
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int semester;

        try {

            semester =
                    Integer.parseInt(
                            semesterText
                    );

            if (
                    semester < 1
                            || semester > 12
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid semester.",
                        "Invalid Semester",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Semester must be a number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // INSERT
        // =====================================================

        String sql =
                "INSERT INTO students " +
                        "(name, email, password, course, semester, cgpa) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    name
            );

            statement.setString(
                    2,
                    email
            );

            statement.setString(
                    3,
                    password
            );

            statement.setString(
                    4,
                    course
            );

            statement.setInt(
                    5,
                    semester
            );

            if (cgpa == null) {
                statement.setNull(6, java.sql.Types.DECIMAL);
            } else {
                statement.setBigDecimal(6, cgpa);
            }

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Student added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            loadStudents();

        } catch (
                SQLException e
        ) {

            String errorMessage =
                    e.getMessage();

            if (
                    errorMessage != null
                            && errorMessage
                            .toLowerCase()
                            .contains("duplicate")
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "This email is already registered.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: "
                                + errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    // =========================================================
    // EDIT SELECTED STUDENT
    // =========================================================

    private void editSelectedStudent() {

        int selectedRow =
                studentsTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student first.",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                studentsTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        int studentId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        String currentName =
                tableModel.getValueAt(
                        modelRow,
                        1
                ).toString();

        String currentEmail =
                tableModel.getValueAt(
                        modelRow,
                        2
                ).toString();

        String currentCourse =
                tableModel.getValueAt(
                        modelRow,
                        3
                ).toString();

        String currentSemester =
                tableModel.getValueAt(
                        modelRow,
                        4
                ).toString();


        JTextField name =
                createTextField("");

        name.setText(
                currentName
        );


        JTextField email =
                createTextField("");

        email.setText(
                currentEmail
        );


        JTextField course =
                createTextField("");

        course.setText(
                currentCourse
        );


        JTextField semester =
                createTextField("");

        semester.setText(
                currentSemester
        );

        JTextField cgpaEdit = createTextField("");
        Object currentCgpa = tableModel.getValueAt(selectedRow, 5);
        cgpaEdit.setText(currentCgpa == null || "—".equals(currentCgpa.toString()) ? "" : currentCgpa.toString());


        JPanel form =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                10
                        )
                );

        form.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        form.add(
                new JLabel(
                        "Name:"
                )
        );

        form.add(
                name
        );

        form.add(
                new JLabel(
                        "Email:"
                )
        );

        form.add(
                email
        );

        form.add(
                new JLabel(
                        "Course:"
                )
        );

        form.add(
                course
        );

        form.add(
                new JLabel(
                        "Semester:"
                )
        );

        form.add(
                semester
        );

        form.add(new JLabel("CGPA (0.00–10.00):"));
        form.add(cgpaEdit);


        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        form,
                        "Edit Student",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (
                result !=
                        JOptionPane.OK_OPTION
        ) {
            return;
        }


        String newName =
                name.getText()
                        .trim();

        String newEmail =
                email.getText()
                        .trim();

        String newCourse =
                course.getText()
                        .trim();

        String newSemesterText =
                semester.getText()
                        .trim();

        String newCgpaText = cgpaEdit.getText().trim();
        java.math.BigDecimal newCgpa = null;
        if (!newCgpaText.isEmpty()) {
            try {
                newCgpa = new java.math.BigDecimal(newCgpaText);
                if (newCgpa.compareTo(java.math.BigDecimal.ZERO) < 0 || newCgpa.compareTo(new java.math.BigDecimal("10.00")) > 0 || newCgpa.scale() > 2) {
                    JOptionPane.showMessageDialog(this, "CGPA must be between 0.00 and 10.00 with at most 2 decimal places.", "Invalid CGPA", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid CGPA.", "Invalid CGPA", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }


        if (
                newName.isEmpty()
                        || newEmail.isEmpty()
                        || newCourse.isEmpty()
                        || newSemesterText.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (
                !newEmail.contains("@")
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int newSemester;

        try {

            newSemester =
                    Integer.parseInt(
                            newSemesterText
                    );

            if (
                    newSemester < 1
                            || newSemester > 12
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid semester.",
                        "Invalid Semester",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Semester must be a number.",
                    "Invalid Semester",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================================
        // UPDATE
        // =====================================================

        String sql =
                "UPDATE students SET " +
                        "name = ?, " +
                        "email = ?, " +
                        "course = ?, " +
                        "semester = ?, " +
                        "cgpa = ? " +
                        "WHERE student_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    newName
            );

            statement.setString(
                    2,
                    newEmail
            );

            statement.setString(
                    3,
                    newCourse
            );

            statement.setInt(
                    4,
                    newSemester
            );

            if (newCgpa == null) {
                statement.setNull(5, java.sql.Types.DECIMAL);
            } else {
                statement.setBigDecimal(5, newCgpa);
            }

            statement.setInt(
                    6,
                    studentId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Student updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadStudents();

        } catch (
                SQLException e
        ) {

            String errorMessage =
                    e.getMessage();

            if (
                    errorMessage != null
                            && errorMessage
                            .toLowerCase()
                            .contains("duplicate")
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "This email is already registered.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: "
                                + errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    // =========================================================
    // DELETE SELECTED STUDENT
    // =========================================================

    private void deleteSelectedStudent() {

        int selectedRow =
                studentsTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student first.",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int modelRow =
                studentsTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        int studentId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        String studentName =
                tableModel.getValueAt(
                        modelRow,
                        1
                ).toString();


        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete\n"
                                + studentName
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                confirmation !=
                        JOptionPane.YES_OPTION
        ) {
            return;
        }


        String sql =
                "DELETE FROM students " +
                        "WHERE student_id = ?";


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    studentId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Student deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadStudents();

        } catch (
                SQLException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete student.\n\n"
                            + "Database Error: "
                            + e.getMessage(),
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        nameField.setText("");

        emailField.setText("");

        passwordField.setText("");

        courseField.setText("");

        semesterField.setText("");

        cgpaField.setText("");

        nameField.requestFocus();
    }


    // =========================================================
    // CUSTOM CAMPUSCONNECT BACKGROUND
    // Login + Announcements inspired
    // =========================================================

    private static class BackgroundPanel
            extends JPanel {

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            int width =
                    getWidth();

            int height =
                    getHeight();


            // =================================================
            // HIGH QUALITY RENDERING
            // =================================================

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );


            // =================================================
            // LIGHT BLUE / TEAL GRADIENT
            // =================================================

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            BG_TOP,
                            width,
                            height,
                            BG_BOTTOM
                    );

            g2.setPaint(
                    gradient
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );


            // =================================================
            // LARGE TOP-RIGHT WHITE CIRCLE
            // Similar to Announcement page
            // =================================================

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            135
                    )
            );

            g2.fillOval(
                    width - 430,
                    -210,
                    520,
                    520
            );


            // =================================================
            // SECOND TOP-RIGHT CIRCLE
            // =================================================

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            75
                    )
            );

            g2.fillOval(
                    width - 310,
                    -130,
                    380,
                    380
            );


            // =================================================
            // BOTTOM-LEFT LARGE CIRCLE
            // =================================================

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            100
                    )
            );

            g2.fillOval(
                    -210,
                    height - 300,
                    500,
                    500
            );


            // =================================================
            // BOTTOM-RIGHT SOFT TEAL CIRCLE
            // =================================================

            g2.setColor(
                    new Color(
                            24,
                            160,
                            174,
                            25
                    )
            );

            g2.fillOval(
                    width - 270,
                    height - 230,
                    380,
                    380
            );


            // =================================================
            // TOP-LEFT TEAL SHAPE
            // =================================================

            g2.setColor(
                    new Color(
                            24,
                            160,
                            174,
                            18
                    )
            );

            g2.fillOval(
                    -130,
                    -150,
                    400,
                    400
            );


            // =================================================
            // DECORATIVE WHITE ARC
            // =================================================

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            170
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            3f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawArc(
                    width - 350,
                    -140,
                    470,
                    470,
                    30,
                    105
            );


            // =================================================
            // SECOND TEAL ARC
            // =================================================

            g2.setColor(
                    new Color(
                            24,
                            160,
                            174,
                            40
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            2f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawArc(
                    width - 390,
                    -155,
                    510,
                    510,
                    30,
                    105
            );


            // =================================================
            // BOTTOM LEFT CURVE
            // =================================================

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            120
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            2f
                    )
            );

            g2.drawArc(
                    -190,
                    height - 250,
                    430,
                    430,
                    200,
                    110
            );


            // =================================================
            // SMALL DECORATIVE DOTS TOP RIGHT
            // =================================================

            g2.setColor(
                    new Color(
                            19,
                            57,
                            96,
                            30
                    )
            );

            int startX =
                    width - 180;

            int startY =
                    48;

            for (
                    int row = 0;
                    row < 4;
                    row++
            ) {

                for (
                        int col = 0;
                        col < 5;
                        col++
                ) {

                    int x =
                            startX
                                    + col * 15;

                    int y =
                            startY
                                    + row * 15;

                    g2.fillOval(
                            x,
                            y,
                            5,
                            5
                    );
                }
            }


            // =================================================
            // BOTTOM LEFT DOTS
            // =================================================

            g2.setColor(
                    new Color(
                            24,
                            160,
                            174,
                            35
                    )
            );

            int bottomY =
                    height - 75;

            for (
                    int i = 0;
                    i < 8;
                    i++
            ) {

                g2.fillOval(
                        45 + i * 15,
                        bottomY,
                        5,
                        5
                );
            }


            // =================================================
            // SMALL TEAL ACCENT CIRCLES
            // =================================================

            g2.setColor(
                    new Color(
                            24,
                            160,
                            174,
                            28
                    )
            );

            g2.fillOval(
                    width - 95,
                    145,
                    35,
                    35
            );

            g2.fillOval(
                    75,
                    height - 145,
                    25,
                    25
            );


            // =================================================
            // VERY SOFT CENTER HIGHLIGHT
            // =================================================

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            45
                    )
            );

            g2.fillOval(
                    width / 2 - 180,
                    height / 2 - 180,
                    360,
                    360
            );


            g2.dispose();
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CampusConnect - Manage Students");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1050, 700));
            frame.setContentPane(new StudentsFrame());
            frame.setSize(1250, 800);
            frame.setLocationRelativeTo(null);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }
}
