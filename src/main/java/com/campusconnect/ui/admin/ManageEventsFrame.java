package com.campusconnect.ui.admin;

import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * CampusConnect
 * Admin - Manage Events
 *
 * Existing database table:
 * events
 *
 * Required columns:
 * event_id
 * event_name
 * event_date
 * venue
 * description
 */
public class ManageEventsFrame extends JPanel {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color PAGE_BG =
            new Color(232, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color NAVY =
            new Color(24, 67, 108);

    private static final Color NAVY_DARK =
            new Color(20, 55, 88);

    private static final Color TEAL =
            new Color(24, 168, 181);

    private static final Color TEAL_DARK =
            new Color(18, 143, 155);

    private static final Color BLUE =
            new Color(55, 105, 180);

    private static final Color GREEN =
            new Color(22, 160, 86);

    private static final Color RED =
            new Color(218, 55, 55);

    private static final Color GRAY =
            new Color(102, 120, 140);

    private static final Color LIGHT_BORDER =
            new Color(205, 222, 232);

    private static final Color TEXT =
            new Color(35, 55, 75);

    private static final Color MUTED =
            new Color(95, 118, 140);

    // =========================================================
    // FONTS
    // =========================================================

    private static final Font TITLE_FONT =
            new Font("Arial", Font.BOLD, 30);

    private static final Font SECTION_FONT =
            new Font("Arial", Font.BOLD, 21);

    private static final Font LABEL_FONT =
            new Font("Arial", Font.BOLD, 12);

    private static final Font NORMAL_FONT =
            new Font("Arial", Font.PLAIN, 13);

    private static final Font BUTTON_FONT =
            new Font("Arial", Font.BOLD, 13);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JTable eventsTable;

    private DefaultTableModel tableModel;

    private JTextField eventNameField;
    private JTextField eventDateField;
    private JTextField venueField;
    private JTextArea descriptionArea;
    private JTextField searchField;

    private JLabel totalEventsValue;
    private JLabel upcomingEventsValue;
    private JLabel latestEventValue;

    private final Runnable onClose;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ManageEventsFrame(Runnable onClose) {

        this.onClose = onClose;

        buildUI();

        loadEvents();
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(new BorderLayout());

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
                        14,
                        16,
                        20,
                        16
                )
        );

        JPanel statsPanel = createStatsPanel();
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        content.add(statsPanel);

        content.add(
                Box.createVerticalStrut(14)
        );

        JPanel eventsCard = createEventsListCard();
        eventsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        eventsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        content.add(eventsCard);

        content.add(
                Box.createVerticalStrut(14)
        );

