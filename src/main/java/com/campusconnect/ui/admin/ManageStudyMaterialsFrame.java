package com.campusconnect.ui.admin;

// * import com.campusconnect.util.StudyMaterialStorage;
import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ManageStudyMaterialsFrame extends JPanel {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color PAGE_BG =
            new Color(232, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color NAVY =
            new Color(24, 67, 108);

    private static final Color TEAL =
            new Color(24, 168, 181);

    private static final Color BLUE =
            new Color(55, 105, 180);

    private static final Color GREEN =
            new Color(27, 157, 91);

    private static final Color RED =
            new Color(215, 55, 55);

    private static final Color GRAY =
            new Color(100, 119, 138);

    private static final Color TEXT =
            new Color(38, 57, 76);

    private static final Color MUTED =
            new Color(96, 117, 137);

    private static final Color BORDER =
            new Color(205, 222, 232);

    // =========================================================
    // RESOURCE DIRECTORY
    // =========================================================

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

    private final Runnable onClose;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ManageStudyMaterialsFrame() {
        this(() -> { });
    }

    public ManageStudyMaterialsFrame(Runnable onClose) {
        this.onClose = onClose == null ? () -> { } : onClose;
        setLayout(new BorderLayout());
        buildUI();
        loadMaterials();
    }

    // =========================================================
    // MAIN UI
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(PAGE_BG);

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(PAGE_BG);

        content.setBorder(
                new EmptyBorder(
                        15,
                        35,
                        25,
                        35
                )
        );

        content.add(
                createStatsPanel()
        );

        content.add(
                Box.createVerticalStrut(15)
        );

        content.add(
                createMaterialsCard()
        );

        content.add(
                Box.createVerticalStrut(15)
        );

        content.add(
                createAddMaterialCard()
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        content
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        root.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(root);
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
                                14,
                                35,
                                14,
                                35
                        )
                )
        );

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.X_AXIS
                )
        );

        left.setBackground(WHITE);

        JPanel iconBox =
                createIconBox(
                        "▤",
                        TEAL,
                        68
                );

        left.add(iconBox);

        left.add(
                Box.createHorizontalStrut(16)
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(WHITE);

        JLabel brand =
                new JLabel(
                        "CAMPUSCONNECT"
                );

        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        brand.setForeground(TEAL);

        JLabel title =
                new JLabel(
                        "Manage Study Materials"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        29
                )
        );

        title.setForeground(NAVY);

        JLabel subtitle =
                new JLabel(
                        "Add, organize and manage learning resources"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(MUTED);

        titlePanel.add(brand);

        titlePanel.add(
                Box.createVerticalStrut(2)
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(subtitle);

        left.add(titlePanel);

        header.add(
                left,
                BorderLayout.WEST
        );

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        right.setBackground(WHITE);

        JPanel ccBox =
                createIconBox(
                        "CC",
                        NAVY,
                        52
                );

        JLabel adminLabel =
                new JLabel(
                        "<html><b>CampusConnect</b>"
                                + "<br><font color='#60758A'>Admin Portal</font></html>"
                );

        adminLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        right.add(ccBox);

        right.add(adminLabel);

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // ICON BOX
    // =========================================================

    private JPanel createIconBox(
            String icon,
            Color background,
            int size
    ) {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(background);

        panel.setPreferredSize(
                new Dimension(
                        size,
                        size
                )
        );

        panel.setMinimumSize(
                new Dimension(
                        size,
                        size
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        size,
                        size
                )
        );

        JLabel label =
                new JLabel(icon);

        label.setForeground(WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        size >= 65
                                ? 29
                                : 19
                )
        );

        panel.add(label);

        return panel;
    }

    // =========================================================
    // STATS
    // =========================================================

    private JPanel createStatsPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        panel.setOpaque(false);

        totalMaterialsValue =
                createStatValue();

        subjectCountValue =
                createStatValue();

        latestMaterialValue =
                createStatValue();

        panel.add(
                createStatCard(
                        "▤",
                        "TOTAL MATERIALS",
                        totalMaterialsValue,
                        TEAL
                )
        );

        panel.add(
                createStatCard(
                        "◈",
                        "TOTAL SUBJECTS",
                        subjectCountValue,
                        BLUE
                )
        );

        panel.add(
                createStatCard(
                        "★",
                        "LATEST MATERIAL",
                        latestMaterialValue,
                        NAVY
                )
        );

        return panel;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String icon,
            String title,
            JLabel value,
            Color accent
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                12,
                                14,
                                12,
                                14
                        )
                )
        );

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setOpaque(true);

        iconLabel.setBackground(
                new Color(
                        235,
                        248,
                        251
                )
        );

        iconLabel.setForeground(accent);

        iconLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        55,
                        55
                )
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setBackground(WHITE);

        text.setBorder(
                new EmptyBorder(
                        3,
                        12,
                        0,
                        0
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        titleLabel.setForeground(MUTED);

        value.setForeground(accent);

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(5)
        );

        text.add(value);

        card.add(
                iconLabel,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // STAT VALUE
    // =========================================================

    private JLabel createStatValue() {

        JLabel label =
                new JLabel("0");

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        return label;
    }

    // =========================================================
    // MATERIALS CARD
    // =========================================================

    private JPanel createMaterialsCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                16,
                                20,
                                14,
                                20
                        )
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(WHITE);

        JPanel heading =
                new JPanel();

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.X_AXIS
                )
        );

        heading.setBackground(WHITE);

        JLabel icon =
                new JLabel("▤");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        icon.setForeground(TEAL);

        heading.add(icon);

        heading.add(
                Box.createHorizontalStrut(9)
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setBackground(WHITE);

        JLabel title =
                new JLabel(
                        "Study Materials Library"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(NAVY);

        JLabel subtitle =
                new JLabel(
                        "View and manage all available learning resources"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(MUTED);

        text.add(title);

        text.add(
                Box.createVerticalStrut(2)
        );

        text.add(subtitle);

        heading.add(text);

        top.add(
                heading,
                BorderLayout.WEST
        );

        // =====================================================
        // SEARCH
        // =====================================================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        searchPanel.setBackground(WHITE);

        JLabel searchIcon =
                new JLabel("⌕");

        searchIcon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        searchIcon.setForeground(NAVY);

        searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        230,
                        34
                )
        );

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        searchField.setToolTipText(
                "Search title, subject or description"
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                0,
                                8,
                                0,
                                8
                        )
                )
        );

        JButton refreshButton =
                createButton(
                        "↻  Refresh",
                        BLUE,
                        WHITE,
                        105,
                        34
                );

        searchPanel.add(searchIcon);

        searchPanel.add(searchField);

        searchPanel.add(refreshButton);

        top.add(
                searchPanel,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "ID",
                "TITLE",
                "SUBJECT",
                "DESCRIPTION",
                "FILE"
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

        materialsTable =
                new JTable(
                        tableModel
                );

        materialsTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        materialsTable.setForeground(TEXT);

        materialsTable.setBackground(WHITE);

        materialsTable.setRowHeight(42);

        materialsTable.setShowGrid(true);

        materialsTable.setGridColor(
                new Color(
                        220,
                        231,
                        238
                )
        );

        materialsTable.setSelectionBackground(
                new Color(
                        222,
                        241,
                        247
                )
        );

        materialsTable.setSelectionForeground(NAVY);

        materialsTable.setAutoCreateRowSorter(
                true
        );

        materialsTable.setFillsViewportHeight(
                true
        );

        materialsTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        materialsTable.getTableHeader()
                .setForeground(WHITE);

        materialsTable.getTableHeader()
                .setBackground(NAVY);

        materialsTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                42
                        )
                );

        materialsTable.getTableHeader()
                .setReorderingAllowed(false);

        materialsTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        materialsTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(220);

        materialsTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(150);

        materialsTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(350);

        materialsTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(280);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        materialsTable.getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        centerRenderer
                );

        JScrollPane tableScroll =
                new JScrollPane(
                        materialsTable
                );

        tableScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        tableScroll.setPreferredSize(
                new Dimension(
                        950,
                        310
                )
        );

        card.add(
                tableScroll,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM ACTIONS
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        bottom.setBackground(WHITE);

        JButton clearSelectionButton =
                createButton(
                        "⌫  Clear Selection",
                        GRAY,
                        WHITE,
                        145,
                        38
                );

        JButton deleteButton =
                createButton(
                        "▣  Delete Selected",
                        RED,
                        WHITE,
                        155,
                        38
                );

        bottom.add(
                clearSelectionButton
        );

        bottom.add(deleteButton);

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        // =====================================================
        // ACTIONS
        // =====================================================

        refreshButton.addActionListener(
                e -> loadMaterials()
        );

        clearSelectionButton.addActionListener(
                e -> {
                    materialsTable.clearSelection();
                    clearForm();
                }
        );

        deleteButton.addActionListener(
                e -> deleteMaterial()
        );

        searchField.getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            private void search() {

                                filterMaterials(
                                        searchField
                                                .getText()
                                                .trim()
                                );
                            }

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                search();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                search();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                search();
                            }
                        }
                );

        materialsTable.getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (
                                    !e.getValueIsAdjusting()
                                            &&
                                            materialsTable
                                                    .getSelectedRow()
                                                    >= 0
                            ) {

                                int row =
                                        materialsTable
                                                .convertRowIndexToModel(
                                                        materialsTable
                                                                .getSelectedRow()
                                                );

                                loadSelectedMaterial(
                                        row
                                );
                            }
                        }
                );

        return card;
    }

    // =========================================================
    // ADD MATERIAL CARD
    // =========================================================

    private JPanel createAddMaterialCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                16,
                                20,
                                16,
                                20
                        )
                )
        );

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(WHITE);

        JPanel heading =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        heading.setBackground(WHITE);

        JLabel icon =
                new JLabel("＋");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        icon.setForeground(TEAL);

        heading.add(icon);

        heading.add(
                Box.createHorizontalStrut(8)
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(WHITE);

        JLabel title =
                new JLabel(
                        "Add New Study Material"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(NAVY);

        JLabel subtitle =
                new JLabel(
                        "Add a new learning resource for students"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(MUTED);

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(2)
        );

        titlePanel.add(subtitle);

        heading.add(titlePanel);

        header.add(
                heading,
                BorderLayout.WEST
        );

        JLabel badge =
                new JLabel(
                        "NEW MATERIAL"
                );

        badge.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        badge.setForeground(TEAL);

        header.add(
                badge,
                BorderLayout.EAST
        );

        card.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // FORM
        // =====================================================

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(WHITE);

        form.setBorder(
                new EmptyBorder(
                        12,
                        0,
                        8,
                        0
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        5,
                        6,
                        12
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // =====================================================
        // TITLE
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "▣  TITLE"
                ),
                gbc
        );

        titleField =
                createTextField();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        form.add(
                titleField,
                gbc
        );

        // =====================================================
        // SUBJECT
        // =====================================================

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "◈  SUBJECT"
                ),
                gbc
        );

        subjectField =
                createTextField();

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.8;

        form.add(
                subjectField,
                gbc
        );

        // =====================================================
        // FILE / URL
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "▤  FILE / URL"
                ),
                gbc
        );

        JPanel filePanel =
                new JPanel(
                        new BorderLayout(
                                6,
                                0
                        )
                );

        filePanel.setBackground(WHITE);

        fileField =
                createTextField();

        fileField.setToolTipText(
                "Local resource path or Google Drive / web URL"
        );

        JButton browseButton =
                createButton(
                        "▣  Browse",
                        BLUE,
                        WHITE,
                        100,
                        34
                );

        filePanel.add(
                fileField,
                BorderLayout.CENTER
        );

        filePanel.add(
                browseButton,
                BorderLayout.EAST
        );

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        form.add(
                filePanel,
                gbc
        );

        browseButton.addActionListener(
                e -> browseFile()
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "▤  DESCRIPTION"
                ),
                gbc
        );

        descriptionField =
                createTextField();

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.8;

        form.add(
                descriptionField,
                gbc
        );

        card.add(
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
                                8,
                                0
                        )
                );

        buttons.setBackground(WHITE);

        JButton clearButton =
                createButton(
                        "⌫  Clear",
                        GRAY,
                        WHITE,
                        105,
                        38
                );

        JButton addButton =
                createButton(
                        "＋  Add Material",
                        TEAL,
                        WHITE,
                        150,
                        38
                );

        JButton closeButton =
                createButton(
                        "×  Close",
                        NAVY,
                        WHITE,
                        105,
                        38
                );

        buttons.add(clearButton);

        buttons.add(addButton);

        buttons.add(closeButton);

        card.add(
                buttons,
                BorderLayout.SOUTH
        );

        // =====================================================
        // ACTIONS
        // =====================================================

        clearButton.addActionListener(
                e -> clearForm()
        );

        addButton.addActionListener(
                e -> addMaterial()
        );

        closeButton.addActionListener(
                e -> onClose.run()
        );

        return card;
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                new Color(
                        70,
                        95,
                        118
                )
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(TEXT);

        field.setBackground(WHITE);

        field.setPreferredSize(
                new Dimension(
                        180,
                        34
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                0,
                                8,
                                0,
                                8
                        )
                )
        );

        return field;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground,
            int width,
            int height
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(foreground);

        button.setBackground(background);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        return button;
    }

    // =========================================================
    // LOAD MATERIALS
    // =========================================================

    private void loadMaterials() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT * FROM study_materials "
                        + "ORDER BY material_id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int id =
                        resultSet.getInt(
                                "material_id"
                        );

                String title =
                        resultSet.getString(
                                "title"
                        );

                String subject =
                        resultSet.getString(
                                "subject"
                        );

                String description =
                        resultSet.getString(
                                "description"
                        );

                String filePath =
                        resultSet.getString(
                                "file_path"
                        );

                tableModel.addRow(
                        new Object[]{
                                id,
                                title,
                                subject,
                                description,
                                filePath
                        }
                );
            }

            updateStatistics();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private void updateStatistics() {

        int total =
                tableModel.getRowCount();

        Set<String> subjects =
                new HashSet<>();

        String latest =
                "-";

        if (total > 0) {

            Object latestValue =
                    tableModel.getValueAt(
                            total - 1,
                            1
                    );

            if (latestValue != null) {

                latest =
                        latestValue.toString();
            }
        }

        for (
                int i = 0;
                i < total;
                i++
        ) {

            Object subject =
                    tableModel.getValueAt(
                            i,
                            2
                    );

            if (
                    subject != null
                            &&
                            !subject
                                    .toString()
                                    .trim()
                                    .isEmpty()
            ) {

                subjects.add(
                        subject
                                .toString()
                                .trim()
                                .toLowerCase()
                );
            }
        }

        totalMaterialsValue.setText(
                String.valueOf(total)
        );

        subjectCountValue.setText(
                String.valueOf(
                        subjects.size()
                )
        );

        latestMaterialValue.setText(
                latest
        );
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void filterMaterials(
            String text
    ) {

        if (text.isEmpty()) {

            loadMaterials();

            return;
        }

        tableModel.setRowCount(0);

        String sql =
                "SELECT * FROM study_materials "
                        + "WHERE LOWER(title) LIKE ? "
                        + "OR LOWER(subject) LIKE ? "
                        + "OR LOWER(description) LIKE ? "
                        + "OR LOWER(file_path) LIKE ? "
                        + "ORDER BY material_id";

        String search =
                "%"
                        + text.toLowerCase()
                        + "%";

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

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    tableModel.addRow(
                            new Object[]{
                                    resultSet.getInt(
                                            "material_id"
                                    ),

                                    resultSet.getString(
                                            "title"
                                    ),

                                    resultSet.getString(
                                            "subject"
                                    ),

                                    resultSet.getString(
                                            "description"
                                    ),

                                    resultSet.getString(
                                            "file_path"
                                    )
                            }
                    );
                }
            }

            updateStatistics();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search Error:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // BROWSE FILE
    // =========================================================

    private void browseFile() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Select Study Material"
        );

        // Start chooser inside study_materials
        File resourceFolder =
                new File(
                        RESOURCE_FOLDER
                );

        if (resourceFolder.exists()) {

            chooser.setCurrentDirectory(
                    resourceFolder
            );
        }

        int result =
                chooser.showOpenDialog(
                        this
                );

        if (
                result
                        != JFileChooser.APPROVE_OPTION
        ) {
            return;
        }

        File selectedFile =
                chooser.getSelectedFile();

        // =====================================================
        // CHECK ONLINE URL
        // =====================================================

        if (
                isWebUrl(
                        selectedFile.getAbsolutePath()
                )
        ) {
            fileField.setText(
                    selectedFile.getAbsolutePath()
            );

            return;
        }

        // =====================================================
        // RESOURCE FOLDER
        // =====================================================

        File resourceRoot =
                new File(
                        RESOURCE_FOLDER
                );

        try {

            String rootPath =
                    resourceRoot
                            .getCanonicalPath();

            String selectedPath =
                    selectedFile
                            .getCanonicalPath();

            // =================================================
            // CHECK FILE INSIDE RESOURCE FOLDER
            // =================================================

            if (
                    !isInsideDirectory(
                            selectedFile,
                            resourceRoot
                    )
            ) {

                int choice =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Selected file is outside:\n\n"
                                        + RESOURCE_FOLDER
                                        + "\n\n"
                                        + "For CampusConnect local resources, "
                                        + "place the file inside the study_materials "
                                        + "folder or one of its subfolders.\n\n"
                                        + "Do you want to use the absolute path anyway?",
                                "File Outside Resource Folder",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );

                if (
                        choice
                                == JOptionPane.YES_OPTION
                ) {

                    fileField.setText(
                            selectedPath
                    );
                }

                return;
            }

            // =================================================
            // CREATE RELATIVE PATH
            // =================================================

            String relativePath =
                    rootPath
                            .equals(
                                    selectedPath
                            )
                            ? selectedFile.getName()
                            : selectedPath.substring(
                            rootPath.length()
                    );

            relativePath =
                    relativePath
                            .replace(
                                    File.separatorChar,
                                    '/'
                            );

            if (
                    relativePath.startsWith("/")
            ) {

                relativePath =
                        relativePath.substring(1);
            }

            fileField.setText(
                    relativePath
            );

            // =================================================
            // SHOW SUCCESS INFORMATION
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Resource selected successfully.\n\n"
                            + "Saved Path:\n"
                            + relativePath,
                    "Resource Selected",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to calculate resource path:\n"
                            + e.getMessage(),
                    "Path Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CHECK INSIDE DIRECTORY
    // =========================================================

    private boolean isInsideDirectory(
            File file,
            File directory
    ) {

        try {

            String filePath =
                    file.getCanonicalPath();

            String directoryPath =
                    directory.getCanonicalPath();

            return filePath.equals(
                    directoryPath
            )
                    ||
                    filePath.startsWith(
                            directoryPath
                                    + File.separator
                    );

        } catch (Exception e) {

            return false;
        }
    }

    // =========================================================
    // WEB URL CHECK
    // =========================================================

    private boolean isWebUrl(
            String value
    ) {

        try {

            URI uri =
                    new URI(
                            value
                    );

            String scheme =
                    uri.getScheme();

            return scheme != null
                    &&
                    (
                            scheme.equalsIgnoreCase(
                                    "http"
                            )
                                    ||
                                    scheme.equalsIgnoreCase(
                                            "https"
                                    )
                    );

        } catch (Exception e) {

            return false;
        }
    }

    // =========================================================
    // ADD MATERIAL
    // =========================================================

    private void addMaterial() {

        String title =
                titleField
                        .getText()
                        .trim();

        String subject =
                subjectField
                        .getText()
                        .trim();

        String description =
                descriptionField
                        .getText()
                        .trim();

        String file =
                fileField
                        .getText()
                        .trim();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (
                title.isEmpty()
                        ||
                        subject.isEmpty()
                        ||
                        description.isEmpty()
                        ||
                        file.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =====================================================
        // LOCAL FILE VALIDATION
        // =====================================================

        if (!isWebUrl(file)) {

            File resourceFile =
                    new File(
                            RESOURCE_FOLDER,
                            file.replace(
                                    "/",
                                    File.separator
                            )
                    );

            if (!resourceFile.exists()) {

                int choice =
                        JOptionPane.showConfirmDialog(
                                this,
                                "The selected local resource was not found:\n\n"
                                        + resourceFile.getAbsolutePath()
                                        + "\n\n"
                                        + "Do you still want to save this path?",
                                "Resource Not Found",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );

                if (
                        choice
                                != JOptionPane.YES_OPTION
                ) {

                    return;
                }
            }
        }

        // =====================================================
        // INSERT
        // =====================================================

        String sql =
                "INSERT INTO study_materials "
                        + "(title, subject, description, file_path) "
                        + "VALUES (?, ?, ?, ?)";

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
                    title
            );

            statement.setString(
                    2,
                    subject
            );

            statement.setString(
                    3,
                    description
            );

            statement.setString(
                    4,
                    file
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Study Material Added Successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();

            loadMaterials();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error adding material:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SELECTED MATERIAL
    // =========================================================

    private void loadSelectedMaterial(
            int row
    ) {

        Object title =
                tableModel.getValueAt(
                        row,
                        1
                );

        Object subject =
                tableModel.getValueAt(
                        row,
                        2
                );

        Object description =
                tableModel.getValueAt(
                        row,
                        3
                );

        Object file =
                tableModel.getValueAt(
                        row,
                        4
                );

        titleField.setText(
                title == null
                        ? ""
                        : title.toString()
        );

        subjectField.setText(
                subject == null
                        ? ""
                        : subject.toString()
        );

        descriptionField.setText(
                description == null
                        ? ""
                        : description.toString()
        );

        fileField.setText(
                file == null
                        ? ""
                        : file.toString()
        );
    }

    // =========================================================
    // DELETE MATERIAL
    // =========================================================

    private void deleteMaterial() {

        int selectedRow =
                materialsTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a material to delete.",
                    "No Material Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                materialsTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        int materialId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        String materialTitle =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                1
                        )
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this material?\n\n"
                                + materialTitle,
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                confirmation
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        String sql =
                "DELETE FROM study_materials "
                        + "WHERE material_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    materialId
            );

            int rowsDeleted =
                    statement.executeUpdate();

            if (
                    rowsDeleted > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Study Material Deleted Successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();

                loadMaterials();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Study Material not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting material:\n"
                            + e.getMessage(),
                    "Database Error",
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

        if (
                materialsTable != null
        ) {

            materialsTable.clearSelection();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CampusConnect - Manage Study Materials");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new ManageStudyMaterialsFrame());
            frame.setMinimumSize(new Dimension(1100, 700));
            frame.setSize(1200, 750);
            frame.setLocationRelativeTo(null);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }

}
