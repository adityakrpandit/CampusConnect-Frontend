package com.campusconnect.ui.student;

import com.campusconnect.config.DatabaseConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * CampusConnect - Student Doubts & Queries
 *
 * Features:
 * - Modern CampusConnect UI
 * - Student doubts loaded from database
 * - Ask a new doubt
 * - Attach image
 * - Image preview
 * - View attached image
 * - Search doubts
 * - Pending / Resolved status
 * - Total / Pending / Resolved statistics
 * - Double-click doubt for details
 * - Refresh doubts
 * - Larger and more visible doubts table
 */
public class DoubtFrame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(243, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color NAVY =
            new Color(19, 57, 96);

    private static final Color NAVY_LIGHT =
            new Color(31, 78, 121);

    private static final Color TEAL =
            new Color(21, 157, 171);

    private static final Color TEAL_LIGHT =
            new Color(225, 247, 249);

    private static final Color GREEN =
            new Color(32, 153, 91);

    private static final Color GREEN_LIGHT =
            new Color(228, 247, 236);

    private static final Color ORANGE =
            new Color(224, 139, 34);

    private static final Color ORANGE_LIGHT =
            new Color(255, 244, 224);

    private static final Color RED =
            new Color(207, 73, 73);

    private static final Color TEXT_DARK =
            new Color(28, 42, 58);

    private static final Color TEXT_GRAY =
            new Color(102, 116, 134);

    private static final Color BORDER =
            new Color(218, 226, 235);

    private static final Color TABLE_HEADER =
            new Color(23, 66, 108);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTable doubtTable;
    private DefaultTableModel tableModel;

    private JTextField subjectField;
    private JTextArea questionArea;
    private JTextField searchField;

    private JButton askButton;
    private JButton refreshButton;
    private JButton attachButton;
    private JButton removeImageButton;
    private JButton viewImageButton;

    private JLabel selectedImageLabel;
    private JLabel studentLabel;

    private JLabel totalValueLabel;
    private JLabel pendingValueLabel;
    private JLabel resolvedValueLabel;

    // =========================================================
    // STUDENT
    // =========================================================

    private final String studentEmail;
    private String studentName;

    // =========================================================
    // IMAGE
    // =========================================================

    private String selectedImagePath;

    // Render backend API
    private static final String API_BASE_URL =
            "https://campusconnect-backend-grn0.onrender.com/api/doubts";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<JsonNode> allDoubts = new ArrayList<>();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DoubtFrame(String studentEmail) {

        this.studentEmail =
                studentEmail == null
                        ? ""
                        : studentEmail.trim();

        setTitle("CampusConnect - Doubts & Queries");

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(1100, 720)
        );

        setLayout(
                new BorderLayout()
        );

        // =====================================================
        // MAIN PANEL
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        // =====================================================
        // HEADER
        // =====================================================

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER CONTENT
        // =====================================================

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(0, 14)
                );

        contentPanel.setBackground(
                BACKGROUND
        );

        contentPanel.setBorder(
                new EmptyBorder(
                        16,
                        30,
                        22,
                        30
                )
        );

        // =====================================================
        // STATISTICS
        // =====================================================

        contentPanel.add(
                createStatisticsPanel(),
                BorderLayout.NORTH
        );

        // =====================================================
        // MIDDLE
        // =====================================================

        JPanel middlePanel =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        middlePanel.setOpaque(false);

        // Search
        middlePanel.add(
                createSearchPanel(),
                BorderLayout.NORTH
        );

        // Table
        middlePanel.add(
                createTablePanel(),
                BorderLayout.CENTER
        );

        contentPanel.add(
                middlePanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // ASK DOUBT
        // =====================================================

        contentPanel.add(
                createAskDoubtPanel(),
                BorderLayout.SOUTH
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =====================================================
        // LOAD STUDENT
        // =====================================================

        loadStudentName();

        // =====================================================
        // WINDOW
        // =====================================================

        setSize(
                1200,
                820
        );

        setLocationRelativeTo(null);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setVisible(true);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                WHITE
        );

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
                                30,
                                18,
                                30
                        )
                )
        );

        // -----------------------------------------------------
        // LEFT
        // -----------------------------------------------------

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.setOpaque(false);

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

        brand.setForeground(
                TEAL
        );

        JLabel title =
                new JLabel(
                        "Doubts & Queries"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                NAVY
        );

        JLabel subtitle =
                new JLabel(
                        "Ask questions, attach images and track your answers"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                TEXT_GRAY
        );

        left.add(brand);

        left.add(
                Box.createVerticalStrut(4)
        );

        left.add(title);

        left.add(
                Box.createVerticalStrut(4)
        );

        left.add(subtitle);

        header.add(
                left,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // RIGHT
        // -----------------------------------------------------

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        right.setOpaque(false);

        studentLabel =
                new JLabel(
                        "Student"
                );

        studentLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        studentLabel.setForeground(
                TEXT_GRAY
        );

        refreshButton =
                createButton(
                        "Refresh",
                        NAVY
                );

        refreshButton.addActionListener(
                e -> {

                    if (
                            studentName != null &&
                                    !studentName.trim().isEmpty()
                    ) {
                        loadDoubts();
                    }
                }
        );

        right.add(studentLabel);
        right.add(refreshButton);

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private JPanel createStatisticsPanel() {

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );

        stats.setOpaque(false);

        // -----------------------------------------------------
        // TOTAL
        // -----------------------------------------------------

        RoundedPanel totalCard =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        18
                );

        totalCard.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );

        JLabel totalIcon =
                createStatIcon(
                        "Q",
                        NAVY
                );

        JPanel totalText =
                new JPanel();

        totalText.setOpaque(false);

        totalText.setLayout(
                new BoxLayout(
                        totalText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel totalTitle =
                createSmallLabel(
                        "TOTAL DOUBTS"
                );

        totalValueLabel =
                createValueLabel(
                        "0"
                );

        totalText.add(totalTitle);

        totalText.add(
                Box.createVerticalStrut(4)
        );

        totalText.add(totalValueLabel);

        totalCard.add(
                totalIcon,
                BorderLayout.WEST
        );

        totalCard.add(
                totalText,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // PENDING
        // -----------------------------------------------------

        RoundedPanel pendingCard =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        18
                );

        pendingCard.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );

        JLabel pendingIcon =
                createStatIcon(
                        "P",
                        ORANGE
                );

        JPanel pendingText =
                new JPanel();

        pendingText.setOpaque(false);

        pendingText.setLayout(
                new BoxLayout(
                        pendingText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel pendingTitle =
                createSmallLabel(
                        "PENDING"
                );

        pendingValueLabel =
                createValueLabel(
                        "0"
                );

        pendingText.add(pendingTitle);

        pendingText.add(
                Box.createVerticalStrut(4)
        );

        pendingText.add(pendingValueLabel);

        pendingCard.add(
                pendingIcon,
                BorderLayout.WEST
        );

        pendingCard.add(
                pendingText,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // RESOLVED
        // -----------------------------------------------------

        RoundedPanel resolvedCard =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        18
                );

        resolvedCard.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );

        JLabel resolvedIcon =
                createStatIcon(
                        "R",
                        GREEN
                );

        JPanel resolvedText =
                new JPanel();

        resolvedText.setOpaque(false);

        resolvedText.setLayout(
                new BoxLayout(
                        resolvedText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel resolvedTitle =
                createSmallLabel(
                        "RESOLVED"
                );

        resolvedValueLabel =
                createValueLabel(
                        "0"
                );

        resolvedText.add(resolvedTitle);

        resolvedText.add(
                Box.createVerticalStrut(4)
        );

        resolvedText.add(resolvedValueLabel);

        resolvedCard.add(
                resolvedIcon,
                BorderLayout.WEST
        );

        resolvedCard.add(
                resolvedText,
                BorderLayout.CENTER
        );

        stats.add(totalCard);
        stats.add(pendingCard);
        stats.add(resolvedCard);

        return stats;
    }

    // =========================================================
    // SEARCH PANEL
    // =========================================================

    private JPanel createSearchPanel() {

        RoundedPanel searchPanel =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        16
                );

        searchPanel.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        searchPanel.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        JLabel searchTitle =
                new JLabel(
                        "Search"
                );

        searchTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        searchTitle.setForeground(
                TEXT_DARK
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

        searchField.setPreferredSize(
                new Dimension(
                        350,
                        36
                )
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        JLabel hint =
                new JLabel(
                        "Search by subject, question, status or answer"
                );

        hint.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        hint.setForeground(
                TEXT_GRAY
        );

        JButton clearButton =
                createOutlineButton(
                        "Clear"
                );

        clearButton.addActionListener(
                e -> {

                    searchField.setText("");

                    filterDoubts();
                }
        );

        JPanel searchLeft =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        searchLeft.setOpaque(false);

        searchLeft.add(searchTitle);
        searchLeft.add(searchField);
        searchLeft.add(hint);

        searchPanel.add(
                searchLeft,
                BorderLayout.CENTER
        );

        searchPanel.add(
                clearButton,
                BorderLayout.EAST
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                filterDoubts();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                filterDoubts();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                filterDoubts();
                            }
                        }
                );

        return searchPanel;
    }

    // =========================================================
    // TABLE PANEL
    // =========================================================

    private JPanel createTablePanel() {

        RoundedPanel tablePanel =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        18
                );

        tablePanel.setLayout(
                new BorderLayout(
                        0,
                        8
                )
        );

        /*
         * IMPORTANT:
         * Larger preferred/minimum height so the table
         * does not become too small inside the dashboard.
         */
        tablePanel.setPreferredSize(
                new Dimension(
                        1000,
                        390
                )
        );

        tablePanel.setMinimumSize(
                new Dimension(
                        900,
                        300
                )
        );

        tablePanel.setBorder(
                new EmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        // -----------------------------------------------------
        // TABLE TITLE
        // -----------------------------------------------------

        JPanel tableTitlePanel =
                new JPanel(
                        new BorderLayout()
                );

        tableTitlePanel.setOpaque(false);

        JLabel tableTitle =
                new JLabel(
                        "Your Doubts"
                );

        tableTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        tableTitle.setForeground(
                TEXT_DARK
        );

        JLabel tableHint =
                new JLabel(
                        "Double-click any doubt to view complete details"
                );

        tableHint.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        tableHint.setForeground(
                TEXT_GRAY
        );

        tableTitlePanel.add(
                tableTitle,
                BorderLayout.WEST
        );

        tableTitlePanel.add(
                tableHint,
                BorderLayout.EAST
        );

        tablePanel.add(
                tableTitlePanel,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // TABLE MODEL
        // -----------------------------------------------------

        String[] columns = {
                "ID",
                "SUBJECT",
                "QUESTION",
                "STATUS",
                "ANSWER",
                "ATTACHMENT",
                "ANSWERED BY"
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

        // -----------------------------------------------------
        // TABLE
        // -----------------------------------------------------

        doubtTable =
                new JTable(
                        tableModel
                );

        /*
         * Increased row height.
         * This gives Question and Answer more vertical space.
         */
        doubtTable.setRowHeight(
                88
        );

        doubtTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        doubtTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        doubtTable.setShowGrid(
                true
        );

        doubtTable.setGridColor(
                new Color(
                        230,
                        235,
                        241
                )
        );

        doubtTable.setIntercellSpacing(
                new Dimension(
                        0,
                        1
                )
        );

        doubtTable.setSelectionBackground(
                new Color(
                        228,
                        244,
                        247
                )
        );

        doubtTable.setSelectionForeground(
                TEXT_DARK
        );

        /*
         * Keep our preferred column widths.
         * Horizontal scrolling will be available if required.
         */
        doubtTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        doubtTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        doubtTable
                .getTableHeader()
                .setBackground(
                        TABLE_HEADER
                );

        doubtTable
                .getTableHeader()
                .setForeground(
                        WHITE
                );

        doubtTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                44
                        )
                );

        // -----------------------------------------------------
        // COLUMN WIDTHS
        // -----------------------------------------------------

        doubtTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60);

        doubtTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(170);

        doubtTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(390);

        doubtTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        doubtTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(420);

        doubtTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(150);

        doubtTable
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(170);

        // -----------------------------------------------------
        // RENDERERS
        // -----------------------------------------------------

        doubtTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        new CenterRenderer()
                );

        doubtTable
                .getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        new SubjectRenderer()
                );

        doubtTable
                .getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        new TextAreaRenderer()
                );

        doubtTable
                .getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new StatusRenderer()
                );

        doubtTable
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new TextAreaRenderer()
                );

        doubtTable
                .getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        new AttachmentRenderer()
                );

        doubtTable
                .getColumnModel()
                .getColumn(6)
                .setCellRenderer(
                        new DefaultTableCellRenderer()
                );

        // -----------------------------------------------------
        // SCROLL PANE
        // -----------------------------------------------------

        JScrollPane scrollPane =
                new JScrollPane(
                        doubtTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // DOUBLE CLICK
        // -----------------------------------------------------

        doubtTable.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        if (
                                e.getClickCount() == 2 &&
                                        SwingUtilities.isLeftMouseButton(e)
                        ) {

                            int row =
                                    doubtTable.rowAtPoint(
                                            e.getPoint()
                                    );

                            if (row >= 0) {
                                showDoubtDetails(row);
                            }
                        }
                    }
                }
        );

        return tablePanel;
    }

    // =========================================================
    // ASK DOUBT PANEL
    // =========================================================

    private JPanel createAskDoubtPanel() {

        RoundedPanel panel =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        18
                );

        panel.setLayout(
                new BorderLayout(
                        15,
                        8
                )
        );

        /*
         * Keep this section compact so the table gets
         * more vertical space.
         */
        panel.setPreferredSize(
                new Dimension(
                        1000,
                        225
                )
        );

        panel.setMinimumSize(
                new Dimension(
                        800,
                        210
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Ask a New Doubt"
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
                        "Describe your question clearly. Attach an image if it helps explain the problem."
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

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(2)
        );

        titlePanel.add(subtitle);

        panel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // FORM
        // -----------------------------------------------------

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        4,
                        5,
                        4,
                        10
                );

        gbc.anchor =
                GridBagConstraints.NORTHWEST;

        // -----------------------------------------------------
        // SUBJECT LABEL
        // -----------------------------------------------------

        JLabel subjectLabel =
                createFormLabel(
                        "Subject"
                );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;

        form.add(
                subjectLabel,
                gbc
        );

        // -----------------------------------------------------
        // SUBJECT FIELD
        // -----------------------------------------------------

        subjectField =
                new JTextField();

        subjectField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subjectField.setPreferredSize(
                new Dimension(
                        300,
                        36
                )
        );

        subjectField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        form.add(
                subjectField,
                gbc
        );

        // -----------------------------------------------------
        // QUESTION LABEL
        // -----------------------------------------------------

        JLabel questionLabel =
                createFormLabel(
                        "Question"
                );

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        form.add(
                questionLabel,
                gbc
        );

        // -----------------------------------------------------
        // QUESTION AREA
        // -----------------------------------------------------

        questionArea =
                new JTextArea(
                        3,
                        35
                );

        questionArea.setLineWrap(
                true
        );

        questionArea.setWrapStyleWord(
                true
        );

        questionArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        questionArea.setBorder(
                new EmptyBorder(
                        7,
                        9,
                        7,
                        9
                )
        );

        JScrollPane questionScroll =
                new JScrollPane(
                        questionArea
                );

        questionScroll.setPreferredSize(
                new Dimension(
                        420,
                        78
                )
        );

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        form.add(
                questionScroll,
                gbc
        );

        // -----------------------------------------------------
        // ATTACHMENT LABEL
        // -----------------------------------------------------

        JLabel attachmentLabel =
                createFormLabel(
                        "Attachment"
                );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;

        form.add(
                attachmentLabel,
                gbc
        );

        // -----------------------------------------------------
        // IMAGE PANEL
        // -----------------------------------------------------

        JPanel imagePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                7,
                                0
                        )
                );

        imagePanel.setOpaque(false);

        attachButton =
                createButton(
                        "Attach Image",
                        TEAL
                );

        removeImageButton =
                createOutlineButton(
                        "Remove"
                );

        selectedImageLabel =
                new JLabel(
                        "No image selected"
                );

        selectedImageLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        selectedImageLabel.setForeground(
                TEXT_GRAY
        );

        imagePanel.add(
                attachButton
        );

        imagePanel.add(
                removeImageButton
        );

        imagePanel.add(
                selectedImageLabel
        );

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        form.add(
                imagePanel,
                gbc
        );

        panel.add(
                form,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        viewImageButton =
                createOutlineButton(
                        "View Image"
                );

        viewImageButton.setEnabled(
                false
        );

        askButton =
                createButton(
                        "Ask Doubt",
                        TEAL
                );

        buttonPanel.add(
                viewImageButton
        );

        buttonPanel.add(
                askButton
        );

        panel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // -----------------------------------------------------
        // ACTIONS
        // -----------------------------------------------------

        attachButton.addActionListener(
                e -> chooseImage()
        );

        removeImageButton.addActionListener(
                e -> removeSelectedImage()
        );

        viewImageButton.addActionListener(
                e -> viewSelectedImage()
        );

        askButton.addActionListener(
                e -> askDoubt()
        );

        return panel;
    }

    // =========================================================
    // LOAD STUDENT
    // =========================================================

    private void loadStudentName() {

        String sql =
                "SELECT name FROM students WHERE email = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    studentEmail
            );

            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {

                if (rs.next()) {

                    studentName =
                            rs.getString("name");

                    studentLabel.setText(
                            studentName
                    );

                    loadDoubts();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Student details not found.",
                            "Student Not Found",
                            JOptionPane.ERROR_MESSAGE
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

    // =========================================================
    // LOAD DOUBTS
    // =========================================================

    private void loadDoubts() {
        if (studentName == null || studentName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Student name is not available.", "Doubts",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshButton.setEnabled(false);
        new SwingWorker<List<JsonNode>, Void>() {
            private String errorMessage;

            @Override
            protected List<JsonNode> doInBackground() throws Exception {
                String encodedName = URLEncoder.encode(studentName, StandardCharsets.UTF_8.name());
                URL url = new URL(API_BASE_URL + "/student/" + encodedName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(30000);
                connection.setRequestProperty("Accept", "application/json");

                try {
                    int statusCode = connection.getResponseCode();
                    java.io.InputStream stream = statusCode >= 200 && statusCode < 300
                            ? connection.getInputStream() : connection.getErrorStream();
                    JsonNode root = objectMapper.readTree(stream);
                    if (statusCode < 200 || statusCode >= 300) {
                        throw new IOException(root.path("error").asText(
                                "Unable to load doubts (HTTP " + statusCode + ")."));
                    }
                    if (!root.isArray()) {
                        throw new IOException("Unexpected response received from Doubts API.");
                    }
                    List<JsonNode> result = new ArrayList<>();
                    root.forEach(result::add);
                    return result;
                } finally {
                    connection.disconnect();
                }
            }

            @Override
            protected void done() {
                refreshButton.setEnabled(true);
                try {
                    allDoubts.clear();
                    allDoubts.addAll(get());
                    filterDoubts();
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    JOptionPane.showMessageDialog(DoubtFrame.this,
                            "Error loading doubts from Render API:\n" + cause.getMessage(),
                            "Doubts API Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // =========================================================
    // FILTER DOUBTS
    // =========================================================

    private void filterDoubts() {
        String search = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        int total = 0;
        int pending = 0;
        int resolved = 0;

        for (JsonNode doubt : allDoubts) {
            String subject = jsonText(doubt, "subject");
            String question = jsonText(doubt, "question");
            String status = jsonText(doubt, "status");
            String answer = jsonText(doubt, "answer");
            String imagePath = jsonText(doubt, "image_path");
            String answeredBy = jsonText(doubt, "answered_by");

            String searchable = (subject + " " + question + " " + status + " "
                    + answer + " " + answeredBy).toLowerCase();
            if (!search.isEmpty() && !searchable.contains(search)) {
                continue;
            }

            total++;
            if ("Resolved".equalsIgnoreCase(status)) resolved++; else pending++;

            if (answer.trim().isEmpty() || "null".equalsIgnoreCase(answer)) {
                answer = "Not answered yet";
            }
            if (imagePath.trim().isEmpty() || "null".equalsIgnoreCase(imagePath)) {
                imagePath = "No Image";
            }

            tableModel.addRow(new Object[]{
                    doubt.path("doubt_id").asInt(), subject, question, status,
                    answer, imagePath, displayAnsweredBy(answeredBy, status)
            });
        }
        updateStatistics(total, pending, resolved);
    }

    private String jsonText(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? "" : value.asText("");
    }

    // =========================================================
    // ASK DOUBT
    // =========================================================

    private void askDoubt() {
        String subject = subjectField.getText().trim();
        String question = questionArea.getText().trim();

        if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the subject.",
                    "Missing Subject", JOptionPane.WARNING_MESSAGE);
            subjectField.requestFocus();
            return;
        }
        if (question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your question.",
                    "Missing Question", JOptionPane.WARNING_MESSAGE);
            questionArea.requestFocus();
            return;
        }
        if (question.length() < 5) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a little more detail about your doubt.",
                    "Question Too Short", JOptionPane.WARNING_MESSAGE);
            questionArea.requestFocus();
            return;
        }
        if (studentName == null || studentName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student details are not loaded yet.",
                    "Doubts", JOptionPane.WARNING_MESSAGE);
            return;
        }

        askButton.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            private String errorMessage;

            @Override
            protected Boolean doInBackground() throws Exception {
                URL url = new URL(API_BASE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(30000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("studentName", studentName.trim());
                payload.put("subject", subject);
                payload.put("question", question);
                if (selectedImagePath == null || selectedImagePath.trim().isEmpty()) {
                    payload.putNull("imagePath");
                } else {
                    payload.put("imagePath", selectedImagePath);
                }
                try {
                    try (OutputStream out = connection.getOutputStream()) {
                        out.write(objectMapper.writeValueAsBytes(payload));
                    }
                    int code = connection.getResponseCode();
                    java.io.InputStream stream = code >= 200 && code < 300
                            ? connection.getInputStream() : connection.getErrorStream();
                    JsonNode response = objectMapper.readTree(stream);
                    if (code < 200 || code >= 300) {
                        throw new IOException(response.path("error").asText(
                                "Unable to submit doubt (HTTP " + code + ")."));
                    }
                    return true;
                } finally {
                    connection.disconnect();
                }
            }

            @Override
            protected void done() {
                askButton.setEnabled(true);
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(DoubtFrame.this,
                                "Your doubt has been submitted successfully.",
                                "Doubt Submitted", JOptionPane.INFORMATION_MESSAGE);
                        subjectField.setText("");
                        questionArea.setText("");
                        removeSelectedImage();
                        loadDoubts();
                    }
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    JOptionPane.showMessageDialog(DoubtFrame.this,
                            "Error submitting doubt to Render API:\n" + cause.getMessage(),
                            "Doubts API Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // =========================================================
    // CHOOSE IMAGE
    // =========================================================

    private void chooseImage() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Select Doubt Image"
        );

        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Image Files (*.jpg, *.jpeg, *.png, *.gif)",
                        "jpg",
                        "jpeg",
                        "png",
                        "gif"
                )
        );

        int result =
                chooser.showOpenDialog(
                        this
                );

        if (
                result ==
                        JFileChooser.APPROVE_OPTION
        ) {

            File file =
                    chooser.getSelectedFile();

            selectedImagePath =
                    file.getAbsolutePath();

            selectedImageLabel.setText(
                    file.getName()
            );

            selectedImageLabel.setToolTipText(
                    selectedImagePath
            );

            viewImageButton.setEnabled(
                    true
            );
        }
    }

    // =========================================================
    // REMOVE IMAGE
    // =========================================================

    private void removeSelectedImage() {

        selectedImagePath = null;

        selectedImageLabel.setText(
                "No image selected"
        );

        selectedImageLabel.setToolTipText(
                null
        );

        viewImageButton.setEnabled(
                false
        );
    }

    // =========================================================
    // VIEW SELECTED IMAGE
    // =========================================================

    private void viewSelectedImage() {

        if (
                selectedImagePath == null ||
                        selectedImagePath.trim().isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No image has been selected.",
                    "No Image",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        openImage(
                selectedImagePath
        );
    }

    // =========================================================
    // SHOW DOUBT DETAILS
    // =========================================================

    private void showDoubtDetails(
            int row
    ) {

        String subject =
                String.valueOf(
                        doubtTable.getValueAt(
                                row,
                                1
                        )
                );

        String question =
                String.valueOf(
                        doubtTable.getValueAt(
                                row,
                                2
                        )
                );

        String status =
                String.valueOf(
                        doubtTable.getValueAt(
                                row,
                                3
                        )
                );

        String answer =
                String.valueOf(
                        doubtTable.getValueAt(
                                row,
                                4
                        )
                );

        String imagePath =
                String.valueOf(
                        doubtTable.getValueAt(
                                row,
                                5
                        )
                );

        String answeredBy =
                String.valueOf(
                        doubtTable.getValueAt(
                                row,
                                6
                        )
                );

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        5,
                        5,
                        5,
                        5
                )
        );

        JLabel subjectLabel =
                new JLabel(
                        subject
                );

        subjectLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        subjectLabel.setForeground(
                NAVY
        );

        panel.add(
                subjectLabel,
                BorderLayout.NORTH
        );

        JTextArea details =
                new JTextArea();

        details.setEditable(
                false
        );

        details.setLineWrap(
                true
        );

        details.setWrapStyleWord(
                true
        );

        details.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        details.setText(
                "Question:\n"
                        + question
                        + "\n\n"
                        + "Status: "
                        + status
                        + "\n\n"
                        + "Answered By: "
                        + answeredBy
                        + "\n\n"
                        + "Answer:\n"
                        + answer
        );

        details.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scroll =
                new JScrollPane(
                        details
                );

        scroll.setPreferredSize(
                new Dimension(
                        700,
                        380
                )
        );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        if (
                imagePath != null &&
                        !imagePath.equalsIgnoreCase(
                                "No Image"
                        )
        ) {

            JButton viewButton =
                    createOutlineButton(
                            "View Attachment"
                    );

            viewButton.addActionListener(
                    e -> openImage(
                            imagePath
                    )
            );

            bottom.add(
                    viewButton
            );
        }

        panel.add(
                bottom,
                BorderLayout.SOUTH
        );

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Doubt Details",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    // =========================================================
    // OPEN IMAGE
    // =========================================================

    private void openImage(
            String imagePath
    ) {

        if (
                imagePath == null ||
                        imagePath.trim().isEmpty()
        ) {
            return;
        }

        File imageFile =
                new File(
                        imagePath
                );

        if (
                !imageFile.exists()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "The attached image could not be found.\n\n"
                            + imagePath,
                    "Image Not Found",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Image image =
                    ImageIO.read(
                            imageFile
                    );

            if (image == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "This file is not a valid image.",
                        "Invalid Image",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int originalWidth =
                    image.getWidth(null);

            int originalHeight =
                    image.getHeight(null);

            int maxWidth =
                    850;

            int maxHeight =
                    600;

            double scale =
                    Math.min(
                            (double) maxWidth
                                    / originalWidth,
                            (double) maxHeight
                                    / originalHeight
                    );

            scale =
                    Math.min(
                            scale,
                            1.0
                    );

            int width =
                    Math.max(
                            1,
                            (int)
                                    (
                                            originalWidth
                                                    * scale
                                    )
                    );

            int height =
                    Math.max(
                            1,
                            (int)
                                    (
                                            originalHeight
                                                    * scale
                                    )
                    );

            Image scaled =
                    image.getScaledInstance(
                            width,
                            height,
                            Image.SCALE_SMOOTH
                    );

            JLabel imageLabel =
                    new JLabel(
                            new ImageIcon(
                                    scaled
                            )
                    );

            imageLabel.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            JScrollPane scrollPane =
                    new JScrollPane(
                            imageLabel
                    );

            scrollPane.setPreferredSize(
                    new Dimension(
                            Math.min(
                                    width + 40,
                                    900
                            ),
                            Math.min(
                                    height + 40,
                                    650
                            )
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Attached Image",
                    JOptionPane.PLAIN_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open image:\n"
                            + e.getMessage(),
                    "Image Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // UPDATE STATISTICS
    // =========================================================

    private String displayAnsweredBy(String answeredBy, String status) {
        if (answeredBy != null && !answeredBy.trim().isEmpty()) {
            return answeredBy.trim();
        }

        if (status != null && status.equalsIgnoreCase("Resolved")) {
            return "Faculty not recorded";
        }

        return "Not answered yet";
    }

    private void updateStatistics(
            int total,
            int pending,
            int resolved
    ) {

        if (totalValueLabel != null) {

            totalValueLabel.setText(
                    String.valueOf(total)
            );
        }

        if (pendingValueLabel != null) {

            pendingValueLabel.setText(
                    String.valueOf(pending)
            );
        }

        if (resolvedValueLabel != null) {

            resolvedValueLabel.setText(
                    String.valueOf(resolved)
            );
        }
    }

    // =========================================================
    // FORM LABEL
    // =========================================================

    private JLabel createFormLabel(
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
                        13
                )
        );

        label.setForeground(
                TEXT_DARK
        );

        return label;
    }

    // =========================================================
    // SMALL LABEL
    // =========================================================

    private JLabel createSmallLabel(
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
                        11
                )
        );

        label.setForeground(
                TEXT_GRAY
        );

        return label;
    }

    // =========================================================
    // VALUE LABEL
    // =========================================================

    private JLabel createValueLabel(
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
                        25
                )
        );

        label.setForeground(
                NAVY
        );

        return label;
    }

    // =========================================================
    // STAT ICON
    // =========================================================

    private JLabel createStatIcon(
            String text,
            Color color
    ) {

        JLabel label =
                new JLabel(
                        text,
                        SwingConstants.CENTER
                );

        label.setPreferredSize(
                new Dimension(
                        48,
                        48
                )
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        label.setForeground(
                color
        );

        label.setOpaque(
                true
        );

        label.setBackground(
                new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        25
                )
        );

        return label;
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

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
                        18,
                        10,
                        18
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
    // OUTLINE BUTTON
    // =========================================================

    private JButton createOutlineButton(
            String text
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
                NAVY
        );

        button.setBackground(
                WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                14,
                                8,
                                14
                        )
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
    // CENTER RENDERER
    // =========================================================

    private static class CenterRenderer
            extends DefaultTableCellRenderer {

        public CenterRenderer() {

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setVerticalAlignment(
                    SwingConstants.CENTER
            );
        }
    }

    // =========================================================
    // SUBJECT RENDERER
    // =========================================================

    private static class SubjectRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            label.setForeground(
                    NAVY
            );

            label.setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            label.setVerticalAlignment(
                    SwingConstants.CENTER
            );

            return label;
        }
    }

    // =========================================================
    // TEXT AREA RENDERER
    // =========================================================

    private static class TextAreaRenderer
            extends JTextArea
            implements javax.swing.table.TableCellRenderer {

        public TextAreaRenderer() {

            setLineWrap(
                    true
            );

            setWrapStyleWord(
                    true
            );

            setOpaque(
                    true
            );

            setBorder(
                    BorderFactory.createEmptyBorder(
                            8,
                            10,
                            8,
                            10
                    )
            );
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            setText(
                    value == null
                            ? ""
                            : value.toString()
            );

            setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            13
                    )
            );

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground()
                );

                setForeground(
                        table.getSelectionForeground()
                );

            } else {

                setBackground(
                        Color.WHITE
                );

                setForeground(
                        TEXT_DARK
                );
            }

            return this;
        }
    }

    // =========================================================
    // STATUS RENDERER
    // =========================================================

    private static class StatusRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );

            String status =
                    value == null
                            ? ""
                            : value.toString();

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setVerticalAlignment(
                    SwingConstants.CENTER
            );

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            12
                    )
            );

            if (
                    status.equalsIgnoreCase(
                            "Resolved"
                    )
            ) {

                label.setText(
                        "● Resolved"
                );

                label.setForeground(
                        GREEN
                );

                label.setBackground(
                        GREEN_LIGHT
                );

            } else {

                label.setText(
                        "● Pending"
                );

                label.setForeground(
                        ORANGE
                );

                label.setBackground(
                        ORANGE_LIGHT
                );
            }

            if (isSelected) {

                label.setBackground(
                        table.getSelectionBackground()
                );
            }

            return label;
        }
    }

    // =========================================================
    // ATTACHMENT RENDERER
    // =========================================================

    private static class AttachmentRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            label.setVerticalAlignment(
                    SwingConstants.CENTER
            );

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            12
                    )
            );

            String path =
                    value == null
                            ? ""
                            : value.toString();

            if (
                    path.isEmpty() ||
                            path.equalsIgnoreCase(
                                    "No Image"
                            )
            ) {

                label.setText(
                        "No Attachment"
                );

                label.setForeground(
                        TEXT_GRAY
                );

            } else {

                label.setText(
                        "View Image"
                );

                label.setForeground(
                        TEAL
                );
            }

            if (isSelected) {

                label.setBackground(
                        table.getSelectionBackground()
                );

            } else {

                label.setBackground(
                        WHITE
                );
            }

            return label;
        }
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel {

        private final Color backgroundColor;
        private final Color borderColor;
        private final int radius;

        public RoundedPanel(
                Color backgroundColor,
                Color borderColor,
                int radius
        ) {

            this.backgroundColor =
                    backgroundColor;

            this.borderColor =
                    borderColor;

            this.radius =
                    radius;

            setOpaque(
                    false
            );
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            Shape shape =
                    new RoundRectangle2D.Double(
                            0,
                            0,
                            width - 1,
                            height - 1,
                            radius,
                            radius
                    );

            g2.setColor(
                    backgroundColor
            );

            g2.fill(
                    shape
            );

            g2.setColor(
                    borderColor
            );

            g2.draw(
                    shape
            );

            g2.dispose();

            super.paintComponent(
                    g
            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () ->
                        new DoubtFrame(
                                "aditya@gmail.com"
                        )
        );
    }
}