        JPanel addEventCard = createAddEventCard();
        addEventCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        addEventCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        content.add(addEventCard);

        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane =
                new JScrollPane(content);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.getViewport().setBackground(PAGE_BG);
        scrollPane.setMinimumSize(new Dimension(0, 0));

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        root.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // This panel is displayed inside AdminDashboardFrame's CardLayout.
        // Use BorderLayout so the root fills the entire available card area
        // instead of being centered at its preferred size by FlowLayout.
        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(WHITE);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                LIGHT_BORDER
                        ),
                        new EmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

        // =====================================================
        // LEFT SIDE
        // =====================================================

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.X_AXIS
                )
        );

        left.setBackground(WHITE);

        // =====================================================
        // EVENT ICON BOX
        // =====================================================

        JPanel eventIconBox =
                createIconBox(
                        "EV",
                        TEAL,
                        58
                );

        left.add(eventIconBox);

        left.add(
                Box.createHorizontalStrut(12)
        );

        // =====================================================
        // TITLE AREA
        // =====================================================

        JPanel titleBox =
                new JPanel();

        titleBox.setLayout(
                new BoxLayout(
                        titleBox,
                        BoxLayout.Y_AXIS
                )
        );

        titleBox.setBackground(WHITE);

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
                        "Manage Events"
                );

        title.setFont(TITLE_FONT);

        title.setForeground(NAVY);

        JLabel subtitle =
                new JLabel(
                        "Create, manage and organize campus events"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(MUTED);

        titleBox.add(brand);

        titleBox.add(
                Box.createVerticalStrut(2)
        );

        titleBox.add(title);

        titleBox.add(
                Box.createVerticalStrut(3)
        );

        titleBox.add(subtitle);

        left.add(titleBox);

        header.add(
                left,
                BorderLayout.WEST
        );

        // =====================================================
        // RIGHT SIDE
        // =====================================================

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        right.setBackground(WHITE);

        JPanel campusIcon =
                createIconBox(
                        "CC",
                        NAVY,
                        58
                );

        JLabel campus =
                new JLabel(
                        "<html><b style='font-size:15px'>CampusConnect</b>"
                                + "<br><font color='#60758A'>Admin Portal</font></html>"
                );

        right.add(campusIcon);

        right.add(campus);

        header.add(
                right,
                BorderLayout.EAST
        );

        // Keep header contents from forcing an oversized preferred width.
        header.setMinimumSize(new Dimension(0, 90));
        header.setPreferredSize(new Dimension(0, 112));

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

        JPanel box =
                new JPanel(
                        new GridBagLayout()
                );

        box.setBackground(background);

        box.setPreferredSize(
                new Dimension(
                        size,
                        size
                )
        );

        box.setMinimumSize(
                new Dimension(
                        size,
                        size
                )
        );

        box.setMaximumSize(
                new Dimension(
                        size,
                        size
                )
        );

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setForeground(WHITE);

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        size >= 65 ? 28 : 21
                )
        );

        box.add(iconLabel);

        return box;
    }

    // =========================================================
    // STATS PANEL
    // =========================================================

    private JPanel createStatsPanel() {

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                14,
                                0
                        )
                );

        stats.setOpaque(false);

        totalEventsValue =
                createStatValue();

        upcomingEventsValue =
                createStatValue();

        latestEventValue =
                createStatValue();

        stats.add(
                createStatCard(
                        "EV",
                        "TOTAL EVENTS",
                        totalEventsValue,
                        TEAL
                )
        );

        stats.add(
                createStatCard(
                        "UP",
                        "UPCOMING EVENTS",
                        upcomingEventsValue,
                        BLUE
                )
        );

        stats.add(
                createStatCard(
                        "NEW",
                        "LATEST EVENT",
                        latestEventValue,
                        NAVY
                )
        );

        return stats;
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
                new JPanel(new BorderLayout());

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                LIGHT_BORDER
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
                        Math.min(
                                255,
                                accent.getRed() + 205
                        ),
                        Math.min(
                                255,
                                accent.getGreen() + 205
                        ),
                        Math.min(
                                255,
                                accent.getBlue() + 205
                        )
                )
        );

        iconLabel.setForeground(accent);

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        iconLabel.setHorizontalAlignment(
                SwingConstants.CENTER
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
                        2,
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
    // EVENTS LIST CARD
    // =========================================================

    private JPanel createEventsListCard() {

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
                                LIGHT_BORDER
                        ),
                        new EmptyBorder(
                                14,
                                12,
                                12,
                                12
                        )
                )
        );

        // =====================================================
        // TOP BAR
        // =====================================================

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

        JLabel headingIcon =
                new JLabel("EV");

        headingIcon.setForeground(TEAL);

        headingIcon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        heading.add(headingIcon);

        heading.add(
                Box.createHorizontalStrut(9)
        );

        JPanel headingText =
                new JPanel();

        headingText.setLayout(
                new BoxLayout(
                        headingText,
                        BoxLayout.Y_AXIS
                )
        );

        headingText.setBackground(WHITE);

        JLabel title =
                new JLabel(
                        "Current Events"
                );

        title.setFont(SECTION_FONT);

        title.setForeground(NAVY);

        JLabel subtitle =
                new JLabel(
                        "Select an event to view or manage it"
                );

        subtitle.setFont(NORMAL_FONT);

        subtitle.setForeground(MUTED);

        headingText.add(title);

        headingText.add(
                Box.createVerticalStrut(2)
        );

        headingText.add(subtitle);

        heading.add(headingText);

        top.add(
                heading,
                BorderLayout.WEST
        );

        // =====================================================
        // SEARCH AREA
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
                new JLabel("Search:");

        searchIcon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        searchIcon.setForeground(NAVY);

        searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        150,
                        34
                )
        );

        searchField.setFont(NORMAL_FONT);

        searchField.setToolTipText(
                "Search events..."
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                LIGHT_BORDER
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
                        "Refresh",
                        BLUE,
                        WHITE,
                        95,
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
                "EVENT",
                "DATE",
                "VENUE",
                "DESCRIPTION"
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

        eventsTable =
                new JTable(tableModel);

        eventsTable.setFont(NORMAL_FONT);

        eventsTable.setForeground(TEXT);

        eventsTable.setBackground(WHITE);

        eventsTable.setRowHeight(40);

        eventsTable.setShowGrid(true);

        eventsTable.setGridColor(
                new Color(
                        220,
                        231,
                        238
                )
        );

        eventsTable.setSelectionBackground(
                new Color(
                        222,
                        241,
                        247
                )
        );

        eventsTable.setSelectionForeground(
                NAVY
        );

        eventsTable.setAutoCreateRowSorter(true);

        eventsTable.setFillsViewportHeight(true);

        // =====================================================
        // TABLE HEADER
        // =====================================================

        eventsTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        eventsTable.getTableHeader()
                .setForeground(WHITE);

        eventsTable.getTableHeader()
                .setBackground(NAVY);

        eventsTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                42
                        )
                );

        eventsTable.getTableHeader()
                .setReorderingAllowed(false);

        // =====================================================
        // COLUMN WIDTHS
        // =====================================================

        eventsTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        eventsTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(190);

        eventsTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(120);

        eventsTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        eventsTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(390);

        // =====================================================
        // CENTER RENDERER
        // =====================================================

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        eventsTable.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centerRenderer);

        eventsTable.getColumnModel()
                .getColumn(2)
                .setCellRenderer(centerRenderer);

        JScrollPane tableScroll =
                new JScrollPane(eventsTable);

        tableScroll.setBorder(
                BorderFactory.createLineBorder(
                        LIGHT_BORDER
                )
        );

        tableScroll.setPreferredSize(
                new Dimension(
                        0,
                        300
                )
        );
        tableScroll.setMinimumSize(new Dimension(0, 180));
        eventsTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

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

        JButton clearSelection =
                createButton(
                        "Clear Selection",
                        GRAY,
                        WHITE,
                        155,
                        38
                );

        JButton deleteButton =
                createButton(
                        "Delete Selected",
                        RED,
                        WHITE,
                        165,
                        38
                );

        bottom.add(clearSelection);

        bottom.add(deleteButton);

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        // =====================================================
        // ACTIONS
        // =====================================================

        refreshButton.addActionListener(
                e -> loadEvents()
        );

        clearSelection.addActionListener(
                e -> {
                    eventsTable.clearSelection();
                    clearForm();
                }
        );

        deleteButton.addActionListener(
                e -> deleteEvent()
        );

        searchField.getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            private void search() {

                                filterEvents(
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

        eventsTable.getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (!e.getValueIsAdjusting()) {

                                int row =
                                        eventsTable
                                                .getSelectedRow();

                                if (row >= 0) {

                                    int modelRow =
                                            eventsTable
                                                    .convertRowIndexToModel(
                                                            row
                                                    );

                                    loadSelectedEvent(
                                            modelRow
                                    );
                                }
                            }
                        }
                );

        return card;
    }

    // =========================================================
    // ADD EVENT CARD
    // =========================================================

    private JPanel createAddEventCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                LIGHT_BORDER
                        ),
                        new EmptyBorder(
                                14,
                                12,
                                14,
                                12
                        )
                )
        );

        // =====================================================
        // HEADER
        // =====================================================

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
                new JLabel("+");

        icon.setForeground(TEAL);

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        heading.add(icon);

        heading.add(
                Box.createHorizontalStrut(8)
        );

        JPanel headingText =
                new JPanel();

        headingText.setLayout(
                new BoxLayout(
                        headingText,
                        BoxLayout.Y_AXIS
                )
        );

        headingText.setBackground(WHITE);

        JLabel title =
                new JLabel(
                        "Add New Event"
                );

        title.setFont(SECTION_FONT);

        title.setForeground(NAVY);

        JLabel subtitle =
                new JLabel(
                        "Publish an important event for CampusConnect students"
                );

        subtitle.setFont(NORMAL_FONT);

        subtitle.setForeground(MUTED);

        headingText.add(title);

        headingText.add(
                Box.createVerticalStrut(2)
        );

        headingText.add(subtitle);

        heading.add(headingText);

        header.add(
                heading,
                BorderLayout.WEST
        );

        JLabel badge =
                new JLabel(
                        "NEW EVENT"
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
        // EVENT NAME
        // =====================================================

        JLabel nameLabel =
                createFieldLabel(
                        "EVENT NAME"
                );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        form.add(
                nameLabel,
                gbc
        );

        eventNameField =
                createTextField();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        form.add(
                eventNameField,
                gbc
        );

        // =====================================================
        // DATE
        // =====================================================

        JLabel dateLabel =
                createFieldLabel(
                        "DATE"
                );

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;

        form.add(
                dateLabel,
                gbc
        );

        JPanel datePanel =
                new JPanel(
                        new BorderLayout(
                                5,
                                0
                        )
                );

        datePanel.setBackground(WHITE);

        eventDateField =
                createTextField();

        JButton todayButton =
                createButton(
                        "Today",
                        TEAL,
                        WHITE,
                        76,
                        34
                );

        datePanel.add(
                eventDateField,
                BorderLayout.CENTER
        );

        datePanel.add(
                todayButton,
                BorderLayout.EAST
        );

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.8;

        form.add(
                datePanel,
                gbc
        );

        todayButton.addActionListener(
                e -> setToday()
        );

        // =====================================================
        // VENUE
        // =====================================================

        JLabel venueLabel =
                createFieldLabel(
                        "VENUE"
                );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        form.add(
                venueLabel,
                gbc
        );

        venueField =
                createTextField();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        form.add(
                venueField,
                gbc
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel descriptionLabel =
                createFieldLabel(
                        "DESCRIPTION"
                );

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;

        form.add(
                descriptionLabel,
                gbc
        );

        descriptionArea =
                new JTextArea(
                        3,
                        20
                );

        descriptionArea.setFont(NORMAL_FONT);

        descriptionArea.setLineWrap(true);

        descriptionArea.setWrapStyleWord(true);

        descriptionArea.setForeground(TEXT);

        descriptionArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                LIGHT_BORDER
                        ),
                        new EmptyBorder(
                                7,
                                8,
                                7,
                                8
                        )
                )
        );

        JScrollPane descriptionScroll =
                new JScrollPane(
                        descriptionArea
                );

        descriptionScroll.setBorder(
                BorderFactory.createLineBorder(
                        LIGHT_BORDER
                )
        );

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.8;

        form.add(
                descriptionScroll,
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
                        "Clear",
                        GRAY,
                        WHITE,
                        100,
                        38
                );

        JButton addButton =
                createButton(
                        "Add Event",
                        TEAL,
                        WHITE,
                        135,
                        38
                );

        JButton closeButton =
                createButton(
                        "Close",
                        NAVY,
                        WHITE,
                        100,
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
                e -> addEvent()
        );

        closeButton.addActionListener(
                e -> {
                    if (onClose != null) {
                        onClose.run();
                    }
                }
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

        label.setFont(LABEL_FONT);

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

        field.setFont(NORMAL_FONT);

        field.setForeground(TEXT);

        field.setBackground(WHITE);

        field.setPreferredSize(
                new Dimension(
                        120,
                        34
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                LIGHT_BORDER
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

        button.setFont(BUTTON_FONT);

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

        button.setMinimumSize(
                new Dimension(width, height)
        );

        return button;
    }

    // =========================================================
    // LOAD EVENTS
    // =========================================================

    private void loadEvents() {

        tableModel.setRowCount(0);

        String query =
                "SELECT event_id, event_name, event_date, "
                        + "venue, description "
                        + "FROM events "
                        + "ORDER BY event_date ASC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                query
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                tableModel.addRow(
                        new Object[]{
                                resultSet.getInt(
                                        "event_id"
                                ),

                                resultSet.getString(
                                        "event_name"
                                ),

                                resultSet.getDate(
                                        "event_date"
                                ),

                                resultSet.getString(
                                        "venue"
                                ),

                                resultSet.getString(
                                        "description"
                                )
                        }
                );
            }

            updateStatistics();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading events:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // UPDATE STATISTICS
    // =========================================================

    private void updateStatistics() {

        int total =
                tableModel.getRowCount();

        int upcoming = 0;

        String latest = "-";

        long today =
                System.currentTimeMillis();

        long latestTime =
                Long.MAX_VALUE;

        for (
                int i = 0;
                i < total;
                i++
        ) {

            Object dateObject =
                    tableModel.getValueAt(
                            i,
                            2
                    );

            if (
                    dateObject
                            instanceof Date
            ) {

                Date date =
                        (Date) dateObject;

                if (
                        date.getTime()
                                >= today
                ) {

                    upcoming++;
                }

                if (
                        date.getTime()
                                >= today
                                &&
                                date.getTime()
                                        < latestTime
                ) {

                    latestTime =
                            date.getTime();

                    Object name =
                            tableModel.getValueAt(
                                    i,
                                    1
                            );

                    if (name != null) {

                        latest =
                                name.toString();
                    }
                }
            }
        }

        totalEventsValue.setText(
                String.valueOf(total)
        );

        upcomingEventsValue.setText(
                String.valueOf(upcoming)
        );

        latestEventValue.setText(
                latest
        );
    }

    // =========================================================
    // FILTER EVENTS
    // =========================================================

    private void filterEvents(
            String text
    ) {

        if (text.isEmpty()) {

            loadEvents();

            return;
        }

        tableModel.setRowCount(0);

        String query =
                "SELECT event_id, event_name, event_date, "
                        + "venue, description "
                        + "FROM events "
                        + "WHERE LOWER(event_name) LIKE ? "
                        + "OR LOWER(venue) LIKE ? "
                        + "OR LOWER(description) LIKE ? "
                        + "ORDER BY event_date ASC";

        String search =
                "%"
                        + text.toLowerCase()
                        + "%";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                query
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

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    tableModel.addRow(
                            new Object[]{
                                    resultSet.getInt(
                                            "event_id"
                                    ),

                                    resultSet.getString(
                                            "event_name"
                                    ),

                                    resultSet.getDate(
                                            "event_date"
                                    ),

                                    resultSet.getString(
                                            "venue"
                                    ),

                                    resultSet.getString(
                                            "description"
                                    )
                            }
                    );
                }
            }

            updateStatistics();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error searching events:\n"
                            + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // LOAD SELECTED EVENT
    // =========================================================

    private void loadSelectedEvent(
            int modelRow
    ) {

        Object name =
                tableModel.getValueAt(
                        modelRow,
                        1
                );

        Object date =
                tableModel.getValueAt(
                        modelRow,
                        2
                );

        Object venue =
                tableModel.getValueAt(
                        modelRow,
                        3
                );

        Object description =
                tableModel.getValueAt(
                        modelRow,
                        4
                );

        eventNameField.setText(
                name == null
                        ? ""
                        : name.toString()
        );

        eventDateField.setText(
                date == null
                        ? ""
                        : date.toString()
        );

        venueField.setText(
                venue == null
                        ? ""
                        : venue.toString()
        );

        descriptionArea.setText(
                description == null
                        ? ""
                        : description.toString()
        );
    }

    // =========================================================
    // TODAY
    // =========================================================

    private void setToday() {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "yyyy-MM-dd"
                );

        eventDateField.setText(
                format.format(
                        new java.util.Date()
                )
        );
    }

    // =========================================================
    // ADD EVENT
    // =========================================================

    private void addEvent() {

        String eventName =
                eventNameField
                        .getText()
                        .trim();

        String eventDate =
                eventDateField
                        .getText()
                        .trim();

        String venue =
                venueField
                        .getText()
                        .trim();

        String description =
                descriptionArea
                        .getText()
                        .trim();

        if (
                eventName.isEmpty()
                        || eventDate.isEmpty()
                        || venue.isEmpty()
                        || description.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all event fields.",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Date sqlDate;

        try {

            sqlDate =
                    Date.valueOf(
                            eventDate
                    );

        } catch (
                IllegalArgumentException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format.\n\n"
                            + "Use:\n"
                            + "YYYY-MM-DD\n\n"
                            + "Example:\n"
                            + "2026-10-15",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String query =
                "INSERT INTO events "
                        + "(event_name, event_date, venue, description) "
                        + "VALUES (?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                query
                        )
        ) {

            statement.setString(
                    1,
                    eventName
            );

            statement.setDate(
                    2,
                    sqlDate
            );

            statement.setString(
                    3,
                    venue
            );

            statement.setString(
                    4,
                    description
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Event added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();

            loadEvents();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error adding event:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // DELETE EVENT
    // =========================================================

    private void deleteEvent() {

        int selectedRow =
                eventsTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event to delete.",
                    "No Event Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                eventsTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        int eventId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        String eventName =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                1
                        )
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete this event?\n\n"
                                + eventName,
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

        String query =
                "DELETE FROM events "
                        + "WHERE event_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                query
                        )
        ) {

            statement.setInt(
                    1,
                    eventId
            );

            int deleted =
                    statement.executeUpdate();

            if (deleted > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Event deleted successfully!",
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();

                loadEvents();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Event could not be found.",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error deleting event:\n"
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

        eventNameField.setText("");

        eventDateField.setText("");

        venueField.setText("");

        descriptionArea.setText("");

        if (eventsTable != null) {

            eventsTable.clearSelection();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("CampusConnect - Manage Events");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1100, 700));
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            ManageEventsFrame eventsPanel = new ManageEventsFrame(frame::dispose);
            frame.setContentPane(eventsPanel);
            frame.setVisible(true);
        });
    }
}
