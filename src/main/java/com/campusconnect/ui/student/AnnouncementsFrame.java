package com.campusconnect.ui.student;

import com.campusconnect.dao.AnnouncementDAO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsFrame extends JFrame {

    private final AnnouncementDAO announcementDAO = new AnnouncementDAO();

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BACKGROUND =
            new Color(239, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color DARK_BLUE =
            new Color(20, 55, 95);

    private static final Color DARKER_BLUE =
            new Color(12, 43, 78);

    private static final Color TEAL =
            new Color(24, 158, 173);

    private static final Color TEAL_DARK =
            new Color(13, 125, 139);

    private static final Color LIGHT_TEAL =
            new Color(229, 247, 249);

    private static final Color TEXT_DARK =
            new Color(28, 43, 60);

    private static final Color TEXT_GRAY =
            new Color(95, 112, 130);

    private static final Color BORDER =
            new Color(211, 225, 234);

    private static final Color LIGHT_BLUE =
            new Color(242, 249, 253);

    // =====================================================
    // DATA
    // =====================================================

    private final List<Announcement> allAnnouncements =
            new ArrayList<>();

    // =====================================================
    // COMPONENTS
    // =====================================================

    private JPanel announcementsContainer;

    private JTextField searchField;

    private JLabel totalLabel;

    private JLabel showingLabel;

    private JLabel lastUpdatedLabel;

    private JButton refreshButton;

    // =====================================================
    // AUTO REFRESH
    // =====================================================

    private Timer autoRefreshTimer;

    private static final int AUTO_REFRESH_DELAY = 10000;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AnnouncementsFrame() {

        setTitle("CampusConnect - Announcements");

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(1000, 650)
        );

        setSize(
                1200,
                760
        );

        setLocationRelativeTo(null);

        initializeUI();

        loadAnnouncements(false);

        startAutoRefresh();

        setVisible(true);
    }

    // =====================================================
    // INITIALIZE UI
    // =====================================================

    private void initializeUI() {

        GradientBackgroundPanel mainPanel =
                new GradientBackgroundPanel();

        mainPanel.setLayout(
                new BorderLayout()
        );

        setContentPane(mainPanel);

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        mainPanel.add(
                createContent(),
                BorderLayout.CENTER
        );
    }

    // =====================================================
    // HEADER
    // =====================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        32,
                        15,
                        32
                )
        );

        // LEFT
        JPanel titlePanel = new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel brandLabel =
                new JLabel("CAMPUSCONNECT");

        brandLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        brandLabel.setForeground(TEAL_DARK);

        JLabel titleLabel =
                new JLabel("Announcements");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        31
                )
        );

        titleLabel.setForeground(DARK_BLUE);

        JLabel subtitleLabel =
                new JLabel(
                        "Stay updated with the latest campus news and important notices."
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(TEXT_GRAY);

        titlePanel.add(brandLabel);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(titleLabel);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitleLabel);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // RIGHT TOTAL CARD
        RoundedPanel totalCard =
                new RoundedPanel(
                        18,
                        new Color(
                                255,
                                255,
                                255,
                                240
                        )
                );

        totalCard.setLayout(
                new BoxLayout(
                        totalCard,
                        BoxLayout.Y_AXIS
                )
        );

        totalCard.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                18,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                20,
                                10,
                                20
                        )
                )
        );

        JLabel totalTitle =
                new JLabel("CAMPUS UPDATES");

        totalTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        totalTitle.setForeground(TEAL_DARK);

        totalLabel =
                new JLabel("0");

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        totalLabel.setForeground(DARK_BLUE);

        JLabel totalText =
                new JLabel("Announcements");

        totalText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        totalText.setForeground(TEXT_GRAY);

        totalCard.add(totalTitle);

        totalCard.add(
                Box.createVerticalStrut(2)
        );

        totalCard.add(totalLabel);

        totalCard.add(totalText);

        header.add(
                totalCard,
                BorderLayout.EAST
        );

        return header;
    }

    // =====================================================
    // CONTENT
    // =====================================================

    private JPanel createContent() {

        JPanel outer =
                new JPanel(
                        new BorderLayout()
                );

        outer.setOpaque(false);

        outer.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        32,
                        28,
                        32
                )
        );

        RoundedPanel card =
                new RoundedPanel(
                        22,
                        new Color(
                                255,
                                255,
                                255,
                                245
                        )
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        15
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                22,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                18,
                                20
                        )
                )
        );

        // =================================================
        // TOOLBAR
        // =================================================

        JPanel toolbar =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        toolbar.setOpaque(false);

        JPanel sectionPanel =
                new JPanel();

        sectionPanel.setOpaque(false);

        sectionPanel.setLayout(
                new BoxLayout(
                        sectionPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel sectionTitle =
                new JLabel("Latest Updates");

        sectionTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        sectionTitle.setForeground(TEXT_DARK);

        JLabel sectionSubtitle =
                new JLabel(
                        "Click any announcement to read the complete notice."
                );

        sectionSubtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        sectionSubtitle.setForeground(TEXT_GRAY);

        sectionPanel.add(sectionTitle);

        sectionPanel.add(
                Box.createVerticalStrut(4)
        );

        sectionPanel.add(sectionSubtitle);

        toolbar.add(
                sectionPanel,
                BorderLayout.WEST
        );

        // =================================================
        // SEARCH
        // =================================================

        JPanel searchPanel =
                new JPanel(
                        new BorderLayout(8, 0)
                );

        searchPanel.setOpaque(false);

        searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        260,
                        38
                )
        );

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        searchField.setForeground(TEXT_DARK);

        searchField.setBackground(WHITE);

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                10,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        refreshButton =
                createButton(
                        "↻  Refresh",
                        DARK_BLUE
                );

        refreshButton.addActionListener(
                e -> loadAnnouncements(true)
        );

        searchPanel.add(
                refreshButton,
                BorderLayout.EAST
        );

        toolbar.add(
                searchPanel,
                BorderLayout.EAST
        );

        card.add(
                toolbar,
                BorderLayout.NORTH
        );

        // =================================================
        // ANNOUNCEMENT CONTAINER
        // =================================================

        announcementsContainer =
                new JPanel();

        announcementsContainer.setLayout(
                new BoxLayout(
                        announcementsContainer,
                        BoxLayout.Y_AXIS
                )
        );

        announcementsContainer.setOpaque(false);

        JScrollPane scrollPane =
                new JScrollPane(
                        announcementsContainer
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setOpaque(false);

        scrollPane
                .getViewport()
                .setOpaque(false);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =================================================
        // FOOTER
        // =================================================

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setOpaque(false);

        showingLabel =
                new JLabel(
                        "Showing 0 announcements"
                );

        showingLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        showingLabel.setForeground(TEXT_GRAY);

        footer.add(
                showingLabel,
                BorderLayout.WEST
        );

        lastUpdatedLabel =
                new JLabel(
                        "Updated just now"
                );

        lastUpdatedLabel.setFont(
                new Font(
                        "Arial",
                        Font.ITALIC,
                        11
                )
        );

        lastUpdatedLabel.setForeground(TEXT_GRAY);

        footer.add(
                lastUpdatedLabel,
                BorderLayout.CENTER
        );

        JButton closeButton =
                createButton(
                        "Close",
                        DARK_BLUE
                );

        closeButton.addActionListener(
                e -> dispose()
        );

        footer.add(
                closeButton,
                BorderLayout.EAST
        );

        card.add(
                footer,
                BorderLayout.SOUTH
        );

        outer.add(
                card,
                BorderLayout.CENTER
        );

        // =================================================
        // SEARCH LISTENER
        // =================================================

        searchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {

                                applySearch();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {

                                applySearch();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {

                                applySearch();
                            }
                        }
                );

        return outer;
    }

    // =====================================================
    // LOAD ANNOUNCEMENTS
    // =====================================================

    private void loadAnnouncements(boolean showError) {
        try {
            List<com.campusconnect.model.Announcement> loaded =
                    announcementDAO.getAllAnnouncements();

            allAnnouncements.clear();

            for (com.campusconnect.model.Announcement item : loaded) {
                allAnnouncements.add(
                        new Announcement(
                                item.getAnnouncementId(),
                                item.getTitle(),
                                item.getMessage(),
                                item.getAnnouncementDate()
                        )
                );
            }

            totalLabel.setText(String.valueOf(allAnnouncements.size()));
            applySearch();
            lastUpdatedLabel.setText("Updated just now");

        } catch (RuntimeException ex) {
            if (showError) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load announcements.\n\n" + ex.getMessage(),
                        "Announcements Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // =====================================================
    // SEARCH
    // =====================================================

    private void applySearch() {

        if (announcementsContainer == null) {
            return;
        }

        String searchText =
                searchField == null
                        ? ""
                        : searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        announcementsContainer.removeAll();

        int count = 0;

        for (int i = 0;
             i < allAnnouncements.size();
             i++) {

            Announcement announcement =
                    allAnnouncements.get(i);

            String title =
                    safeLower(
                            announcement.title
                    );

            String message =
                    safeLower(
                            announcement.message
                    );

            String date =
                    safeLower(
                            announcement.date
                    );

            boolean matches =
                    searchText.isEmpty()
                            || title.contains(searchText)
                            || message.contains(searchText)
                            || date.contains(searchText);

            if (!matches) {
                continue;
            }

            boolean latest = i == 0;

            JPanel announcementCard =
                    createAnnouncementCard(
                            announcement,
                            latest
                    );

            announcementsContainer.add(
                    announcementCard
            );

            announcementsContainer.add(
                    Box.createVerticalStrut(12)
            );

            count++;
        }

        if (count == 0) {

            announcementsContainer.add(
                    createEmptyState()
            );
        }

        showingLabel.setText(
                "Showing "
                        + count
                        + " of "
                        + allAnnouncements.size()
                        + " announcements"
        );

        announcementsContainer.revalidate();

        announcementsContainer.repaint();
    }

    // =====================================================
    // ANNOUNCEMENT CARD
    // =====================================================

    private JPanel createAnnouncementCard(
            Announcement announcement,
            boolean latest
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        16,
                        0
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                18,
                                latest
                                        ? new Color(
                                        167,
                                        219,
                                        225
                                )
                                        : BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14,
                                14,
                                14,
                                14
                        )
                )
        );

        /*
         * IMPORTANT:
         * Keep the card width controlled by the
         * JScrollPane instead of allowing long labels
         * to make the card extremely wide.
         */
        card.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        132
                )
        );

        // =================================================
        // ICON
        // =================================================

        String iconText =
                getAnnouncementIcon(
                        announcement.title
                );

        Color iconColor =
                getAnnouncementColor(
                        announcement.title
                );

        RoundedPanel iconPanel =
                new RoundedPanel(
                        14,
                        iconColor
                );

        iconPanel.setLayout(
                new GridBagLayout()
        );

        iconPanel.setPreferredSize(
                new Dimension(
                        64,
                        82
                )
        );

        iconPanel.setMinimumSize(
                new Dimension(
                        64,
                        82
                )
        );

        iconPanel.setMaximumSize(
                new Dimension(
                        64,
                        82
                )
        );

        JLabel icon =
                new JLabel(
                        iconText
                );

        icon.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        27
                )
        );

        icon.setForeground(WHITE);

        iconPanel.add(icon);

        card.add(
                iconPanel,
                BorderLayout.WEST
        );

        // =================================================
        // CENTER TEXT PANEL
        // =================================================

        JPanel textPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                5
                        )
                );

        textPanel.setOpaque(false);

        /*
         * Title + badge
         */
        JPanel topLine =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        topLine.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        announcement.title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setForeground(DARKER_BLUE);

        /*
         * Prevent title from becoming wider than
         * available card area.
         */
        titleLabel.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        topLine.add(
                titleLabel,
                BorderLayout.CENTER
        );

        if (latest) {

            JLabel latestBadge =
                    new JLabel("NEW");

            latestBadge.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            9
                    )
            );

            latestBadge.setForeground(WHITE);

            latestBadge.setBackground(TEAL);

            latestBadge.setOpaque(true);

            latestBadge.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            latestBadge.setBorder(
                    BorderFactory.createEmptyBorder(
                            5,
                            9,
                            5,
                            9
                    )
            );

            topLine.add(
                    latestBadge,
                    BorderLayout.EAST
            );
        }

        textPanel.add(
                topLine,
                BorderLayout.NORTH
        );

        // =================================================
        // MESSAGE PREVIEW
        // =================================================

        JTextArea messageArea =
                new JTextArea(
                        createPreview(
                                announcement.message
                        )
                );

        messageArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        messageArea.setForeground(TEXT_GRAY);

        messageArea.setBackground(WHITE);

        messageArea.setLineWrap(true);

        messageArea.setWrapStyleWord(true);

        messageArea.setEditable(false);

        messageArea.setFocusable(false);

        messageArea.setBorder(
                BorderFactory.createEmptyBorder()
        );

        messageArea.setRows(2);

        /*
         * This fixes the problem visible in your
         * screenshot. Text will wrap inside the card
         * instead of pushing the content horizontally.
         */
        textPanel.add(
                messageArea,
                BorderLayout.CENTER
        );

        // =================================================
        // DATE
        // =================================================

        JLabel dateLabel =
                new JLabel(
                        "Published  •  "
                                + safeText(
                                announcement.date
                        )
                );

        dateLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        dateLabel.setForeground(TEAL_DARK);

        textPanel.add(
                dateLabel,
                BorderLayout.SOUTH
        );

        card.add(
                textPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // VIEW BUTTON
        // =================================================

        JButton viewButton =
                createSmallButton("View");

        viewButton.addActionListener(
                e ->
                        showAnnouncementDetails(
                                announcement
                        )
        );

        card.add(
                viewButton,
                BorderLayout.EAST
        );

        // =================================================
        // CARD CLICK
        // =================================================

        MouseAdapter cardClick =
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        showAnnouncementDetails(
                                announcement
                        );
                    }

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                LIGHT_BLUE
                        );

                        messageArea.setBackground(
                                LIGHT_BLUE
                        );

                        card.repaint();
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        card.setBackground(
                                WHITE
                        );

                        messageArea.setBackground(
                                WHITE
                        );

                        card.repaint();
                    }
                };

        card.addMouseListener(cardClick);

        iconPanel.addMouseListener(cardClick);

        textPanel.addMouseListener(cardClick);

        topLine.addMouseListener(cardClick);

        messageArea.addMouseListener(cardClick);

        dateLabel.addMouseListener(cardClick);

        return card;
    }

    // =====================================================
    // ICON BY ANNOUNCEMENT TYPE
    // =====================================================

    private String getAnnouncementIcon(
            String title
    ) {

        String value =
                safeLower(title);

        if (
                value.contains("event")
        ) {

            return "◫";
        }

        if (
                value.contains("holiday")
                        || value.contains("vacation")
        ) {

            return "☀";
        }

        if (
                value.contains("project")
                        || value.contains("submission")
        ) {

            return "▣";
        }

        if (
                value.contains("assignment")
                        || value.contains("deadline")
        ) {

            return "◷";
        }

        if (
                value.contains("exam")
                        || value.contains("test")
        ) {

            return "✎";
        }

        if (
                value.contains("notice")
                        || value.contains("announcement")
        ) {

            return "📢";
        }

        return "●";
    }

    // =====================================================
    // COLOR BY ANNOUNCEMENT TYPE
    // =====================================================

    private Color getAnnouncementColor(
            String title
    ) {

        String value =
                safeLower(title);

        if (
                value.contains("event")
        ) {

            return new Color(
                    24,
                    158,
                    173
            );
        }

        if (
                value.contains("holiday")
                        || value.contains("vacation")
        ) {

            return new Color(
                    231,
                    158,
                    67
            );
        }

        if (
                value.contains("project")
                        || value.contains("submission")
        ) {

            return new Color(
                    85,
                    104,
                    173
            );
        }

        if (
                value.contains("assignment")
                        || value.contains("deadline")
        ) {

            return new Color(
                    192,
                    95,
                    103
            );
        }

        if (
                value.contains("exam")
                        || value.contains("test")
        ) {

            return new Color(
                    112,
                    91,
                    160
            );
        }

        return DARK_BLUE;
    }

    // =====================================================
    // EMPTY STATE
    // =====================================================

    private JPanel createEmptyState() {

        RoundedPanel empty =
                new RoundedPanel(
                        18,
                        new Color(
                                249,
                                252,
                                254
                        )
                );

        empty.setLayout(
                new BoxLayout(
                        empty,
                        BoxLayout.Y_AXIS
                )
        );

        empty.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                18,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                50,
                                30,
                                50,
                                30
                        )
                )
        );

        JLabel icon =
                new JLabel("◉");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        35
                )
        );

        icon.setForeground(TEAL);

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "No announcements found"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT_DARK);

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel text =
                new JLabel(
                        "New campus updates will appear here automatically."
                );

        text.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        text.setForeground(TEXT_GRAY);

        text.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        empty.add(icon);

        empty.add(
                Box.createVerticalStrut(10)
        );

        empty.add(title);

        empty.add(
                Box.createVerticalStrut(5)
        );

        empty.add(text);

        empty.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        250
                )
        );

        return empty;
    }

    // =====================================================
    // DETAILS DIALOG
    // =====================================================

    private void showAnnouncementDetails(
            Announcement announcement
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Announcement Details",
                        true
                );

        dialog.setSize(
                650,
                470
        );

        dialog.setLocationRelativeTo(this);

        dialog.setLayout(
                new BorderLayout()
        );

        dialog.getContentPane()
                .setBackground(
                        BACKGROUND
                );

        RoundedPanel card =
                new RoundedPanel(
                        20,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        15
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                20,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                28,
                                20,
                                28
                        )
                )
        );

        // =================================================
        // HEADING
        // =================================================

        JPanel heading =
                new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel brand =
                new JLabel(
                        "CAMPUSCONNECT  •  ANNOUNCEMENT"
                );

        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        brand.setForeground(TEAL_DARK);

        JLabel title =
                new JLabel(
                        "<html>"
                                + escapeHtml(
                                announcement.title
                        )
                                + "</html>"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(DARK_BLUE);

        JLabel date =
                new JLabel(
                        "Published: "
                                + safeText(
                                announcement.date
                        )
                );

        date.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        date.setForeground(TEAL);

        heading.add(brand);

        heading.add(
                Box.createVerticalStrut(7)
        );

        heading.add(title);

        heading.add(
                Box.createVerticalStrut(8)
        );

        heading.add(date);

        card.add(
                heading,
                BorderLayout.NORTH
        );

        // =================================================
        // MESSAGE
        // =================================================

        JTextArea messageArea =
                new JTextArea(
                        safeText(
                                announcement.message
                        )
                );

        messageArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        messageArea.setForeground(TEXT_DARK);

        messageArea.setBackground(WHITE);

        messageArea.setLineWrap(true);

        messageArea.setWrapStyleWord(true);

        messageArea.setEditable(false);

        messageArea.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        0,
                        15,
                        0
                )
        );

        JScrollPane messageScroll =
                new JScrollPane(
                        messageArea
                );

        messageScroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        messageScroll
                .getViewport()
                .setBackground(WHITE);

        card.add(
                messageScroll,
                BorderLayout.CENTER
        );

        // =================================================
        // CLOSE
        // =================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottom.setOpaque(false);

        JButton close =
                createButton(
                        "Close",
                        DARK_BLUE
                );

        close.addActionListener(
                e -> dialog.dispose()
        );

        bottom.add(close);

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        dialog.add(
                card,
                BorderLayout.CENTER
        );

        dialog.setVisible(true);
    }

    // =====================================================
    // BUTTON
    // =====================================================

    private JButton createButton(
            String text,
            Color background
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(WHITE);

        button.setBackground(background);

        button.setFocusPainted(false);

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

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                background.darker()
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

    // =====================================================
    // SMALL BUTTON
    // =====================================================

    private JButton createSmallButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setForeground(TEAL_DARK);

        button.setBackground(LIGHT_TEAL);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                9,
                                new Color(
                                        173,
                                        221,
                                        226
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                13,
                                8,
                                13
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

    // =====================================================
    // AUTO REFRESH
    // =====================================================

    private void startAutoRefresh() {

        autoRefreshTimer =
                new Timer(
                        AUTO_REFRESH_DELAY,
                        e -> {

                            if (isDisplayable()) {

                                loadAnnouncements(false);
                            }
                        }
                );

        autoRefreshTimer.setRepeats(true);

        autoRefreshTimer.start();
    }

    // =====================================================
    // STOP TIMER
    // =====================================================

    @Override
    public void dispose() {

        if (autoRefreshTimer != null) {

            autoRefreshTimer.stop();
        }

        super.dispose();
    }

    // =====================================================
    // PREVIEW
    // =====================================================

    private String createPreview(
            String message
    ) {

        String text =
                safeText(message)
                        .replace("\n", " ")
                        .replace("\r", " ");

        if (text.length() > 145) {

            return text.substring(
                    0,
                    145
            ) + "...";
        }

        return text;
    }

    // =====================================================
    // SAFE TEXT
    // =====================================================

    private String safeText(
            String value
    ) {

        return value == null
                ? ""
                : value.trim();
    }

    // =====================================================
    // LOWER CASE
    // =====================================================

    private String safeLower(
            String value
    ) {

        return safeText(
                value
        ).toLowerCase();
    }

    // =====================================================
    // HTML ESCAPE
    // =====================================================

    private String escapeHtml(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // =====================================================
    // MODEL
    // =====================================================

    private static class Announcement {

        private final int id;

        private final String title;

        private final String message;

        private final String date;

        private Announcement(
                int id,
                String title,
                String message,
                String date
        ) {

            this.id = id;

            this.title = title;

            this.message = message;

            this.date = date;
        }
    }

    // =====================================================
    // BACKGROUND
    // =====================================================

    private static class GradientBackgroundPanel
            extends JPanel {

        public GradientBackgroundPanel() {

            setOpaque(true);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();

            int height = getHeight();

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    239,
                                    248,
                                    252
                            ),
                            0,
                            height,
                            new Color(
                                    221,
                                    241,
                                    248
                            )
                    );

            g2.setPaint(gradient);

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            // TOP RIGHT DECORATION
            g2.setColor(
                    new Color(
                            20,
                            165,
                            180,
                            20
                    )
            );

            g2.fillOval(
                    width - 260,
                    -150,
                    380,
                    380
            );

            // BOTTOM LEFT DECORATION
            g2.setColor(
                    new Color(
                            18,
                            65,
                            110,
                            14
                    )
            );

            g2.fillOval(
                    -180,
                    height - 220,
                    390,
                    390
            );

            g2.dispose();
        }
    }

    // =====================================================
    // ROUNDED PANEL
    // =====================================================

    private static class RoundedPanel
            extends JPanel {

        private final int radius;

        private Color backgroundColor;

        public RoundedPanel(
                int radius,
                Color backgroundColor
        ) {

            this.radius =
                    radius;

            this.backgroundColor =
                    backgroundColor;

            setOpaque(false);
        }

        @Override
        public void setBackground(
                Color color
        ) {

            this.backgroundColor =
                    color;

            super.setBackground(color);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    backgroundColor
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =====================================================
    // ROUNDED BORDER
    // =====================================================

    private static class RoundedBorder
            extends AbstractBorder {

        private final int radius;

        private final Color color;

        public RoundedBorder(
                int radius,
                Color color
        ) {

            this.radius = radius;

            this.color = color;
        }

        @Override
        public void paintBorder(
                Component component,
                Graphics graphics,
                int x,
                int y,
                int width,
                int height
        ) {

            Graphics2D g2 =
                    (Graphics2D) graphics.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.drawRoundRect(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            );

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(
                Component component
        ) {

            return new Insets(
                    1,
                    1,
                    1,
                    1
            );
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                AnnouncementsFrame::new
        );
    }
}
