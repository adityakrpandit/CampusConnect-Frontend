package com.campusconnect.ui.admin;

import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

/**
 * CampusConnect - Admin Announcement Management
 *
 * Database connection and table structure remain unchanged.
 *
 * Table:
 * announcements
 *
 * Columns:
 * announcement_id
 * title
 * message
 * announcement_date
 */
public class ManageAnnouncementsFrame extends JPanel {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG =
            new Color(232, 247, 251);

    private static final Color BG_LIGHT =
            new Color(242, 250, 252);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color NAVY =
            new Color(22, 63, 103);

    private static final Color NAVY_DARK =
            new Color(17, 49, 82);

    private static final Color TEAL =
            new Color(24, 165, 177);

    private static final Color TEAL_DARK =
            new Color(13, 137, 150);

    private static final Color BLUE =
            new Color(57, 105, 181);

    private static final Color BLUE_DARK =
            new Color(43, 87, 157);

    private static final Color RED =
            new Color(205, 72, 75);

    private static final Color RED_DARK =
            new Color(177, 54, 57);

    private static final Color GRAY =
            new Color(106, 125, 145);

    private static final Color GRAY_DARK =
            new Color(83, 102, 121);

    private static final Color TEXT =
            new Color(27, 53, 78);

    private static final Color MUTED =
            new Color(91, 116, 140);

    private static final Color BORDER =
            new Color(205, 223, 232);

    private static final Color TABLE_HEADER =
            new Color(22, 63, 103);

    private static final Color SELECTED =
            new Color(221, 241, 246);

    private static final Color GREEN =
            new Color(36, 156, 91);

    private static final Color TODAY =
            new Color(226, 244, 247);


    // =========================================================
    // DATE FORMAT
    // =========================================================

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");


    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTable announcementTable;

    private DefaultTableModel tableModel;

    private JTextField searchField;

    private JTextField titleField;

    private JTextField dateField;

    private JTextArea messageArea;

    private JLabel totalLabel;

    private JLabel latestLabel;

    private JButton refreshButton;

    private JButton clearSelectionButton;

    private JButton deleteButton;

    private JButton clearButton;

    private JButton addButton;

    private JButton closeButton;

    private JButton calendarButton;

    private final Runnable onClose;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ManageAnnouncementsFrame() {
        this(() -> {
        });
    }

    public ManageAnnouncementsFrame(Runnable onClose) {
        this.onClose = onClose == null ? () -> {
        } : onClose;

        setLayout(new BorderLayout());

        buildUI();
        loadAnnouncements();
    }

    // =========================================================
    // MAIN UI
    // =========================================================

    private void buildUI() {

        BackgroundPanel background =
                new BackgroundPanel();

        background.setLayout(
                new BorderLayout()
        );

        // HEADER
        background.add(
                createHeader(),
                BorderLayout.NORTH
        );

        // MAIN CONTENT
        JPanel content =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(
                        12,
                        32,
                        8,
                        32
                )
        );

        // TOP AREA
        JPanel topArea =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        topArea.setOpaque(false);

        topArea.add(
                createStatisticsPanel(),
                BorderLayout.NORTH
        );

        topArea.add(
                createAnnouncementsSection(),
                BorderLayout.CENTER
        );

        content.add(
                topArea,
                BorderLayout.CENTER
        );

        // ADD SECTION
        content.add(
                createAddAnnouncementSection(),
                BorderLayout.SOUTH
        );

        background.add(
                content,
                BorderLayout.CENTER
        );

        // FOOTER
        background.add(
                createFooter(),
                BorderLayout.SOUTH
        );

