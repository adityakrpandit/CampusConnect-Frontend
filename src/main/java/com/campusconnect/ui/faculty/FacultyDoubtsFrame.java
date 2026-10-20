package com.campusconnect.ui.faculty;

import com.campusconnect.config.DatabaseConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;

/**
 * CampusConnect - Faculty Doubts
 *
 * Features:
 * - CampusConnect announcement-style background
 * - Card based admin UI
 * - Student doubts table
 * - Search doubts
 * - Student and Subject inline details
 * - Image attachment preview using image_path
 * - Full image viewer
 * - Admin text response
 * - Status update
 * - Delete doubt
 * - Refresh and clear
 *
 * Database table expected:
 *
 * doubts(
 *     doubt_id,
 *     student_name,
 *     subject,
 *     question,
 *     image_path,
 *     status,
 *     answer,
 *     answered_by
 * )
 */
public class FacultyDoubtsFrame extends JFrame {

    // =========================================================
    // COLORS
    // Same visual family as ManageAnnouncementsFrame
    // =========================================================

    private static final Color PAGE_BG =
            new Color(232, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color NAVY =
            new Color(24, 67, 108);

    private static final Color NAVY_DARK =
            new Color(17, 48, 79);

    private static final Color TEAL =
            new Color(25, 166, 181);

    private static final Color TEAL_DARK =
            new Color(16, 139, 153);

    private static final Color TEXT =
            new Color(39, 58, 78);

    private static final Color MUTED =
            new Color(91, 119, 145);

    private static final Color BORDER =
            new Color(205, 224, 233);

    private static final Color LIGHT_BLUE =
            new Color(239, 249, 253);

    private static final Color LIGHT_GREEN =
            new Color(239, 251, 244);

    private static final Color LIGHT_ORANGE =
            new Color(255, 247, 235);

    private static final Color SUCCESS =
            new Color(22, 163, 74);

    private static final Color WARNING =
            new Color(234, 119, 19);

    private static final Color DANGER =
            new Color(211, 58, 64);

    private static final Color SLATE =
            new Color(105, 125, 145);

    // =========================================================
    // TABLE
    // =========================================================

    private JTable doubtsTable;

    private DefaultTableModel tableModel;

    private TableRowSorter<DefaultTableModel> tableSorter;

    private JTextField searchField;

    // =========================================================
    // DETAILS
    // =========================================================

    private JComboBox<String> statusComboBox;

    private JTextArea answerArea;

    private JTextArea questionPreviewArea;

    private JLabel selectedStudentLabel;

    private JLabel selectedSubjectLabel;

    private JLabel selectedStatusLabel;

    private JLabel selectedIdLabel;

    // =========================================================
    // STATISTICS
    // =========================================================

    private JLabel totalDoubtsLabel;

    private JLabel pendingDoubtsLabel;

    private JLabel resolvedDoubtsLabel;

    // =========================================================
    // IMAGE
    // =========================================================

    private JLabel attachmentStatusLabel;

    private JLabel imagePreviewLabel;

    private JButton viewImageButton;

    private String selectedImagePath;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    private final String facultyName;
    private final String facultyEmail;
    private final boolean embedded;
    private final Runnable onClose;

    // Existing constructor: keeps standalone-window behaviour unchanged.
    public FacultyDoubtsFrame(String facultyName, String facultyEmail) {
        this(facultyName, facultyEmail, false, null);
    }

    // Embedded constructor: lets FacultyDashboardFrame display this module
    // inside its content area without opening a separate window.
    public FacultyDoubtsFrame(
            String facultyName,
            String facultyEmail,
            boolean embedded,
            Runnable onClose
    ) {
        this.facultyName = facultyName == null || facultyName.trim().isEmpty()
                ? "Faculty"
                : facultyName.trim();
        this.facultyEmail = facultyEmail == null ? "" : facultyEmail.trim();
        this.embedded = embedded;
        this.onClose = onClose;

        setTitle("CampusConnect - Faculty Doubts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 720));

        buildUI();
        loadDoubts();

        if (!embedded) {
            setSize(1280, 820);
            setLocationRelativeTo(null);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
    }

    // Handles the Close button in both standalone and embedded modes.
    private void closeModule() {
        if (embedded) {
            if (onClose != null) {
                onClose.run();
            }
        } else {
            dispose();
        }
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        CampusBackgroundPanel rootPanel =
                new CampusBackgroundPanel();

        rootPanel.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout(18, 0)
                );

        headerPanel.setBackground(
                WHITE
        );

        headerPanel.setBorder(
                new EmptyBorder(
                        16,
                        34,
                        16,
                        34
                )
        );

        // -----------------------------------------------------
        // HEADER LEFT
        // -----------------------------------------------------

        JPanel headerLeft =
                new JPanel(
                        new BorderLayout(15, 0)
                );

        headerLeft.setOpaque(false);

        JLabel doubtIconBox =
                createIconBox(
                        new DoubtIcon(
                                WHITE,
                                30
                        ),
                        TEAL,
                        64,
                        64
                );

        headerLeft.add(
                doubtIconBox,
                BorderLayout.WEST
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel brandLabel =
                new JLabel(
                        "CAMPUSCONNECT"
                );

        brandLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        brandLabel.setForeground(
                TEAL_DARK
        );

        JLabel titleLabel =
                new JLabel(
                        "Student Doubts"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        titleLabel.setForeground(
                NAVY_DARK
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Review student questions, attachments and provide solutions"
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitleLabel.setForeground(
                MUTED
        );

        titlePanel.add(
                brandLabel
        );

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(
                titleLabel
        );

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(
                subtitleLabel
        );

        headerLeft.add(
                titlePanel,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // HEADER RIGHT BRAND
        // -----------------------------------------------------

        JPanel brandRight =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        brandRight.setOpaque(false);

        JLabel ccBox =
                createIconBox(
                        new CampusIcon(
                                WHITE,
                                24
                        ),
                        TEAL,
                        58,
                        58
                );

        brandRight.add(
                ccBox,
                BorderLayout.WEST
        );

        JPanel brandText =
                new JPanel();

        brandText.setOpaque(false);

        brandText.setLayout(
                new BoxLayout(
                        brandText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel campusLabel =
                new JLabel(
                        "CampusConnect"
                );

        campusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        campusLabel.setForeground(
                NAVY_DARK
        );

        JLabel adminLabel =
                new JLabel(
                        "Faculty Portal"
                );

        adminLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        adminLabel.setForeground(
                MUTED
        );

        brandText.add(
                campusLabel
        );

        brandText.add(
                Box.createVerticalStrut(2)
        );

        brandText.add(
                adminLabel
        );

        brandRight.add(
                brandText,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // REFRESH
        // -----------------------------------------------------

        JButton refreshButton =
                createButton(
                        "Refresh",
                        TEAL_DARK,
                        WHITE,
                        new RefreshIcon(
                                WHITE,
                                14
                        )
                );

        refreshButton.setPreferredSize(
                new Dimension(
                        118,
                        40
                )
        );

        refreshButton.addActionListener(
                e -> {

                    loadDoubts();

                    clearFields();
                }
        );

        headerPanel.add(
                headerLeft,
                BorderLayout.WEST
        );

        headerPanel.add(
                brandRight,
                BorderLayout.CENTER
        );

        headerPanel.add(
                refreshButton,
                BorderLayout.EAST
        );

        rootPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(
                                14,
                                14
                        )
                );

        contentPanel.setOpaque(false);

        contentPanel.setBorder(
                new EmptyBorder(
                        14,
                        34,
                        16,
                        34
                )
        );

        // =====================================================
        // SUMMARY CARDS
        // =====================================================

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                12,
                                0
                        )
                );

        summaryPanel.setOpaque(false);

        summaryPanel.setPreferredSize(
                new Dimension(
                        0,
                        78
                )
        );

        JPanel totalCard =
                createSummaryCard(
                        "Total Doubts",
                        "0",
                        TEAL,
                        new ClipboardIcon(
                                TEAL,
                                22
                        )
                );

        JPanel pendingCard =
                createSummaryCard(
                        "Pending",
                        "0",
                        WARNING,
                        new ClockIcon(
                                WARNING,
                                22
                        )
                );

        JPanel resolvedCard =
                createSummaryCard(
                        "Resolved",
                        "0",
                        SUCCESS,
                        new CheckIcon(
                                SUCCESS,
                                22
                        )
                );

        totalDoubtsLabel =
                (JLabel) totalCard.getClientProperty(
                        "valueLabel"
                );

        pendingDoubtsLabel =
                (JLabel) pendingCard.getClientProperty(
                        "valueLabel"
                );

        resolvedDoubtsLabel =
                (JLabel) resolvedCard.getClientProperty(
                        "valueLabel"
                );

        summaryPanel.add(
                totalCard
        );

        summaryPanel.add(
                pendingCard
        );

        summaryPanel.add(
                resolvedCard
        );

        contentPanel.add(
                summaryPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN AREA
        // =====================================================

        JPanel mainArea =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        mainArea.setOpaque(false);

        // =====================================================
        // STUDENT DOUBTS CARD
        // =====================================================

        JPanel tableCard =
                createCardPanel();

        tableCard.setLayout(
                new BorderLayout()
        );

        // -----------------------------------------------------
        // TABLE HEADER
        // -----------------------------------------------------

        JPanel tableTop =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        tableTop.setOpaque(false);

        tableTop.setBorder(
                new EmptyBorder(
                        13,
                        18,
                        11,
                        18
                )
        );

        JPanel tableTitleBox =
                new JPanel();

        tableTitleBox.setOpaque(false);

        tableTitleBox.setLayout(
                new BoxLayout(
                        tableTitleBox,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel tableTitle =
                new JLabel(
                        "Student Doubts"
                );

        tableTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        tableTitle.setForeground(
                NAVY_DARK
        );

        JLabel tableSubtitle =
                new JLabel(
                        "Select a doubt to review the question, image and response"
                );

        tableSubtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        tableSubtitle.setForeground(
                MUTED
        );

        tableTitleBox.add(
                tableTitle
        );

        tableTitleBox.add(
                Box.createVerticalStrut(3)
        );

        tableTitleBox.add(
                tableSubtitle
        );

        // -----------------------------------------------------
        // SEARCH
        // -----------------------------------------------------

        JPanel searchBox =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        searchBox.setOpaque(false);

        searchBox.setPreferredSize(
                new Dimension(
                        300,
                        38
                )
        );

        JLabel searchIcon =
                new JLabel(
                        new SearchIcon(
                                MUTED,
                                17
                        )
                );

        searchIcon.setBorder(
                new EmptyBorder(
                        0,
                        7,
                        0,
                        0
                )
        );

        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        searchField.setForeground(
                TEXT
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                7,
                                5,
                                7,
                                8
                        )
                )
        );

        searchField.setToolTipText(
                "Search by student, subject or question"
        );

        searchBox.add(
                searchIcon,
                BorderLayout.WEST
        );

        searchBox.add(
                searchField,
                BorderLayout.CENTER
        );

        tableTop.add(
                tableTitleBox,
                BorderLayout.WEST
        );

        tableTop.add(
                searchBox,
                BorderLayout.EAST
        );

        tableCard.add(
                tableTop,
                BorderLayout.NORTH
        );

        // =====================================================
        // TABLE
        // =====================================================

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Student",
                                "Subject",
                                "Question",
                                "Attachment",
                                "Status"
                        },
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

        doubtsTable =
                new JTable(
                        tableModel
                );

        doubtsTable.setRowHeight(
                43
        );

        doubtsTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        doubtsTable.setForeground(
                TEXT
        );

        doubtsTable.setBackground(
                WHITE
        );

        doubtsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        doubtsTable.setSelectionBackground(
                new Color(
                        235,
                        248,
                        251
                )
        );

        doubtsTable.setSelectionForeground(
                NAVY_DARK
        );

        doubtsTable.setShowVerticalLines(
                false
        );

        doubtsTable.setGridColor(
                new Color(
                        226,
                        236,
                        242
                )
        );

        doubtsTable.setIntercellSpacing(
                new Dimension(
                        0,
                        1
                )
        );

        doubtsTable.setFillsViewportHeight(
                true
        );

        doubtsTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_LAST_COLUMN
        );

        JTableHeader tableHeader =
                doubtsTable.getTableHeader();

        tableHeader.setPreferredSize(
                new Dimension(
                        0,
                        42
                )
        );

        tableHeader.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        tableHeader.setForeground(
                WHITE
        );

        tableHeader.setBackground(
                NAVY
        );

        tableHeader.setReorderingAllowed(
                false
        );

        // -----------------------------------------------------
        // COLUMN WIDTH
        // -----------------------------------------------------

        doubtsTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        doubtsTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(145);

        doubtsTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(125);

        doubtsTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(390);

        doubtsTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);

        doubtsTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        doubtsTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        new CenterRenderer()
                );

        doubtsTable
                .getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new QuestionRenderer()
                );

        doubtsTable
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new AttachmentRenderer()
                );

        doubtsTable
                .getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        new StatusRenderer()
                );

        JScrollPane tableScroll =
                new JScrollPane(
                        doubtsTable
                );

        tableScroll.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        14,
                        14,
                        14
                )
        );

        tableScroll.getViewport()
                .setBackground(
                        WHITE
                );

        tableCard.add(
                tableScroll,
                BorderLayout.CENTER
        );

        // =====================================================
        // SEARCH FILTER
        // =====================================================

        tableSorter =
                new TableRowSorter<>(
                        tableModel
                );

        doubtsTable.setRowSorter(
                tableSorter
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            private void filter() {

                                String text =
                                        searchField
                                                .getText()
                                                .trim();

                                if (text.isEmpty()) {

                                    tableSorter
                                            .setRowFilter(
                                                    null
                                            );

                                } else {

                                    tableSorter
                                            .setRowFilter(
                                                    RowFilter.regexFilter(
                                                            "(?i)"
                                                                    + java.util.regex.Pattern
                                                                    .quote(text)
                                                    )
                                            );
                                }
                            }

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                filter();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                filter();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                filter();
                            }
                        }
                );

        mainArea.add(
                tableCard,
                BorderLayout.CENTER
        );

        // =====================================================
        // DETAILS CARD
        // =====================================================

        JPanel detailsCard =
                createCardPanel();

        detailsCard.setPreferredSize(
                new Dimension(
                        430,
                        0
                )
        );

        detailsCard.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // DETAILS HEADER
        // =====================================================

        JPanel detailsHeader =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        detailsHeader.setOpaque(false);

        detailsHeader.setBorder(
                new EmptyBorder(
                        13,
                        16,
                        10,
                        16
                )
        );

        JLabel detailsIcon =
                new JLabel(
                        new DetailIcon(
                                TEAL,
                                25
                        )
                );

        detailsHeader.add(
                detailsIcon,
                BorderLayout.WEST
        );

        JPanel detailsTitleBox =
                new JPanel();

        detailsTitleBox.setOpaque(false);

        detailsTitleBox.setLayout(
                new BoxLayout(
                        detailsTitleBox,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel detailsTitle =
                new JLabel(
                        "Doubt Details"
                );

        detailsTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        detailsTitle.setForeground(
                NAVY_DARK
        );

        JLabel detailsSubtitle =
                new JLabel(
                        "Review the student's query and provide a solution"
                );

        detailsSubtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        detailsSubtitle.setForeground(
                MUTED
        );

        detailsTitleBox.add(
                detailsTitle
        );

        detailsTitleBox.add(
                Box.createVerticalStrut(3)
        );

        detailsTitleBox.add(
                detailsSubtitle
        );

        detailsHeader.add(
                detailsTitleBox,
                BorderLayout.CENTER
        );

        detailsCard.add(
                detailsHeader,
                BorderLayout.NORTH
        );

        // =====================================================
        // DETAILS CONTENT
        // =====================================================

        JPanel detailsContent =
                new JPanel();

        detailsContent.setOpaque(false);

        detailsContent.setLayout(
                new BoxLayout(
                        detailsContent,
                        BoxLayout.Y_AXIS
                )
        );

        detailsContent.setBorder(
                new EmptyBorder(
                        0,
                        14,
                        8,
                        14
                )
        );

        // =====================================================
        // STUDENT + SUBJECT
        // =====================================================

        JPanel identityCard =
                createInnerCard();

        identityCard.setLayout(
                new GridLayout(
                        1,
                        2,
                        12,
                        0
                )
        );

        identityCard.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        JPanel studentBox =
                createInlineDetail(
                        "Student",
                        new UserIcon(
                                TEAL,
                                17
                        )
                );

        selectedStudentLabel =
                (JLabel) studentBox
                        .getClientProperty(
                                "valueLabel"
                        );

        JPanel subjectBox =
                createInlineDetail(
                        "Subject",
                        new BookIcon(
                                NAVY,
                                17
                        )
                );

        selectedSubjectLabel =
                (JLabel) subjectBox
                        .getClientProperty(
                                "valueLabel"
                        );

        identityCard.add(
                studentBox
        );

        identityCard.add(
                subjectBox
        );

        detailsContent.add(
                identityCard
        );

        detailsContent.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // STATUS
        // =====================================================

        JPanel statusCard =
                createInnerCard();

        statusCard.setLayout(
                new BorderLayout(
                        10,
                        0
                )
        );

        statusCard.setBorder(
                new EmptyBorder(
                        9,
                        12,
                        9,
                        12
                )
        );

        JPanel statusText =
                new JPanel();

        statusText.setOpaque(false);

        statusText.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        6,
                        0
                )
        );

        JLabel statusCaption =
                new JLabel(
                        "Status:"
                );

        statusCaption.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        statusCaption.setForeground(
                MUTED
        );

        selectedStatusLabel =
                new JLabel(
                        "Select a doubt"
                );

        selectedStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        selectedStatusLabel.setForeground(
                TEXT
        );

        statusText.add(
                statusCaption
        );

        statusText.add(
                selectedStatusLabel
        );

        selectedIdLabel =
                new JLabel(
                        "ID: --"
                );

        selectedIdLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        selectedIdLabel.setForeground(
                MUTED
        );

        statusCard.add(
                statusText,
                BorderLayout.CENTER
        );

        statusCard.add(
                selectedIdLabel,
                BorderLayout.EAST
        );

        detailsContent.add(
                statusCard
        );

        detailsContent.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // STUDENT QUESTION
        // =====================================================

        JPanel questionCard =
                createInnerCard();

        questionCard.setLayout(
                new BorderLayout(
                        7,
                        7
                )
        );

        JLabel questionTitle =
                createSectionLabel(
                        "Student Question",
                        new QuestionIcon(
                                TEAL,
                                16
                        )
                );

        questionPreviewArea =
                new JTextArea(
                        "Select a doubt to view the question."
                );

        questionPreviewArea.setEditable(
                false
        );

        questionPreviewArea.setLineWrap(
                true
        );

        questionPreviewArea.setWrapStyleWord(
                true
        );

        questionPreviewArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        questionPreviewArea.setForeground(
                TEXT
        );

        questionPreviewArea.setBackground(
                WHITE
        );

        questionPreviewArea.setBorder(
                new EmptyBorder(
                        7,
                        8,
                        7,
                        8
                )
        );

        JScrollPane questionScroll =
                new JScrollPane(
                        questionPreviewArea
                );

        questionScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        questionScroll.setPreferredSize(
                new Dimension(
                        0,
                        78
                )
        );

        questionCard.add(
                questionTitle,
                BorderLayout.NORTH
        );

        questionCard.add(
                questionScroll,
                BorderLayout.CENTER
        );

        detailsContent.add(
                questionCard
        );

        detailsContent.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // ATTACHMENT
        // =====================================================

        JPanel imageCard =
                createInnerCard();

        imageCard.setLayout(
                new BorderLayout(
                        7,
                        7
                )
        );

        JLabel imageTitle =
                createSectionLabel(
                        "Attached Image",
                        new ImageIconShape(
                                TEAL,
                                16
                        )
                );

        imagePreviewLabel =
                new JLabel(
                        "No attachment",
                        SwingConstants.CENTER
                );

        imagePreviewLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        imagePreviewLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        imagePreviewLabel.setForeground(
                MUTED
        );

        imagePreviewLabel.setBackground(
                new Color(
                        248,
                        251,
                        253
                )
        );

        imagePreviewLabel.setOpaque(
                true
        );

        imagePreviewLabel.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        imagePreviewLabel.setPreferredSize(
                new Dimension(
                        0,
                        130
                )
        );

        attachmentStatusLabel =
                new JLabel(
                        "No image attached"
                );

        attachmentStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        attachmentStatusLabel.setForeground(
                MUTED
        );

        viewImageButton =
                createButton(
                        "View Full Image",
                        TEAL_DARK,
                        WHITE,
                        new ImageIconShape(
                                WHITE,
                                14
                        )
                );

        viewImageButton.setPreferredSize(
                new Dimension(
                        145,
                        31
                )
        );

        viewImageButton.setEnabled(
                false
        );

        viewImageButton.addActionListener(
                e -> showFullImage()
        );

        JPanel imageBottom =
                new JPanel(
                        new BorderLayout(
                                7,
                                0
                        )
                );

        imageBottom.setOpaque(false);

        imageBottom.add(
                attachmentStatusLabel,
                BorderLayout.CENTER
        );

        imageBottom.add(
                viewImageButton,
                BorderLayout.EAST
        );

        imageCard.add(
                imageTitle,
                BorderLayout.NORTH
        );

        imageCard.add(
                imagePreviewLabel,
                BorderLayout.CENTER
        );

        imageCard.add(
                imageBottom,
                BorderLayout.SOUTH
        );

        detailsContent.add(
                imageCard
        );

        detailsContent.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // ADMIN RESPONSE
        // =====================================================

        JPanel answerCard =
                createInnerCard();

        answerCard.setLayout(
                new BorderLayout(
                        7,
                        7
                )
        );

        JLabel answerTitle =
                createSectionLabel(
                        "Faculty Response",
                        new AnswerIcon(
                                SUCCESS,
                                16
                        )
                );

        answerArea =
                new JTextArea();

        answerArea.setLineWrap(
                true
        );

        answerArea.setWrapStyleWord(
                true
        );

        answerArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        answerArea.setForeground(
                TEXT
        );

        answerArea.setBackground(
                WHITE
        );

        answerArea.setBorder(
                new EmptyBorder(
                        7,
                        8,
                        7,
                        8
                )
        );

        JScrollPane answerScroll =
                new JScrollPane(
                        answerArea
                );

        answerScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        answerScroll.setPreferredSize(
                new Dimension(
                        0,
                        105
                )
        );

        answerCard.add(
                answerTitle,
                BorderLayout.NORTH
        );

        answerCard.add(
                answerScroll,
                BorderLayout.CENTER
        );

        detailsContent.add(
                answerCard
        );

        detailsContent.add(
                Box.createVerticalStrut(
                        8
                )
        );

        // =====================================================
        // NEW STATUS
        // =====================================================

        JPanel newStatusCard =
                createInnerCard();

        newStatusCard.setLayout(
                new BorderLayout(
                        10,
                        0
                )
        );

        newStatusCard.setBorder(
                new EmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        JLabel newStatusLabel =
                new JLabel(
                        "New Status"
                );

        newStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        newStatusLabel.setForeground(
                TEXT
        );

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Pending",
                                "Resolved"
                        }
                );

        statusComboBox.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        statusComboBox.setPreferredSize(
                new Dimension(
                        150,
                        32
                )
        );

        newStatusCard.add(
                newStatusLabel,
                BorderLayout.WEST
        );

        newStatusCard.add(
                statusComboBox,
                BorderLayout.EAST
        );

        detailsContent.add(
                newStatusCard
        );

        // =====================================================
        // DETAILS SCROLL
        // =====================================================

        JScrollPane detailsScroll =
                new JScrollPane(
                        detailsContent
                );

        detailsScroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        detailsScroll
                .getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        detailsScroll
                .getViewport()
                .setOpaque(false);

        detailsScroll.setOpaque(
                false
        );

        detailsCard.add(
                detailsScroll,
                BorderLayout.CENTER
        );

        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        JPanel actionPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                8,
                                7
                        )
                );

        actionPanel.setOpaque(false);

        actionPanel.setBorder(
                new EmptyBorder(
                        9,
                        14,
                        13,
                        14
                )
        );

        JButton answerButton =
                createButton(
                        "Answer & Resolve",
                        SUCCESS,
                        WHITE,
                        new CheckIcon(
                                WHITE,
                                14
                        )
                );

        JButton deleteButton =
                createButton(
                        "Delete",
                        DANGER,
                        WHITE,
                        new TrashIcon(
                                WHITE,
                                14
                        )
                );

        JButton clearButton =
                createButton(
                        "Clear",
                        SLATE,
                        WHITE,
                        new ClearIcon(
                                WHITE,
                                14
                        )
                );

        JButton closeButton =
                createButton(
                        "Close",
                        NAVY_DARK,
                        WHITE,
                        new CloseIcon(
                                WHITE,
                                14
                        )
                );

        answerButton.addActionListener(
                e -> answerDoubt()
        );

        deleteButton.addActionListener(
                e -> deleteDoubt()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> closeModule()
        );

        actionPanel.add(
                answerButton
        );

        actionPanel.add(
                deleteButton
        );

        actionPanel.add(
                clearButton
        );

        actionPanel.add(
                closeButton
        );

        detailsCard.add(
                actionPanel,
                BorderLayout.SOUTH
        );

        mainArea.add(
                detailsCard,
                BorderLayout.EAST
        );

        contentPanel.add(
                mainArea,
                BorderLayout.CENTER
        );

        rootPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        add(
                rootPanel
        );

        // =====================================================
        // TABLE SELECTION
        // =====================================================

        doubtsTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (e.getValueIsAdjusting()) {
                                return;
                            }

                            int selectedRow =
                                    doubtsTable.getSelectedRow();

                            if (selectedRow == -1) {
                                return;
                            }

                            int modelRow =
                                    doubtsTable
                                            .convertRowIndexToModel(
                                                    selectedRow
                                            );

                            int doubtId =
                                    Integer.parseInt(
                                            String.valueOf(
                                                    tableModel
                                                            .getValueAt(
                                                                    modelRow,
                                                                    0
                                                            )
                                            )
                                    );

                            String student =
                                    String.valueOf(
                                            tableModel
                                                    .getValueAt(
                                                            modelRow,
                                                            1
                                                    )
                                    );

                            String subject =
                                    String.valueOf(
                                            tableModel
                                                    .getValueAt(
                                                            modelRow,
                                                            2
                                                    )
                                    );

                            String question =
                                    String.valueOf(
                                            tableModel
                                                    .getValueAt(
                                                            modelRow,
                                                            3
                                                    )
                                    );

                            String imagePath =
                                    String.valueOf(
                                            tableModel
                                                    .getValueAt(
                                                            modelRow,
                                                            4
                                                    )
                                    );

                            String status =
                                    String.valueOf(
                                            tableModel
                                                    .getValueAt(
                                                            modelRow,
                                                            5
                                                    )
                                    );

                            selectedIdLabel.setText(
                                    "ID: " + doubtId
                            );

                            selectedStudentLabel.setText(
                                    student
                            );

                            selectedSubjectLabel.setText(
                                    subject
                            );

                            selectedStatusLabel.setText(
                                    status
                            );

                            if (
                                    status.equalsIgnoreCase(
                                            "Resolved"
                                    )
                            ) {

                                selectedStatusLabel
                                        .setForeground(
                                                SUCCESS
                                        );

                            } else {

                                selectedStatusLabel
                                        .setForeground(
                                                WARNING
                                        );
                            }

                            questionPreviewArea.setText(
                                    question
                            );

                            statusComboBox.setSelectedItem(
                                    status
                            );

                            selectedImagePath =
                                    normalizeImagePath(
                                            imagePath
                                    );

                            loadImagePreview(
                                    selectedImagePath
                            );

                            loadExistingAnswer(
                                    doubtId
                            );
                        }
                );
    }

    // =========================================================
    // SUMMARY CARD
    // =========================================================

    private JPanel createSummaryCard(
            String title,
            String value,
            Color accent,
            Icon icon
    ) {

        JPanel card =
                new RoundedPanel(
                        14,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        card.setBorder(
                new EmptyBorder(
                        9,
                        14,
                        9,
                        14
                )
        );

        JLabel iconLabel =
                new JLabel(
                        icon,
                        SwingConstants.CENTER
                );

        iconLabel.setOpaque(
                true
        );

        iconLabel.setBackground(
                blend(
                        accent,
                        WHITE,
                        0.88f
                )
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        50,
                        50
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
                        title.toUpperCase()
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        titleLabel.setForeground(
                MUTED
        );

        JLabel valueLabel =
                new JLabel(
                        value
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        valueLabel.setForeground(
                accent
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(
                        2
                )
        );

        text.add(
                valueLabel
        );

        card.add(
                iconLabel,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        card.putClientProperty(
                "valueLabel",
                valueLabel
        );

        return card;
    }

    // =========================================================
    // CARD PANEL
    // =========================================================

    private JPanel createCardPanel() {

        JPanel panel =
                new RoundedPanel(
                        14,
                        WHITE
                );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        return panel;
    }

    // =========================================================
    // INNER CARD
    // =========================================================

    private JPanel createInnerCard() {

        JPanel panel =
                new RoundedPanel(
                        10,
                        new Color(
                                248,
                                251,
                                253
                        )
                );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                8,
                                9,
                                8,
                                9
                        )
                )
        );

        return panel;
    }

    // =========================================================
    // INLINE STUDENT / SUBJECT
    // =========================================================

    private JPanel createInlineDetail(
            String caption,
            Icon icon
    ) {

        JPanel box =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                6,
                                0
                        )
                );

        box.setOpaque(false);

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        box.add(
                iconLabel
        );

        JLabel captionLabel =
                new JLabel(
                        caption + ":"
                );

        captionLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        captionLabel.setForeground(
                MUTED
        );

        JLabel valueLabel =
                new JLabel(
                        "Select a doubt"
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        valueLabel.setForeground(
                TEXT
        );

        box.add(
                captionLabel
        );

        box.add(
                valueLabel
        );

        box.putClientProperty(
                "valueLabel",
                valueLabel
        );

        return box;
    }

    // =========================================================
    // SECTION LABEL
    // =========================================================

    private JLabel createSectionLabel(
            String text,
            Icon icon
    ) {

        JLabel label =
                new JLabel(
                        text,
                        icon,
                        JLabel.LEFT
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(
                NAVY
        );

        return label;
    }

    // =========================================================
    // ICON BOX
    // =========================================================

    private JLabel createIconBox(
            Icon icon,
            Color background,
            int width,
            int height
    ) {

        JLabel label =
                new JLabel(
                        icon,
                        SwingConstants.CENTER
                );

        label.setOpaque(
                true
        );

        label.setBackground(
                background
        );

        label.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        return label;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground,
            Icon icon
    ) {

        JButton button =
                new JButton(
                        text,
                        icon
                );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setForeground(
                foreground
        );

        button.setBackground(
                background
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setIconTextGap(
                7
        );

        button.setMargin(
                new Insets(
                        6,
                        10,
                        6,
                        10
                )
        );

        return button;
    }

    // =========================================================
    // COLOR BLEND
    // =========================================================

    private Color blend(
            Color a,
            Color b,
            float ratio
    ) {

        int r =
                (int) (
                        a.getRed()
                                * (1 - ratio)
                                + b.getRed()
                                * ratio
                );

        int g =
                (int) (
                        a.getGreen()
                                * (1 - ratio)
                                + b.getGreen()
                                * ratio
                );

        int bl =
                (int) (
                        a.getBlue()
                                * (1 - ratio)
                                + b.getBlue()
                                * ratio
                );

        return new Color(
                r,
                g,
                bl
        );
    }

    // =========================================================
    // LOAD DOUBTS
    // =========================================================

    private void loadDoubts() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(
                0
        );

        int total = 0;

        int pending = 0;

        int resolved = 0;

        String sql =
                "SELECT doubt_id, student_name, subject, question, "
                        + "image_path, status "
                        + "FROM doubts "
                        + "ORDER BY doubt_id DESC";

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

            while (
                    resultSet.next()
            ) {

                String status =
                        resultSet.getString(
                                "status"
                        );

                String imagePath =
                        resultSet.getString(
                                "image_path"
                        );

                if (
                        status == null
                                || status.trim().isEmpty()
                ) {

                    status = "Pending";
                }

                tableModel.addRow(
                        new Object[]{
                                resultSet.getInt(
                                        "doubt_id"
                                ),

                                safe(
                                        resultSet.getString(
                                                "student_name"
                                        )
                                ),

                                safe(
                                        resultSet.getString(
                                                "subject"
                                        )
                                ),

                                safe(
                                        resultSet.getString(
                                                "question"
                                        )
                                ),

                                imagePath == null
                                        ? ""
                                        : imagePath,

                                status
                        }
                );

                total++;

                if (
                        "Resolved".equalsIgnoreCase(
                                status
                        )
                ) {

                    resolved++;

                } else {

                    pending++;
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading doubts:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        totalDoubtsLabel.setText(
                String.valueOf(
                        total
                )
        );

        pendingDoubtsLabel.setText(
                String.valueOf(
                        pending
                )
        );

        resolvedDoubtsLabel.setText(
                String.valueOf(
                        resolved
                )
        );
    }

    // =========================================================
    // LOAD EXISTING ANSWER
    // =========================================================

    private void loadExistingAnswer(
            int doubtId
    ) {

        String sql =
                "SELECT answer, status "
                        + "FROM doubts "
                        + "WHERE doubt_id = ?";

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
                    doubtId
            );

            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {

                if (
                        rs.next()
                ) {

                    String answer =
                            rs.getString(
                                    "answer"
                            );

                    answerArea.setText(
                            answer == null
                                    ? ""
                                    : answer
                    );
                }
            }

        } catch (SQLException e) {

            answerArea.setText(
                    ""
            );
        }
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // IMAGE PATH
    // =========================================================

    private String normalizeImagePath(
            String imagePath
    ) {

        if (
                imagePath == null
                        || imagePath.trim().isEmpty()
                        || "null".equalsIgnoreCase(
                        imagePath.trim()
                )
        ) {

            return null;
        }

        return imagePath
                .trim()
                .replace(
                        "/",
                        File.separator
                );
    }

    // =========================================================
    // FIND IMAGE FILE
    // =========================================================

    private File findImageFile(
            String imagePath
    ) {

        if (
                imagePath == null
                        || imagePath.trim().isEmpty()
        ) {

            return null;
        }

        File direct =
                new File(
                        imagePath
                );

        if (
                direct.exists()
                        && direct.isFile()
        ) {

            return direct;
        }

        File relative =
                new File(
                        System.getProperty(
                                "user.dir"
                        ),
                        imagePath
                );

        if (
                relative.exists()
                        && relative.isFile()
        ) {

            return relative;
        }

        return null;
    }

    // =========================================================
    // LOAD IMAGE PREVIEW
    // =========================================================

    private void loadImagePreview(
            String imagePath
    ) {

        selectedImagePath =
                imagePath;

        if (
                imagePath == null
        ) {

            imagePreviewLabel.setIcon(
                    null
            );

            imagePreviewLabel.setText(
                    "No attachment"
            );

            attachmentStatusLabel.setText(
                    "No image attached"
            );

            viewImageButton.setEnabled(
                    false
            );

            return;
        }

        File imageFile =
                findImageFile(
                        imagePath
                );

        if (
                imageFile == null
        ) {

            imagePreviewLabel.setIcon(
                    null
            );

            imagePreviewLabel.setText(
                    "Image not found"
            );

            attachmentStatusLabel.setText(
                    "Attachment path could not be found"
            );

            viewImageButton.setEnabled(
                    false
            );

            return;
        }

        try {

            BufferedImage image =
                    ImageIO.read(
                            imageFile
                    );

            if (
                    image == null
            ) {

                throw new Exception(
                        "Invalid image file"
                );
            }

            Image scaled =
                    scaleImage(
                            image,
                            390,
                            125
                    );

            imagePreviewLabel.setText(
                    ""
            );

            imagePreviewLabel.setIcon(
                    new ImageIcon(
                            scaled
                    )
            );

            attachmentStatusLabel.setText(
                    "Attachment: "
                            + imageFile.getName()
            );

            viewImageButton.setEnabled(
                    true
            );

        } catch (Exception e) {

            imagePreviewLabel.setIcon(
                    null
            );

            imagePreviewLabel.setText(
                    "Unable to preview image"
            );

            attachmentStatusLabel.setText(
                    "Invalid or unsupported image"
            );

            viewImageButton.setEnabled(
                    false
            );
        }
    }

    // =========================================================
    // SCALE IMAGE
    // =========================================================

    private Image scaleImage(
            BufferedImage image,
            int maxWidth,
            int maxHeight
    ) {

        int width =
                image.getWidth();

        int height =
                image.getHeight();

        double ratio =
                Math.min(
                        (double) maxWidth / width,
                        (double) maxHeight / height
                );

        int newWidth =
                Math.max(
                        1,
                        (int) (
                                width * ratio
                        )
                );

        int newHeight =
                Math.max(
                        1,
                        (int) (
                                height * ratio
                        )
                );

        return image.getScaledInstance(
                newWidth,
                newHeight,
                Image.SCALE_SMOOTH
        );
    }

    // =========================================================
    // FULL IMAGE VIEWER
    // =========================================================

    private void showFullImage() {

        if (
                selectedImagePath == null
        ) {

            return;
        }

        File imageFile =
                findImageFile(
                        selectedImagePath
                );

        if (
                imageFile == null
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Attached image could not be found.",
                    "Image Not Found",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            BufferedImage image =
                    ImageIO.read(
                            imageFile
                    );

            if (
                    image == null
            ) {

                throw new Exception(
                        "Invalid image"
                );
            }

            Dimension screen =
                    Toolkit
                            .getDefaultToolkit()
                            .getScreenSize();

            Image scaled =
                    scaleImage(
                            image,
                            Math.max(
                                    500,
                                    screen.width - 180
                            ),
                            Math.max(
                                    400,
                                    screen.height - 220
                            )
                    );

            JDialog dialog =
                    new JDialog(
                            this,
                            "Doubt Attachment",
                            true
                    );

            dialog.setLayout(
                    new BorderLayout(
                            10,
                            10
                    )
            );

            dialog
                    .getContentPane()
                    .setBackground(
                            WHITE
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

            imageLabel.setBorder(
                    new EmptyBorder(
                            12,
                            12,
                            5,
                            12
                    )
            );

            JButton close =
                    createButton(
                            "Close",
                            NAVY_DARK,
                            WHITE,
                            new CloseIcon(
                                    WHITE,
                                    14
                            )
                    );

            close.setPreferredSize(
                    new Dimension(
                            100,
                            34
                    )
            );

            close.addActionListener(
                    e -> dialog.dispose()
            );

            JPanel bottom =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.RIGHT
                            )
                    );

            bottom.setBackground(
                    WHITE
            );

            bottom.setBorder(
                    new EmptyBorder(
                            0,
                            10,
                            10,
                            10
                    )
            );

            bottom.add(
                    close
            );

            dialog.add(
                    new JScrollPane(
                            imageLabel
                    ),
                    BorderLayout.CENTER
            );

            dialog.add(
                    bottom,
                    BorderLayout.SOUTH
            );

            dialog.setSize(
                    Math.min(
                            screen.width - 80,
                            1100
                    ),
                    Math.min(
                            screen.height - 80,
                            760
                    )
            );

            dialog.setLocationRelativeTo(
                    this
            );

            dialog.setVisible(
                    true
            );

        } catch (Exception e) {

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
    // ANSWER DOUBT
    // =========================================================

    private void answerDoubt() {

        int selectedRow =
                doubtsTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a doubt first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String answer =
                answerArea
                        .getText()
                        .trim();

        if (
                answer.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter an answer.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            answerArea.requestFocusInWindow();

            return;
        }

        int modelRow =
                doubtsTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        int doubtId =
                Integer.parseInt(
                        String.valueOf(
                                tableModel.getValueAt(
                                        modelRow,
                                        0
                                )
                        )
                );

        String status =
                String.valueOf(
                        statusComboBox
                                .getSelectedItem()
                );

        String sql =
                "UPDATE doubts "
                        + "SET answer = ?, "
                        + "status = ?, "
                        + "answered_by = ? "
                        + "WHERE doubt_id = ?";

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
                    answer
            );

            statement.setString(
                    2,
                    status
            );

            statement.setString(
                    3,
                    facultyName
            );

            statement.setInt(
                    4,
                    doubtId
            );

            int rows =
                    statement.executeUpdate();

            if (
                    rows > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Answer submitted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadDoubts();

                clearFields();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error submitting answer:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // DELETE DOUBT
    // =========================================================

    private void deleteDoubt() {

        int selectedRow =
                doubtsTable.getSelectedRow();

        if (
                selectedRow == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a doubt to delete.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                doubtsTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        int doubtId =
                Integer.parseInt(
                        String.valueOf(
                                tableModel.getValueAt(
                                        modelRow,
                                        0
                                )
                        )
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this doubt?",
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
                "DELETE FROM doubts "
                        + "WHERE doubt_id = ?";

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
                    doubtId
            );

            int rows =
                    statement.executeUpdate();

            if (
                    rows > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Doubt deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadDoubts();

                clearFields();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting doubt:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private void clearFields() {

        doubtsTable.clearSelection();

        selectedImagePath =
                null;

        selectedIdLabel.setText(
                "ID: --"
        );

        selectedStudentLabel.setText(
                "Select a doubt"
        );

        selectedSubjectLabel.setText(
                "Select a doubt"
        );

        selectedStatusLabel.setText(
                "Select a doubt"
        );

        selectedStatusLabel.setForeground(
                TEXT
        );

        questionPreviewArea.setText(
                "Select a doubt to view the question."
        );

        answerArea.setText(
                ""
        );

        statusComboBox.setSelectedItem(
                "Pending"
        );

        imagePreviewLabel.setIcon(
                null
        );

        imagePreviewLabel.setText(
                "No attachment"
        );

        attachmentStatusLabel.setText(
                "No image attached"
        );

        viewImageButton.setEnabled(
                false
        );
    }

    // =========================================================
    // TABLE RENDERERS
    // =========================================================

    private static class CenterRenderer
            extends DefaultTableCellRenderer {

        CenterRenderer() {

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            Component component =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            selected,
                            focus,
                            row,
                            column
                    );

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setBorder(
                    new EmptyBorder(
                            0,
                            5,
                            0,
                            5
                    )
            );

            return component;
        }
    }

    // =========================================================
    // QUESTION RENDERER
    // =========================================================

    private static class QuestionRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    selected,
                                    focus,
                                    row,
                                    column
                            );

            label.setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            8
                    )
            );

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
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            JLabel label =
                    new JLabel();

            label.setOpaque(
                    true
            );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            boolean attached =
                    value != null
                            && !value
                            .toString()
                            .trim()
                            .isEmpty()
                            && !"null"
                            .equalsIgnoreCase(
                                    value
                                            .toString()
                            );

            if (
                    attached
            ) {

                label.setText(
                        "Attached"
                );

                label.setIcon(
                        new ImageIconShape(
                                TEAL,
                                13
                        )
                );

                label.setForeground(
                        TEAL_DARK
                );

                label.setBackground(
                        new Color(
                                239,
                                250,
                                252
                        )
                );

            } else {

                label.setText(
                        "No Image"
                );

                label.setForeground(
                        new Color(
                                130,
                                148,
                                163
                        )
                );

                label.setBackground(
                        WHITE
                );
            }

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            10
                    )
            );

            return label;
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
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            JLabel label =
                    new JLabel();

            label.setOpaque(
                    true
            );

            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            String status =
                    value == null
                            ? "Pending"
                            : value.toString();

            if (
                    status.equalsIgnoreCase(
                            "Resolved"
                    )
            ) {

                label.setText(
                        "Resolved"
                );

                label.setIcon(
                        new CheckIcon(
                                SUCCESS,
                                13
                        )
                );

                label.setForeground(
                        SUCCESS
                );

                label.setBackground(
                        new Color(
                                239,
                                251,
                                244
                        )
                );

            } else {

                label.setText(
                        "Pending"
                );

                label.setIcon(
                        new ClockIcon(
                                WARNING,
                                13
                        )
                );

                label.setForeground(
                        WARNING
                );

                label.setBackground(
                        new Color(
                                255,
                                247,
                                235
                        )
                );
            }

            label.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            10
                    )
            );

            return label;
        }
    }

    // =========================================================
    // CAMPUSCONNECT BACKGROUND
    // Same visual family as ManageAnnouncements
    // =========================================================

    private static class CampusBackgroundPanel
            extends JPanel {

        CampusBackgroundPanel() {

            setOpaque(
                    true
            );

            setBackground(
                    PAGE_BG
            );
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(
                    g
            );

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w =
                    getWidth();

            int h =
                    getHeight();

            // -------------------------------------------------
            // LARGE WHITE ABSTRACT CIRCLES
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            125
                    )
            );

            g2.fillOval(
                    w - 430,
                    95,
                    530,
                    330
            );

            g2.fillOval(
                    w - 520,
                    h - 230,
                    610,
                    340
            );

            g2.fillOval(
                    -190,
                    h - 120,
                    390,
                    240
            );

            // -------------------------------------------------
            // TEAL ABSTRACT SHAPES
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            25,
                            166,
                            181,
                            22
                    )
            );

            g2.fillOval(
                    w - 300,
                    55,
                    230,
                    150
            );

            g2.fillOval(
                    35,
                    h - 120,
                    180,
                    110
            );

            // -------------------------------------------------
            // DOT PATTERN
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            25,
                            166,
                            181,
                            65
                    )
            );

            for (
                    int x = 38;
                    x < 160;
                    x += 18
            ) {

                for (
                        int y = h - 90;
                        y < h - 25;
                        y += 18
                ) {

                    g2.fillOval(
                            x,
                            y,
                            4,
                            4
                    );
                }
            }

            // -------------------------------------------------
            // ABSTRACT ARCS
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            24,
                            67,
                            108,
                            15
                    )
            );

            g2.drawArc(
                    w - 370,
                    90,
                    420,
                    300,
                    205,
                    125
            );

            g2.drawArc(
                    w - 330,
                    105,
                    380,
                    260,
                    205,
                    125
            );

            g2.dispose();
        }
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel {

        private final int radius;

        private final Color fill;

        RoundedPanel(
                int radius,
                Color fill
        ) {

            this.radius =
                    radius;

            this.fill =
                    fill;

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

            g2.setColor(
                    fill
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(
                    g
            );
        }
    }

    // =========================================================
    // BASE ICON
    // =========================================================

    private abstract static class BaseIcon
            implements Icon {

        protected final Color color;

        protected final int size;

        BaseIcon(
                Color color,
                int size
        ) {

            this.color =
                    color;

            this.size =
                    size;
        }

        @Override
        public int getIconWidth() {

            return size;
        }

        @Override
        public int getIconHeight() {

            return size;
        }

        protected Graphics2D graphics(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    color
            );

            g2.setStroke(
                    new BasicStroke(
                            Math.max(
                                    1.5f,
                                    size / 8f
                            ),
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            return g2;
        }
    }

    // =========================================================
    // DOUBT ICON
    // =========================================================

    private static class DoubtIcon
            extends BaseIcon {

        DoubtIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            int s =
                    size;

            p.drawRoundRect(
                    x + s * 3 / 10,
                    y + s / 5,
                    s * 2 / 5,
                    s / 2,
                    s / 8,
                    s / 8
            );

            p.drawLine(
                    x + s * 3 / 10,
                    y + s * 7 / 10,
                    x + s / 2,
                    y + s * 9 / 10
            );

            p.drawLine(
                    x + s / 2,
                    y + s * 9 / 10,
                    x + s * 7 / 10,
                    y + s * 7 / 10
            );

            p.drawLine(
                    x + s * 4 / 10,
                    y + s * 7 / 20,
                    x + s * 3 / 5,
                    y + s * 7 / 20
            );

            p.dispose();
        }
    }

    // =========================================================
    // CAMPUS ICON
    // =========================================================

    private static class CampusIcon
            extends BaseIcon {

        CampusIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            int s =
                    size;

            Polygon roof =
                    new Polygon(
                            new int[]{
                                    x + s / 10,
                                    x + s / 2,
                                    x + s * 9 / 10
                            },
                            new int[]{
                                    y + s * 2 / 5,
                                    y + s / 10,
                                    y + s * 2 / 5
                            },
                            3
                    );

            p.fillPolygon(
                    roof
            );

            p.drawLine(
                    x + s / 5,
                    y + s * 2 / 5,
                    x + s / 5,
                    y + s * 8 / 10
            );

            p.drawLine(
                    x + s * 4 / 10,
                    y + s * 2 / 5,
                    x + s * 4 / 10,
                    y + s * 8 / 10
            );

            p.drawLine(
                    x + s * 6 / 10,
                    y + s * 2 / 5,
                    x + s * 6 / 10,
                    y + s * 8 / 10
            );

            p.drawLine(
                    x + s * 8 / 10,
                    y + s * 2 / 5,
                    x + s * 8 / 10,
                    y + s * 8 / 10
            );

            p.drawLine(
                    x + s / 10,
                    y + s * 8 / 10,
                    x + s * 9 / 10,
                    y + s * 8 / 10
            );

            p.dispose();
        }
    }

    // =========================================================
    // REFRESH ICON
    // =========================================================

    private static class RefreshIcon
            extends BaseIcon {

        RefreshIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawArc(
                    x + 2,
                    y + 2,
                    size - 4,
                    size - 4,
                    35,
                    285
            );

            p.drawLine(
                    x + size - 2,
                    y + 5,
                    x + size - 2,
                    y + 11
            );

            p.drawLine(
                    x + size - 2,
                    y + 5,
                    x + size - 8,
                    y + 5
            );

            p.dispose();
        }
    }

    // =========================================================
    // CLIPBOARD ICON
    // =========================================================

    private static class ClipboardIcon
            extends BaseIcon {

        ClipboardIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawRoundRect(
                    x + 4,
                    y + 4,
                    size - 8,
                    size - 7,
                    3,
                    3
            );

            p.drawRoundRect(
                    x + size / 3,
                    y + 1,
                    size / 3,
                    6,
                    2,
                    2
            );

            p.drawLine(
                    x + 7,
                    y + size / 2,
                    x + size - 7,
                    y + size / 2
            );

            p.drawLine(
                    x + 7,
                    y + size * 2 / 3,
                    x + size - 9,
                    y + size * 2 / 3
            );

            p.dispose();
        }
    }

    // =========================================================
    // CLOCK ICON
    // =========================================================

    private static class ClockIcon
            extends BaseIcon {

        ClockIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawOval(
                    x + 2,
                    y + 2,
                    size - 4,
                    size - 4
            );

            p.drawLine(
                    x + size / 2,
                    y + size / 2,
                    x + size / 2,
                    y + 6
            );

            p.drawLine(
                    x + size / 2,
                    y + size / 2,
                    x + size * 7 / 10,
                    y + size * 6 / 10
            );

            p.dispose();
        }
    }

    // =========================================================
    // CHECK ICON
    // =========================================================

    private static class CheckIcon
            extends BaseIcon {

        CheckIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawLine(
                    x + 2,
                    y + size / 2,
                    x + size * 2 / 5,
                    y + size - 3
            );

            p.drawLine(
                    x + size * 2 / 5,
                    y + size - 3,
                    x + size - 2,
                    y + 3
            );

            p.dispose();
        }
    }

    // =========================================================
    // SEARCH ICON
    // =========================================================

    private static class SearchIcon
            extends BaseIcon {

        SearchIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawOval(
                    x + 2,
                    y + 2,
                    size * 2 / 3,
                    size * 2 / 3
            );

            p.drawLine(
                    x + size * 3 / 5,
                    y + size * 3 / 5,
                    x + size - 2,
                    y + size - 2
            );

            p.dispose();
        }
    }

    // =========================================================
    // USER ICON
    // =========================================================

    private static class UserIcon
            extends BaseIcon {

        UserIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawOval(
                    x + size * 3 / 8,
                    y + 2,
                    size / 4,
                    size / 4
            );

            p.drawArc(
                    x + size / 5,
                    y + size * 2 / 5,
                    size * 3 / 5,
                    size / 2,
                    0,
                    180
            );

            p.dispose();
        }
    }

    // =========================================================
    // BOOK ICON
    // =========================================================

    private static class BookIcon
            extends BaseIcon {

        BookIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawRoundRect(
                    x + 2,
                    y + 3,
                    size - 4,
                    size - 6,
                    2,
                    2
            );

            p.drawLine(
                    x + size / 2,
                    y + 3,
                    x + size / 2,
                    y + size - 3
            );

            p.dispose();
        }
    }

    // =========================================================
    // DETAIL ICON
    // =========================================================

    private static class DetailIcon
            extends BaseIcon {

        DetailIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawRoundRect(
                    x + 3,
                    y + 2,
                    size - 6,
                    size - 4,
                    3,
                    3
            );

            p.drawLine(
                    x + 7,
                    y + 8,
                    x + size - 7,
                    y + 8
            );

            p.drawLine(
                    x + 7,
                    y + 13,
                    x + size - 7,
                    y + 13
            );

            p.drawLine(
                    x + 7,
                    y + 18,
                    x + size - 10,
                    y + 18
            );

            p.dispose();
        }
    }

    // =========================================================
    // QUESTION ICON
    // =========================================================

    private static class QuestionIcon
            extends BaseIcon {

        QuestionIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawOval(
                    x + 2,
                    y + 2,
                    size - 4,
                    size - 4
            );

            p.drawArc(
                    x + 5,
                    y + 4,
                    size - 10,
                    size - 10,
                    20,
                    220
            );

            p.fillOval(
                    x + size / 2 - 1,
                    y + size * 3 / 5,
                    3,
                    3
            );

            p.dispose();
        }
    }

    // =========================================================
    // IMAGE ICON
    // =========================================================

    private static class ImageIconShape
            extends BaseIcon {

        ImageIconShape(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawRect(
                    x + 2,
                    y + 3,
                    size - 4,
                    size - 6
            );

            p.drawOval(
                    x + 5,
                    y + 5,
                    4,
                    4
            );

            p.drawLine(
                    x + 4,
                    y + size - 5,
                    x + size / 2,
                    y + size / 2
            );

            p.drawLine(
                    x + size / 2,
                    y + size / 2,
                    x + size - 4,
                    y + size - 5
            );

            p.dispose();
        }
    }

    // =========================================================
    // ANSWER ICON
    // =========================================================

    private static class AnswerIcon
            extends BaseIcon {

        AnswerIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawRoundRect(
                    x + 2,
                    y + 3,
                    size - 4,
                    size - 6,
                    3,
                    3
            );

            p.drawLine(
                    x + 5,
                    y + size - 3,
                    x + 9,
                    y + size - 6
            );

            p.drawLine(
                    x + 6,
                    y + 8,
                    x + size - 6,
                    y + 8
            );

            p.drawLine(
                    x + 6,
                    y + 12,
                    x + size - 8,
                    y + 12
            );

            p.dispose();
        }
    }

    // =========================================================
    // TRASH ICON
    // =========================================================

    private static class TrashIcon
            extends BaseIcon {

        TrashIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawRoundRect(
                    x + 4,
                    y + 5,
                    size - 8,
                    size - 7,
                    2,
                    2
            );

            p.drawLine(
                    x + 3,
                    y + 4,
                    x + size - 3,
                    y + 4
            );

            p.drawLine(
                    x + size / 3,
                    y + 2,
                    x + size * 2 / 3,
                    y + 2
            );

            p.dispose();
        }
    }

    // =========================================================
    // CLEAR ICON
    // =========================================================

    private static class ClearIcon
            extends BaseIcon {

        ClearIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawLine(
                    x + 3,
                    y + 3,
                    x + size - 3,
                    y + size - 3
            );

            p.drawLine(
                    x + size - 3,
                    y + 3,
                    x + 3,
                    y + size - 3
            );

            p.dispose();
        }
    }

    // =========================================================
    // CLOSE ICON
    // =========================================================

    private static class CloseIcon
            extends BaseIcon {

        CloseIcon(
                Color color,
                int size
        ) {

            super(
                    color,
                    size
            );
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D p =
                    graphics(
                            g
                    );

            p.drawLine(
                    x + 3,
                    y + 3,
                    x + size - 3,
                    y + size - 3
            );

            p.drawLine(
                    x + size - 3,
                    y + 3,
                    x + 3,
                    y + size - 3
            );

            p.dispose();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new FacultyDoubtsFrame("Faculty Name", "faculty@example.com")
        );
    }
}