        removeAll();
        add(background, BorderLayout.CENTER);
        revalidate();
        repaint();
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
                                36,
                                12,
                                36
                        )
                )
        );


        // LEFT
        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                15,
                                0
                        )
                );

        left.setOpaque(false);


        JPanel iconBox =
                createIconBox(
                        new MegaphoneIcon(
                                Color.WHITE
                        ),
                        TEAL,
                        58,
                        58
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


        JLabel smallTitle =
                new JLabel(
                        "CAMPUSCONNECT"
                );

        smallTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        smallTitle.setForeground(
                TEAL
        );


        JLabel title =
                new JLabel(
                        "Manage Announcements"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(
                TEXT
        );


        JLabel subtitle =
                new JLabel(
                        "Create, publish and manage important campus updates"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                MUTED
        );


        titlePanel.add(
                smallTitle
        );

        titlePanel.add(
                Box.createVerticalStrut(2)
        );

        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(2)
        );

        titlePanel.add(
                subtitle
        );


        left.add(
                iconBox
        );

        left.add(
                titlePanel
        );


        // RIGHT BRAND
        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                2
                        )
                );

        right.setOpaque(false);


        JPanel ccBox =
                new JPanel(
                        new GridBagLayout()
                );

        ccBox.setPreferredSize(
                new Dimension(
                        58,
                        64
                )
        );

        ccBox.setBackground(
                TEAL
        );


        JLabel cc =
                new JLabel(
                        "CC"
                );

        cc.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        cc.setForeground(
                Color.WHITE
        );

        ccBox.add(
                cc
        );


        JPanel brandPanel =
                new JPanel();

        brandPanel.setOpaque(false);

        brandPanel.setLayout(
                new BoxLayout(
                        brandPanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel brand =
                new JLabel(
                        "CampusConnect"
                );

        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        brand.setForeground(
                TEXT
        );


        JLabel portal =
                new JLabel(
                        "Admin Portal"
                );

        portal.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        portal.setForeground(
                MUTED
        );


        brandPanel.add(
                brand
        );

        brandPanel.add(
                Box.createVerticalStrut(2)
        );

        brandPanel.add(
                portal
        );


        right.add(
                ccBox
        );

        right.add(
                brandPanel
        );


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
    // TOP STATISTICS + SEARCH
    // =========================================================

    private JPanel createStatisticsPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        panel.setOpaque(false);


        // -----------------------------------------------------
        // TOTAL SECTION
        // -----------------------------------------------------

        JPanel totalSection =
                createFlatSection();

        totalSection.setPreferredSize(
                new Dimension(
                        215,
                        74
                )
        );


        JPanel totalIcon =
                createIconBox(
                        new LetterIcon(
                                "A",
                                Color.WHITE
                        ),
                        TEAL,
                        46,
                        46
                );


        JPanel totalText =
                createVerticalTextPanel();


        JLabel totalTitle =
                smallLabel(
                        "TOTAL ANNOUNCEMENTS"
                );


        totalLabel =
                new JLabel(
                        "0"
                );

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        totalLabel.setForeground(
                TEAL
        );


        totalText.add(
                totalTitle
        );

        totalText.add(
                Box.createVerticalStrut(2)
        );

        totalText.add(
                totalLabel
        );


        totalSection.add(
                totalIcon,
                BorderLayout.WEST
        );

        totalSection.add(
                totalText,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // LATEST SECTION
        // -----------------------------------------------------

        JPanel latestSection =
                createFlatSection();

        latestSection.setPreferredSize(
                new Dimension(
                        215,
                        74
                )
        );


        JPanel latestIcon =
                createIconBox(
                        new CalendarMiniIcon(
                                Color.WHITE
                        ),
                        NAVY,
                        46,
                        46
                );


        JPanel latestText =
                createVerticalTextPanel();


        JLabel latestTitle =
                smallLabel(
                        "LATEST UPDATE"
                );


        latestLabel =
                new JLabel(
                        "No data"
                );

        latestLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        latestLabel.setForeground(
                NAVY
        );


        latestText.add(
                latestTitle
        );

        latestText.add(
                Box.createVerticalStrut(3)
        );

        latestText.add(
                latestLabel
        );


        latestSection.add(
                latestIcon,
                BorderLayout.WEST
        );

        latestSection.add(
                latestText,
                BorderLayout.CENTER
        );


        JPanel leftStats =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                12,
                                0
                        )
                );

        leftStats.setOpaque(false);

        leftStats.setPreferredSize(
                new Dimension(
                        442,
                        74
                )
        );

        leftStats.add(
                totalSection
        );

        leftStats.add(
                latestSection
        );


        // -----------------------------------------------------
        // SEARCH
        // -----------------------------------------------------

        JPanel searchSection =
                createFlatSection();

        JPanel searchInfo =
                createVerticalTextPanel();


        JLabel searchTitle =
                smallLabel(
                        "SEARCH"
                );


        JLabel searchHint =
                new JLabel(
                        "Find by title or message"
                );

        searchHint.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        searchHint.setForeground(
                MUTED
        );


        searchInfo.add(
                searchTitle
        );

        searchInfo.add(
                Box.createVerticalStrut(1)
        );

        searchInfo.add(
                searchHint
        );


        searchField =
                new JTextField();

        styleTextField(
                searchField
        );

        searchField.setPreferredSize(
                new Dimension(
                        300,
                        38
                )
        );


        refreshButton =
                createActionButton(
                        "↻  Refresh",
                        BLUE,
                        BLUE_DARK,
                        96,
                        38
                );


        JPanel searchRight =
                new JPanel(
                        new BorderLayout(
                                9,
                                0
                        )
                );

        searchRight.setOpaque(false);


        searchRight.add(
                searchField,
                BorderLayout.CENTER
        );

        searchRight.add(
                refreshButton,
                BorderLayout.EAST
        );


        searchSection.add(
                searchInfo,
                BorderLayout.WEST
        );

        searchSection.add(
                searchRight,
                BorderLayout.CENTER
        );


        refreshButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadAnnouncements();
                }
        );


        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            private void update() {

                                filterAnnouncements(
                                        searchField
                                                .getText()
                                                .trim()
                                );
                            }

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                update();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                update();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                update();
                            }
                        }
                );


        panel.add(
                leftStats,
                BorderLayout.WEST
        );

        panel.add(
                searchSection,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================================================
    // CURRENT ANNOUNCEMENTS
    // =========================================================

    private JPanel createAnnouncementsSection() {

        JPanel section =
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );

        section.setBackground(
                WHITE
        );

        section.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                13,
                                20,
                                10,
                                20
                        )
                )
        );


        // -----------------------------------------------------
        // SECTION HEADER
        // -----------------------------------------------------

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);


        JPanel heading =
                new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Current Announcements"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(
                TEXT
        );


        JLabel subtitle =
                new JLabel(
                        "Select an announcement to manage or delete it"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                MUTED
        );


        heading.add(
                title
        );

        heading.add(
                Box.createVerticalStrut(2)
        );

        heading.add(
                subtitle
        );


        JLabel live =
                new JLabel(
                        "● LIVE"
                );

        live.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        live.setForeground(
                GREEN
        );


        header.add(
                heading,
                BorderLayout.WEST
        );

        header.add(
                live,
                BorderLayout.EAST
        );


        // -----------------------------------------------------
        // TABLE
        // -----------------------------------------------------

        String[] columns = {
                "ID",
                "TITLE",
                "MESSAGE",
                "DATE"
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


        announcementTable =
                new JTable(
                        tableModel
                );


        styleAnnouncementTable();


        JScrollPane scrollPane =
                new JScrollPane(
                        announcementTable
                );

        scrollPane.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        scrollPane
                .getViewport()
                .setBackground(
                        WHITE
                );


        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        clearSelectionButton =
                createActionButton(
                        "Clear Selection",
                        GRAY,
                        GRAY_DARK,
                        135,
                        38
                );


        deleteButton =
                createActionButton(
                        "Delete Selected",
                        RED,
                        RED_DARK,
                        140,
                        38
                );


        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttonPanel.setOpaque(false);


        buttonPanel.add(
                clearSelectionButton
        );

        buttonPanel.add(
                deleteButton
        );


        clearSelectionButton.addActionListener(
                e ->
                        announcementTable
                                .clearSelection()
        );


        deleteButton.addActionListener(
                e ->
                        deleteSelectedAnnouncement()
        );


        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        bottom.setOpaque(false);

        bottom.setBorder(
                new EmptyBorder(
                        2,
                        0,
                        0,
                        0
                )
        );

        bottom.add(
                buttonPanel,
                BorderLayout.EAST
        );


        section.add(
                header,
                BorderLayout.NORTH
        );

        section.add(
                scrollPane,
                BorderLayout.CENTER
        );

        section.add(
                bottom,
                BorderLayout.SOUTH
        );


        return section;
    }


    // =========================================================
    // TABLE STYLE
    // =========================================================

    private void styleAnnouncementTable() {

        announcementTable.setRowHeight(
                43
        );

        announcementTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        announcementTable.setForeground(
                TEXT
        );

        announcementTable.setBackground(
                WHITE
        );

        announcementTable.setGridColor(
                BORDER
        );

        announcementTable.setSelectionBackground(
                SELECTED
        );

        announcementTable.setSelectionForeground(
                TEXT
        );

        announcementTable.setShowVerticalLines(
                true
        );

        announcementTable.setShowHorizontalLines(
                true
        );

        announcementTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_LAST_COLUMN
        );


        // HEADER

        JTableHeaderRenderer headerRenderer =
                new JTableHeaderRenderer();


        announcementTable
                .getTableHeader()
                .setDefaultRenderer(
                        headerRenderer
                );


        announcementTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                45
                        )
                );


        announcementTable
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );


        // COLUMN WIDTHS

        announcementTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(75);


        announcementTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(300);


        announcementTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(650);


        announcementTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(150);


        // BODY RENDERER

        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component
                    getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean selected,
                            boolean focused,
                            int row,
                            int column
                    ) {

                        Component component =
                                super
                                        .getTableCellRendererComponent(
                                                table,
                                                value,
                                                selected,
                                                focused,
                                                row,
                                                column
                                        );


                        setBorder(
                                BorderFactory
                                        .createEmptyBorder(
                                                0,
                                                9,
                                                0,
                                                9
                                        )
                        );


                        if (!selected) {

                            component.setBackground(
                                    WHITE
                            );

                            component.setForeground(
                                    TEXT
                            );
                        }


                        if (column == 0 ||
                                column == 3) {

                            setHorizontalAlignment(
                                    SwingConstants.CENTER
                            );

                        } else {

                            setHorizontalAlignment(
                                    SwingConstants.LEFT
                            );
                        }


                        return component;
                    }
                };


        for (
                int i = 0;
                i < 4;
                i++
        ) {

            announcementTable
                    .getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            renderer
                    );
        }
    }


    // =========================================================
    // ADD ANNOUNCEMENT SECTION
    // =========================================================

    private JPanel createAddAnnouncementSection() {

        JPanel section =
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );

        section.setBackground(
                WHITE
        );

        section.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                11,
                                20,
                                11,
                                20
                        )
                )
        );


        // -----------------------------------------------------
        // HEADING
        // -----------------------------------------------------

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setOpaque(false);


        JPanel headingText =
                new JPanel();

        headingText.setOpaque(false);

        headingText.setLayout(
                new BoxLayout(
                        headingText,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Add New Announcement"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(
                TEXT
        );


        JLabel subtitle =
                new JLabel(
                        "Publish an important update for CampusConnect students"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                MUTED
        );


        headingText.add(
                title
        );

        headingText.add(
                Box.createVerticalStrut(2)
        );

        headingText.add(
                subtitle
        );


        JLabel tag =
                new JLabel(
                        "NEW UPDATE"
                );

        tag.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        tag.setForeground(
                TEAL
        );


        heading.add(
                headingText,
                BorderLayout.WEST
        );

        heading.add(
                tag,
                BorderLayout.EAST
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
                        3,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.NORTHWEST;


        // TITLE LABEL

        JLabel titleLabel =
                formLabel(
                        "TITLE"
                );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.36;

        form.add(
                titleLabel,
                gbc
        );


        // DATE LABEL

        JLabel dateLabel =
                formLabel(
                        "DATE"
                );

        gbc.gridx = 1;
        gbc.weightx = 0.22;

        form.add(
                dateLabel,
                gbc
        );


        // MESSAGE LABEL

        JLabel messageLabel =
                formLabel(
                        "MESSAGE"
                );

        gbc.gridx = 2;
        gbc.weightx = 0.42;

        form.add(
                messageLabel,
                gbc
        );


        // TITLE FIELD

        titleField =
                new JTextField();

        styleTextField(
                titleField
        );


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.36;

        form.add(
                titleField,
                gbc
        );


        // DATE PANEL

        JPanel datePanel =
                new JPanel(
                        new BorderLayout(
                                5,
                                0
                        )
                );

        datePanel.setOpaque(false);


        dateField =
                new JTextField(
                        LocalDate.now()
                                .format(
                                        DATE_FORMAT
                                )
                );

        styleTextField(
                dateField
        );


        calendarButton =
                createIconButton(
                        new CalendarIcon(
                                Color.WHITE
                        ),
                        TEAL,
                        TEAL_DARK,
                        42,
                        38
                );


        datePanel.add(
                dateField,
                BorderLayout.CENTER
        );

        datePanel.add(
                calendarButton,
                BorderLayout.EAST
        );


        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.22;

        form.add(
                datePanel,
                gbc
        );


        calendarButton.addActionListener(
                e ->
                        showDatePicker()
        );


        // MESSAGE AREA

        messageArea =
                new JTextArea();

        messageArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        messageArea.setForeground(
                TEXT
        );

        messageArea.setLineWrap(
                true
        );

        messageArea.setWrapStyleWord(
                true
        );

        messageArea.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                7,
                                9,
                                7,
                                9
                        )
                )
        );


        JScrollPane messageScroll =
                new JScrollPane(
                        messageArea
                );

        messageScroll.setBorder(
                new LineBorder(
                        BORDER,
                        1
                )
        );

        messageScroll.setPreferredSize(
                new Dimension(
                        400,
                        58
                )
        );


        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.42;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        form.add(
                messageScroll,
                gbc
        );


        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        clearButton =
                createActionButton(
                        "Clear",
                        GRAY,
                        GRAY_DARK,
                        92,
                        36
                );


        addButton =
                createActionButton(
                        "+  Add Announcement",
                        TEAL,
                        TEAL_DARK,
                        165,
                        36
                );


        closeButton =
                createActionButton(
                        "Close",
                        NAVY,
                        NAVY_DARK,
                        92,
                        36
                );


        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(false);


        buttons.add(
                clearButton
        );

        buttons.add(
                addButton
        );

        buttons.add(
                closeButton
        );


        clearButton.addActionListener(
                e ->
                        clearForm()
        );


        addButton.addActionListener(
                e ->
                        addAnnouncement()
        );


        closeButton.addActionListener(
                e -> onClose.run()
        );


        section.add(
                heading,
                BorderLayout.NORTH
        );

        section.add(
                form,
                BorderLayout.CENTER
        );

        section.add(
                buttons,
                BorderLayout.SOUTH
        );


        return section;
    }


    // =========================================================
    // LOAD ANNOUNCEMENTS
    // =========================================================

    private void loadAnnouncements() {

        if (tableModel == null) {
            return;
        }


        tableModel.setRowCount(
                0
        );


        String query =
                "SELECT announcement_id, title, message, " +
                        "announcement_date " +
                        "FROM announcements " +
                        "ORDER BY announcement_date DESC";


        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(
                                query
                        );

                ResultSet rs =
                        ps.executeQuery()
        ) {

            int count = 0;

            String latestDate = "";


            while (
                    rs.next()
            ) {

                int id =
                        rs.getInt(
                                "announcement_id"
                        );


                String title =
                        rs.getString(
                                "title"
                        );


                String message =
                        rs.getString(
                                "message"
                        );


                Date date =
                        rs.getDate(
                                "announcement_date"
                        );


                String dateText =
                        date != null
                                ? date.toLocalDate()
                                .format(
                                        DATE_FORMAT
                                )
                                : "";


                tableModel.addRow(
                        new Object[]{
                                id,
                                title,
                                message,
                                dateText
                        }
                );


                count++;


                if (
                        latestDate.isEmpty()
                                && date != null
                ) {

                    latestDate =
                            date.toLocalDate()
                                    .format(
                                            DATE_FORMAT
                                    );
                }
            }


            totalLabel.setText(
                    String.valueOf(
                            count
                    )
            );


            if (
                    !latestDate.isEmpty()
            ) {

                latestLabel.setText(
                        latestDate
                );

            } else {

                latestLabel.setText(
                        "No records"
                );
            }


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to load announcements.",
                    e
            );
        }
    }


    // =========================================================
    // ADD ANNOUNCEMENT
    // =========================================================

    private void addAnnouncement() {

        String title =
                titleField
                        .getText()
                        .trim();


        String message =
                messageArea
                        .getText()
                        .trim();


        String dateText =
                dateField
                        .getText()
                        .trim();


        if (
                title.isEmpty()
        ) {

            showWarning(
                    "Please enter the announcement title."
            );

            titleField.requestFocus();

            return;
        }


        if (
                message.isEmpty()
        ) {

            showWarning(
                    "Please enter the announcement message."
            );

            messageArea.requestFocus();

            return;
        }


        if (
                dateText.isEmpty()
        ) {

            showWarning(
                    "Please select the announcement date."
            );

            dateField.requestFocus();

            return;
        }


        Date sqlDate;


        try {

            LocalDate localDate =
                    LocalDate.parse(
                            dateText,
                            DATE_FORMAT
                    );


            sqlDate =
                    Date.valueOf(
                            localDate
                    );

        } catch (
                DateTimeParseException e
        ) {

            showWarning(
                    "Invalid date.\n\n" +
                            "Please use the calendar button " +
                            "or format: YYYY-MM-DD"
            );

            dateField.requestFocus();

            return;
        }


        String query =
                "INSERT INTO announcements " +
                        "(title, message, announcement_date) " +
                        "VALUES (?, ?, ?)";


        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(
                                query
                        )
        ) {

            ps.setString(
                    1,
                    title
            );

            ps.setString(
                    2,
                    message
            );

            ps.setDate(
                    3,
                    sqlDate
            );


            int rows =
                    ps.executeUpdate();


            if (
                    rows > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Announcement added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );


                clearForm();

                loadAnnouncements();
            }


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to add announcement.",
                    e
            );
        }
    }


    // =========================================================
    // DELETE ANNOUNCEMENT
    // =========================================================

    private void deleteSelectedAnnouncement() {

        int selectedRow =
                announcementTable
                        .getSelectedRow();


        if (
                selectedRow == -1
        ) {

            showWarning(
                    "Please select an announcement first."
            );

            return;
        }


        int modelRow =
                announcementTable
                        .convertRowIndexToModel(
                                selectedRow
                        );


        int id =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );


        String title =
                tableModel
                        .getValueAt(
                                modelRow,
                                1
                        )
                        .toString();


        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this announcement?\n\n"
                                + title,
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        if (
                confirm != JOptionPane.YES_OPTION
        ) {
            return;
        }


        String query =
                "DELETE FROM announcements " +
                        "WHERE announcement_id = ?";


        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(
                                query
                        )
        ) {

            ps.setInt(
                    1,
                    id
            );


            int rows =
                    ps.executeUpdate();


            if (
                    rows > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Announcement deleted successfully.",
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE
                );


                loadAnnouncements();
            }


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to delete announcement.",
                    e
            );
        }
    }


    // =========================================================
    // SEARCH / FILTER
    // =========================================================

    private void filterAnnouncements(
            String search
    ) {

        if (
                search.isEmpty()
        ) {

            loadAnnouncements();

            return;
        }


        tableModel.setRowCount(
                0
        );


        String query =
                "SELECT announcement_id, title, message, " +
                        "announcement_date " +
                        "FROM announcements " +
                        "WHERE LOWER(title) LIKE ? " +
                        "OR LOWER(message) LIKE ? " +
                        "ORDER BY announcement_date DESC";


        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(
                                query
                        )
        ) {

            String keyword =
                    "%" +
                            search.toLowerCase() +
                            "%";


            ps.setString(
                    1,
                    keyword
            );

            ps.setString(
                    2,
                    keyword
            );


            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (
                        rs.next()
                ) {

                    int id =
                            rs.getInt(
                                    "announcement_id"
                            );


                    String title =
                            rs.getString(
                                    "title"
                            );


                    String message =
                            rs.getString(
                                    "message"
                            );


                    Date date =
                            rs.getDate(
                                    "announcement_date"
                            );


                    String dateText =
                            date != null
                                    ? date.toLocalDate()
                                    .format(
                                            DATE_FORMAT
                                    )
                                    : "";


                    tableModel.addRow(
                            new Object[]{
                                    id,
                                    title,
                                    message,
                                    dateText
                            }
                    );
                }
            }


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to search announcements.",
                    e
            );
        }
    }


    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {

        titleField.setText(
                ""
        );


        dateField.setText(
                LocalDate.now()
                        .format(
                                DATE_FORMAT
                        )
        );


        messageArea.setText(
                ""
        );


        titleField.requestFocus();
    }


    // =========================================================
    // DATE PICKER
    // =========================================================

    private void showDatePicker() {

        LocalDate initialDate;


        try {

            initialDate =
                    LocalDate.parse(
                            dateField
                                    .getText()
                                    .trim(),
                            DATE_FORMAT
                    );

        } catch (Exception e) {

            initialDate =
                    LocalDate.now();
        }


        final JDialog dialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Select Announcement Date",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setSize(
                440,
                500
        );

        dialog.setMinimumSize(
                new Dimension(
                        440,
                        500
                )
        );

        dialog.setResizable(
                false
        );

        dialog.setLocationRelativeTo(
                SwingUtilities.getWindowAncestor(this)
        );


        JPanel main =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        main.setBackground(
                WHITE
        );

        main.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        16,
                        20
                )
        );


        // -----------------------------------------------------
        // DATE PREVIEW
        // -----------------------------------------------------

        JPanel datePreview =
                new JPanel();

        datePreview.setBackground(
                TEAL
        );

        datePreview.setLayout(
                new BoxLayout(
                        datePreview,
                        BoxLayout.Y_AXIS
                )
        );

        datePreview.setBorder(
                new EmptyBorder(
                        12,
                        16,
                        12,
                        16
                )
        );


        JLabel previewSmall =
                new JLabel(
                        "SELECTED DATE"
                );

        previewSmall.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        previewSmall.setForeground(
                new Color(
                        220,
                        250,
                        252
                )
        );


        JLabel selectedDateLabel =
                new JLabel(
                        initialDate.format(
                                DISPLAY_DATE_FORMAT
                        )
                );

        selectedDateLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        selectedDateLabel.setForeground(
                Color.WHITE
        );


        datePreview.add(
                previewSmall
        );

        datePreview.add(
                Box.createVerticalStrut(3)
        );

        datePreview.add(
                selectedDateLabel
        );


        main.add(
                datePreview,
                BorderLayout.NORTH
        );


        // -----------------------------------------------------
        // CALENDAR
        // -----------------------------------------------------

        CalendarPanel calendar =
                new CalendarPanel(
                        initialDate,
                        date -> {

                            dateField.setText(
                                    date.format(
                                            DATE_FORMAT
                                    )
                            );

                            dialog.dispose();
                        },
                        selectedDateLabel
                );


        main.add(
                calendar,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // BOTTOM BUTTONS
        // -----------------------------------------------------

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        bottom.setOpaque(false);


        JButton todayButton =
                createActionButton(
                        "Today",
                        TEAL,
                        TEAL_DARK,
                        88,
                        34
                );


        JButton cancelButton =
                createActionButton(
                        "Cancel",
                        GRAY,
                        GRAY_DARK,
                        88,
                        34
                );


        todayButton.addActionListener(
                e -> {

                    dateField.setText(
                            LocalDate.now()
                                    .format(
                                            DATE_FORMAT
                                    )
                    );

                    dialog.dispose();
                }
        );


        cancelButton.addActionListener(
                e ->
                        dialog.dispose()
        );


        bottom.add(
                todayButton
        );

        bottom.add(
                cancelButton
        );


        main.add(
                bottom,
                BorderLayout.SOUTH
        );


        dialog.setContentPane(
                main
        );

        dialog.setVisible(
                true
        );
    }


    // =========================================================
    // FLAT SECTION
    // =========================================================

    private JPanel createFlatSection() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        panel.setBackground(
                WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        return panel;
    }


    // =========================================================
    // FOOTER
    // =========================================================

    private JPanel createFooter() {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setOpaque(false);

        footer.setBorder(
                new EmptyBorder(
                        0,
                        36,
                        8,
                        36
                )
        );


        JLabel left =
                new JLabel(
                        "CampusConnect • Admin Announcement Management"
                );

        left.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        left.setForeground(
                MUTED
        );


        JLabel right =
                new JLabel(
                        "Learn  •  Connect  •  Grow Together"
                );

        right.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        right.setForeground(
                TEAL_DARK
        );


        footer.add(
                left,
                BorderLayout.WEST
        );

        footer.add(
                right,
                BorderLayout.EAST
        );


        return footer;
    }


    // =========================================================
    // TEXT FIELD STYLE
    // =========================================================

    private void styleTextField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(
                TEXT
        );

        field.setBackground(
                WHITE
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                6,
                                9,
                                6,
                                9
                        )
                )
        );
    }


    // =========================================================
    // LABEL HELPERS
    // =========================================================

    private JLabel smallLabel(
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
                        10
                )
        );

        label.setForeground(
                MUTED
        );

        return label;
    }


    private JLabel formLabel(
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
                        10
                )
        );

        label.setForeground(
                MUTED
        );

        return label;
    }


    private JPanel createVerticalTextPanel() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        return panel;
    }


    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createActionButton(
            String text,
            Color background,
            Color hoverColor,
            int width,
            int height
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        button.setMinimumSize(
                new Dimension(
                        width,
                        height
                )
        );

        button.setMaximumSize(
                new Dimension(
                        width,
                        height
                )
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                Color.WHITE
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


        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                hoverColor
                        );
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background
                        );
                    }
                }
        );


        return button;
    }


    // =========================================================
    // ICON BUTTON
    // =========================================================

    private JButton createIconButton(
            Icon icon,
            Color background,
            Color hoverColor,
            int width,
            int height
    ) {

        JButton button =
                new JButton(
                        icon
                );

        button.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        button.setMinimumSize(
                new Dimension(
                        width,
                        height
                )
        );

        button.setMaximumSize(
                new Dimension(
                        width,
                        height
                )
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


        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                hoverColor
                        );
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background
                        );
                    }
                }
        );


        return button;
    }


    // =========================================================
    // ICON BOX
    // =========================================================

    private JPanel createIconBox(
            Icon icon,
            Color background,
            int width,
            int height
    ) {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        panel.setMinimumSize(
                new Dimension(
                        width,
                        height
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        width,
                        height
                )
        );

        panel.setBackground(
                background
        );

        panel.add(
                new JLabel(
                        icon
                )
        );

        return panel;
    }


    // =========================================================
    // ERROR HELPERS
    // =========================================================

    private void showWarning(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }


    private void showDatabaseError(
            String message,
            SQLException exception
    ) {

        JOptionPane.showMessageDialog(
                this,
                message +
                        "\n\n" +
                        exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // =========================================================
    // HEADER TABLE RENDERER
    // =========================================================

    private static class JTableHeaderRenderer
            extends DefaultTableCellRenderer {

        public JTableHeaderRenderer() {

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setVerticalAlignment(
                    SwingConstants.CENTER
            );

            setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            12
                    )
            );

            setForeground(
                    Color.WHITE
            );

            setBackground(
                    TABLE_HEADER
            );

            setBorder(
                    BorderFactory.createMatteBorder(
                            0,
                            0,
                            1,
                            1,
                            new Color(
                                    80,
                                    111,
                                    140
                            )
                    )
            );
        }


        @Override
        public Component
        getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focused,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    selected,
                    focused,
                    row,
                    column
            );

            setText(
                    value == null
                            ? ""
                            : value.toString()
            );

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setBackground(
                    TABLE_HEADER
            );

            setForeground(
                    Color.WHITE
            );

            return this;
        }
    }


    // =========================================================
    // BACKGROUND PANEL
    // =========================================================

    private static class BackgroundPanel
            extends JPanel {

        public BackgroundPanel() {

            setOpaque(
                    true
            );

            setBackground(
                    BG
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
                    (Graphics2D) g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            // -------------------------------------------------
            // BASE
            // -------------------------------------------------

            g2.setColor(
                    BG
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );


            // -------------------------------------------------
            // TOP RIGHT LARGE CIRCLE
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            205,
                            235,
                            243,
                            130
                    )
            );

            g2.fillOval(
                    getWidth() - 390,
                    -170,
                    500,
                    500
            );


            g2.setColor(
                    new Color(
                            213,
                            239,
                            246,
                            150
                    )
            );

            g2.fillOval(
                    getWidth() - 285,
                    -105,
                    340,
                    340
            );


            // -------------------------------------------------
            // BOTTOM LEFT CIRCLE
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            205,
                            237,
                            244,
                            125
                    )
            );

            g2.fillOval(
                    -150,
                    getHeight() - 220,
                    370,
                    370
            );


            // -------------------------------------------------
            // TOP RIGHT DOTS
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            24,
                            165,
                            177,
                            55
                    )
            );


            for (
                    int row = 0;
                    row < 3;
                    row++
            ) {

                for (
                        int col = 0;
                        col < 8;
                        col++
                ) {

                    int x =
                            getWidth()
                                    - 210
                                    + col * 16;


                    int y =
                            18
                                    + row * 16;


                    g2.fillOval(
                            x,
                            y,
                            5,
                            5
                    );
                }
            }


            // -------------------------------------------------
            // BOTTOM LEFT DOTS
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            24,
                            165,
                            177,
                            38
                    )
            );


            for (
                    int row = 0;
                    row < 3;
                    row++
            ) {

                for (
                        int col = 0;
                        col < 7;
                        col++
                ) {

                    int x =
                            28
                                    + col * 16;


                    int y =
                            getHeight()
                                    - 65
                                    + row * 15;


                    g2.fillOval(
                            x,
                            y,
                            4,
                            4
                    );
                }
            }


            g2.dispose();
        }
    }


    // =========================================================
    // LETTER ICON
    // =========================================================

    private static class LetterIcon
            implements Icon {

        private final String text;

        private final Color color;


        public LetterIcon(
                String text,
                Color color
        ) {

            this.text =
                    text;

            this.color =
                    color;
        }


        @Override
        public int getIconWidth() {

            return 25;
        }


        @Override
        public int getIconHeight() {

            return 25;
        }


        @Override
        public void paintIcon(
                Component component,
                Graphics graphics,
                int x,
                int y
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            graphics.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(
                    color
            );


            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );


            FontMetrics fm =
                    g2.getFontMetrics();


            int textX =
                    x +
                            (
                                    getIconWidth()
                                            - fm.stringWidth(
                                            text
                                    )
                            ) / 2;


            int textY =
                    y +
                            (
                                    getIconHeight()
                                            - fm.getHeight()
                            ) / 2
                            +
                            fm.getAscent();


            g2.drawString(
                    text,
                    textX,
                    textY
            );


            g2.dispose();
        }
    }


    // =========================================================
    // MEGAPHONE ICON
    // =========================================================

    private static class MegaphoneIcon
            implements Icon {

        private final Color color;


        public MegaphoneIcon(
                Color color
        ) {

            this.color =
                    color;
        }


        @Override
        public int getIconWidth() {

            return 34;
        }


        @Override
        public int getIconHeight() {

            return 34;
        }


        @Override
        public void paintIcon(
                Component component,
                Graphics graphics,
                int x,
                int y
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            graphics.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(
                    color
            );


            g2.setStroke(
                    new BasicStroke(
                            2.5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            Polygon speaker =
                    new Polygon();


            speaker.addPoint(
                    x + 5,
                    y + 13
            );

            speaker.addPoint(
                    x + 14,
                    y + 13
            );

            speaker.addPoint(
                    x + 28,
                    y + 6
            );

            speaker.addPoint(
                    x + 28,
                    y + 28
            );

            speaker.addPoint(
                    x + 14,
                    y + 21
            );

            speaker.addPoint(
                    x + 5,
                    y + 21
            );


            g2.fill(
                    speaker
            );


            // Handle

            g2.drawLine(
                    x + 10,
                    y + 21,
                    x + 10,
                    y + 29
            );

            g2.drawLine(
                    x + 10,
                    y + 29,
                    x + 15,
                    y + 29
            );


            // Sound wave

            g2.drawArc(
                    x + 21,
                    y + 10,
                    13,
                    14,
                    -60,
                    120
            );


            g2.dispose();
        }
    }


    // =========================================================
    // MINI CALENDAR ICON
    // =========================================================

    private static class CalendarMiniIcon
            implements Icon {

        private final Color color;


        public CalendarMiniIcon(
                Color color
        ) {

            this.color =
                    color;
        }


        @Override
        public int getIconWidth() {

            return 25;
        }


        @Override
        public int getIconHeight() {

            return 25;
        }


        @Override
        public void paintIcon(
                Component component,
                Graphics graphics,
                int x,
                int y
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            graphics.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(
                    color
            );


            g2.setStroke(
                    new BasicStroke(
                            2f
                    )
            );


            g2.drawRoundRect(
                    x + 2,
                    y + 5,
                    20,
                    17,
                    3,
                    3
            );


            g2.drawLine(
                    x + 2,
                    y + 10,
                    x + 22,
                    y + 10
            );


            g2.drawLine(
                    x + 7,
                    y + 2,
                    x + 7,
                    y + 8
            );


            g2.drawLine(
                    x + 17,
                    y + 2,
                    x + 17,
                    y + 8
            );


            g2.fillRect(
                    x + 6,
                    y + 13,
                    3,
                    3
            );

            g2.fillRect(
                    x + 11,
                    y + 13,
                    3,
                    3
            );

            g2.fillRect(
                    x + 16,
                    y + 13,
                    3,
                    3
            );


            g2.dispose();
        }
    }


    // =========================================================
    // CALENDAR ICON
    // =========================================================

    private static class CalendarIcon
            implements Icon {

        private final Color color;


        public CalendarIcon(
                Color color
        ) {

            this.color =
                    color;
        }


        @Override
        public int getIconWidth() {

            return 23;
        }


        @Override
        public int getIconHeight() {

            return 23;
        }


        @Override
        public void paintIcon(
                Component component,
                Graphics graphics,
                int x,
                int y
        ) {

            Graphics2D g2 =
                    (Graphics2D)
                            graphics.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(
                    color
            );


            g2.setStroke(
                    new BasicStroke(
                            2f
                    )
            );


            g2.drawRoundRect(
                    x + 2,
                    y + 5,
                    19,
                    16,
                    3,
                    3
            );


            g2.drawLine(
                    x + 2,
                    y + 10,
                    x + 21,
                    y + 10
            );


            g2.drawLine(
                    x + 7,
                    y + 2,
                    x + 7,
                    y + 8
            );


            g2.drawLine(
                    x + 16,
                    y + 2,
                    x + 16,
                    y + 8
            );


            g2.fillRect(
                    x + 6,
                    y + 13,
                    3,
                    3
            );


            g2.fillRect(
                    x + 10,
                    y + 13,
                    3,
                    3
            );


            g2.fillRect(
                    x + 15,
                    y + 13,
                    3,
                    3
            );


            g2.dispose();
        }
    }


    // =========================================================
    // CALENDAR PANEL
    // =========================================================

    private static class CalendarPanel
            extends JPanel {

        private YearMonth currentMonth;

        private LocalDate selectedDate;

        private final Consumer<LocalDate>
                onDateSelected;

        private final JLabel selectedDateLabel;


        public CalendarPanel(
                LocalDate initialDate,
                Consumer<LocalDate> onDateSelected,
                JLabel selectedDateLabel
        ) {

            this.currentMonth =
                    YearMonth.from(
                            initialDate
                    );

            this.selectedDate =
                    initialDate;

            this.onDateSelected =
                    onDateSelected;

            this.selectedDateLabel =
                    selectedDateLabel;


            setLayout(
                    new BorderLayout(
                            0,
                            8
                    )
            );

            setBackground(
                    WHITE
            );


            createCalendar();
        }


        // =====================================================
        // CREATE CALENDAR
        // =====================================================

        private void createCalendar() {

            removeAll();


            // -------------------------------------------------
            // MONTH HEADER
            // -------------------------------------------------

            JPanel monthHeader =
                    new JPanel(
                            new BorderLayout(
                                    10,
                                    0
                            )
                    );

            monthHeader.setOpaque(false);


            JButton previous =
                    navigationButton(
                            "‹"
                    );


            JButton next =
                    navigationButton(
                            "›"
                    );


            String monthName =
                    currentMonth
                            .getMonth()
                            .getDisplayName(
                                    java.time.format.TextStyle.FULL,
                                    java.util.Locale.ENGLISH
                            );


            JLabel monthLabel =
                    new JLabel(
                            monthName +
                                    " " +
                                    currentMonth.getYear(),
                            SwingConstants.CENTER
                    );


            monthLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            17
                    )
            );

            monthLabel.setForeground(
                    NAVY
            );


            monthHeader.add(
                    previous,
                    BorderLayout.WEST
            );

            monthHeader.add(
                    monthLabel,
                    BorderLayout.CENTER
            );

            monthHeader.add(
                    next,
                    BorderLayout.EAST
            );


            previous.addActionListener(
                    e -> {

                        currentMonth =
                                currentMonth.minusMonths(
                                        1
                                );

                        createCalendar();
                    }
            );


            next.addActionListener(
                    e -> {

                        currentMonth =
                                currentMonth.plusMonths(
                                        1
                                );

                        createCalendar();
                    }
            );


            add(
                    monthHeader,
                    BorderLayout.NORTH
            );


            // -------------------------------------------------
            // CALENDAR GRID
            // -------------------------------------------------

            JPanel grid =
                    new JPanel(
                            new GridLayout(
                                    7,
                                    7,
                                    5,
                                    5
                            )
                    );

            grid.setOpaque(false);


            String[] weekDays = {
                    "SUN",
                    "MON",
                    "TUE",
                    "WED",
                    "THU",
                    "FRI",
                    "SAT"
            };


            // Weekday header

            for (
                    String day :
                    weekDays
            ) {

                JLabel label =
                        new JLabel(
                                day,
                                SwingConstants.CENTER
                        );


                label.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                10
                        )
                );


                label.setForeground(
                        MUTED
                );


                grid.add(
                        label
                );
            }


            // -------------------------------------------------
            // DATE CALCULATION
            // -------------------------------------------------

            LocalDate firstDay =
                    currentMonth.atDay(
                            1
                    );


            int firstColumn =
                    firstDay
                            .getDayOfWeek()
                            .getValue()
                            % 7;


            int daysInMonth =
                    currentMonth
                            .lengthOfMonth();


            // Empty cells before day 1

            for (
                    int i = 0;
                    i < firstColumn;
                    i++
            ) {

                JPanel empty =
                        new JPanel();

                empty.setOpaque(
                        false
                );

                grid.add(
                        empty
                );
            }


            // -------------------------------------------------
            // ALL DAYS
            // -------------------------------------------------

            for (
                    int day = 1;
                    day <= daysInMonth;
                    day++
            ) {

                LocalDate date =
                        currentMonth.atDay(
                                day
                        );


                JButton dayButton =
                        new JButton(
                                String.valueOf(
                                        day
                                )
                        );


                dayButton.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                12
                        )
                );


                dayButton.setFocusPainted(
                        false
                );

                dayButton.setBorderPainted(
                        false
                );

                dayButton.setCursor(
                        new Cursor(
                                Cursor.HAND_CURSOR
                        )
                );


                dayButton.setOpaque(
                        true
                );


                boolean selected =
                        date.equals(
                                selectedDate
                        );


                boolean today =
                        date.equals(
                                LocalDate.now()
                        );


                if (selected) {

                    dayButton.setBackground(
                            TEAL
                    );

                    dayButton.setForeground(
                            Color.WHITE
                    );

                } else if (today) {

                    dayButton.setBackground(
                            TODAY
                    );

                    dayButton.setForeground(
                            TEAL_DARK
                    );

                } else {

                    dayButton.setBackground(
                            WHITE
                    );

                    dayButton.setForeground(
                            TEXT
                    );
                }


                dayButton.addActionListener(
                        e -> {

                            selectedDate =
                                    date;


                            selectedDateLabel
                                    .setText(
                                            date.format(
                                                    DISPLAY_DATE_FORMAT
                                            )
                                    );


                            onDateSelected.accept(
                                    date
                            );
                        }
                );


                dayButton.addMouseListener(
                        new MouseAdapter() {

                            @Override
                            public void mouseEntered(
                                    MouseEvent e
                            ) {

                                if (
                                        !date.equals(
                                                selectedDate
                                        )
                                ) {

                                    dayButton.setBackground(
                                            new Color(
                                                    229,
                                                    244,
                                                    247
                                            )
                                    );
                                }
                            }


                            @Override
                            public void mouseExited(
                                    MouseEvent e
                            ) {

                                if (
                                        !date.equals(
                                                selectedDate
                                        )
                                ) {

                                    if (
                                            date.equals(
                                                    LocalDate.now()
                                            )
                                    ) {

                                        dayButton.setBackground(
                                                TODAY
                                        );

                                    } else {

                                        dayButton.setBackground(
                                                WHITE
                                        );
                                    }
                                }
                            }
                        }
                );


                grid.add(
                        dayButton
                );
            }


            // -------------------------------------------------
            // FILL REMAINING CELLS
            // -------------------------------------------------

            int totalCellsUsed =
                    7
                            + firstColumn
                            + daysInMonth;


            int remainingCells =
                    49
                            - totalCellsUsed;


            for (
                    int i = 0;
                    i < remainingCells;
                    i++
            ) {

                JPanel empty =
                        new JPanel();

                empty.setOpaque(
                        false
                );

                grid.add(
                        empty
                );
            }


            add(
                    grid,
                    BorderLayout.CENTER
            );


            revalidate();

            repaint();
        }


        // =====================================================
        // NAVIGATION BUTTON
        // =====================================================

        private JButton navigationButton(
                String text
        ) {

            JButton button =
                    new JButton(
                            text
                    );


            button.setPreferredSize(
                    new Dimension(
                            40,
                            34
                    )
            );


            button.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            22
                    )
            );


            button.setForeground(
                    NAVY
            );


            button.setBackground(
                    new Color(
                            238,
                            247,
                            250
                    )
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


            return button;
        }
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CampusConnect - Manage Announcements");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ManageAnnouncementsFrame());
            frame.setSize(1400, 900);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
