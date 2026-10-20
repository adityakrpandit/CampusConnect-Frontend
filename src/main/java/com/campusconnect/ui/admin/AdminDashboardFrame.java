package com.campusconnect.ui.admin;

import com.campusconnect.*;
import com.campusconnect.ui.login.LoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminDashboardFrame extends JFrame {

    private String adminName;
    private CardLayout pageLayout;
    private JPanel pageContainer;

    private static final String DASHBOARD_PAGE = "DASHBOARD_PAGE";
    private static final String STUDENTS_PAGE = "STUDENTS_PAGE";
    private static final String ANNOUNCEMENTS_PAGE = "ANNOUNCEMENTS_PAGE";
    private static final String STUDY_MATERIALS_PAGE = "STUDY_MATERIALS_PAGE";
    private static final String EVENTS_PAGE = "EVENTS_PAGE";
    private static final String DOUBTS_PAGE = "DOUBTS_PAGE";
    private static final String FACULTY_PAGE = "FACULTY_PAGE";

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BACKGROUND =
            new Color(243, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color NAVY =
            new Color(19, 57, 96);

    private static final Color NAVY_DARK =
            new Color(13, 43, 73);

    private static final Color TEAL =
            new Color(21, 157, 171);

    private static final Color BLUE =
            new Color(55, 105, 180);

    private static final Color GREEN =
            new Color(40, 155, 91);

    private static final Color ORANGE =
            new Color(226, 139, 35);

    private static final Color PURPLE =
            new Color(113, 82, 173);

    private static final Color RED =
            new Color(207, 73, 73);

    private static final Color TEXT_DARK =
            new Color(28, 42, 58);

    private static final Color TEXT_GRAY =
            new Color(105, 119, 137);

    private static final Color BORDER =
            new Color(220, 228, 236);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminDashboardFrame(String adminName) {

        this.adminName =
                adminName == null ||
                        adminName.trim().isEmpty()
                        ? "Admin"
                        : adminName;

        // =====================================================
        // FRAME SETTINGS
        // =====================================================

        setTitle(
                "CampusConnect - Admin Dashboard"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(
                        1100,
                        700
                )
        );

        // =====================================================
        // MAIN CONTAINER
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        // =====================================================
        // SIDEBAR
        // =====================================================

        mainPanel.add(
                createSidebar(),
                BorderLayout.WEST
        );

        // =====================================================
        // CONTENT
        // =====================================================

        // The sidebar stays fixed; only the right content area changes.

        pageLayout = new CardLayout();
        pageContainer = new JPanel(pageLayout);

        pageContainer.add(createMainContent(), DASHBOARD_PAGE);

        pageContainer.add(
                new StudentsFrame(this::showDashboard),
                STUDENTS_PAGE
        );

        pageContainer.add(
                new ManageStudyMaterialsFrame(this::showDashboard),
                STUDY_MATERIALS_PAGE
        );

        pageContainer.add(
                new ManageEventsFrame(this::showDashboard),
                EVENTS_PAGE
        );

        pageContainer.add(
                new ManageAnnouncementsFrame(this::showDashboard),
                ANNOUNCEMENTS_PAGE
        );

        pageContainer.add(
                new ManageDoubtsFrame(this::showDashboard),
                DOUBTS_PAGE
        );

        pageContainer.add(
                new ManageFacultyFrame(this::showDashboard),
                FACULTY_PAGE
        );

        mainPanel.add(
                pageContainer,
                BorderLayout.CENTER
        );

        // =====================================================
        // FRAME
        // =====================================================

        add(mainPanel);

        setSize(
                1200,
                750
        );

        setLocationRelativeTo(null);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setVisible(true);
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(
                        255,
                        0
                )
        );

        sidebar.setBackground(
                NAVY_DARK
        );


        // -----------------------------------------------------
        // BRAND - CAMPUSCONNECT LOGO
        // -----------------------------------------------------

        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(
                new BoxLayout(brandPanel, BoxLayout.Y_AXIS)
        );

        brandPanel.setBorder(
                new EmptyBorder(25, 15, 20, 15)
        );

        // Logo
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setVerticalAlignment(SwingConstants.CENTER);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        logo.setPreferredSize(new Dimension(180, 125));
        logo.setMinimumSize(new Dimension(180, 125));
        logo.setMaximumSize(new Dimension(180, 125));

        java.net.URL logoUrl =
                getClass().getResource("/campusconnect_logo.png");

        if (logoUrl != null) {
            ImageIcon originalIcon = new ImageIcon(logoUrl);

            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            if (originalWidth > 0 && originalHeight > 0) {
                int maxWidth = 180;
                int maxHeight = 125;

                double scale = Math.min(
                        (double) maxWidth / originalWidth,
                        (double) maxHeight / originalHeight
                );

                int scaledWidth = Math.max(
                        1, (int) (originalWidth * scale)
                );

                int scaledHeight = Math.max(
                        1, (int) (originalHeight * scale)
                );

                Image scaledImage =
                        originalIcon.getImage().getScaledInstance(
                                scaledWidth,
                                scaledHeight,
                                Image.SCALE_SMOOTH
                        );

                logo.setIcon(new ImageIcon(scaledImage));
            }
        } else {
            logo.setText("CC");
            logo.setFont(new Font("Arial", Font.BOLD, 28));
            logo.setForeground(WHITE);
            logo.setOpaque(true);
            logo.setBackground(TEAL);
        }

        // CampusConnect brand name
        JLabel brandText = new JLabel("CampusConnect");
        brandText.setFont(
                new Font("Arial", Font.BOLD, 21)
        );
        brandText.setForeground(WHITE);
        brandText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Admin portal subtitle
        JLabel adminPortal = new JLabel("Admin Portal");
        adminPortal.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );
        adminPortal.setForeground(
                new Color(160, 205, 215)
        );
        adminPortal.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Arrange logo and text vertically
        brandPanel.add(logo);
        brandPanel.add(Box.createVerticalStrut(8));
        brandPanel.add(brandText);
        brandPanel.add(Box.createVerticalStrut(5));
        brandPanel.add(adminPortal);

        sidebar.add(brandPanel, BorderLayout.NORTH);


        // -----------------------------------------------------
        // MENU
        // -----------------------------------------------------

        JPanel menuPanel =
                new JPanel();

        menuPanel.setOpaque(false);

        menuPanel.setLayout(
                new BoxLayout(
                        menuPanel,
                        BoxLayout.Y_AXIS
                )
        );

        menuPanel.setBorder(
                new EmptyBorder(
                        10,
                        14,
                        10,
                        14
                )
        );

        JLabel menuTitle =
                new JLabel(
                        "MANAGEMENT"
                );

        menuTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        menuTitle.setForeground(
                new Color(
                        135,
                        170,
                        185
                )
        );

        menuTitle.setBorder(
                new EmptyBorder(
                        0,
                        12,
                        12,
                        0
                )
        );

        menuPanel.add(
                menuTitle
        );

        // Sidebar items

        menuPanel.add(
                createSidebarItem(
                        "⌂",
                        "Dashboard",
                        true,
                        e -> showDashboard()
                )
        );

        menuPanel.add(
                createSidebarItem(
                        "S",
                        "Students",
                        false,
                        e -> showStudents()
                )
        );

        menuPanel.add(
                createSidebarItem(
                        "A",
                        "Announcements",
                        false,
                        e -> showAnnouncements()
                )
        );

        menuPanel.add(
                createSidebarItem(
                        "M",
                        "Study Materials",
                        false,
                        e -> showStudyMaterials()
                )
        );

        menuPanel.add(
                createSidebarItem(
                        "E",
                        "Events",
                        false,
                        e -> showEvents()
                )
        );

        menuPanel.add(
                createSidebarItem(
                        "D",
                        "Doubts",
                        false,
                        e -> showDoubts()
                )
        );


        menuPanel.add(
                createSidebarItem(
                        "F",
                        "Manage Faculty",
                        false,
                        e -> showFaculty()
                )
        );


        sidebar.add(
                menuPanel,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // ADMIN PROFILE + LOGOUT
        // -----------------------------------------------------

        JPanel bottom =
                new JPanel();

        bottom.setOpaque(false);

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBorder(
                new EmptyBorder(
                        15,
                        18,
                        25,
                        18
                )
        );

        JSeparator separator =
                new JSeparator();

        separator.setForeground(
                new Color(
                        65,
                        95,
                        120
                )
        );

        bottom.add(
                separator
        );

        bottom.add(
                Box.createVerticalStrut(15)
        );

        JPanel profile =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        profile.setOpaque(false);

        JLabel avatar =
                new JLabel(
                        getInitial()
                );

        avatar.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        avatar.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
        );

        avatar.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        avatar.setForeground(
                WHITE
        );

        avatar.setOpaque(true);

        avatar.setBackground(
                TEAL
        );

        JPanel profileText =
                new JPanel();

        profileText.setOpaque(false);

        profileText.setLayout(
                new BoxLayout(
                        profileText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel name =
                new JLabel(
                        adminName
                );

        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        name.setForeground(
                WHITE
        );

        JLabel role =
                new JLabel(
                        "Administrator"
                );

        role.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        role.setForeground(
                new Color(
                        165,
                        190,
                        205
                )
        );

        profileText.add(name);

        profileText.add(
                Box.createVerticalStrut(3)
        );

        profileText.add(role);

        profile.add(
                avatar,
                BorderLayout.WEST
        );

        profile.add(
                profileText,
                BorderLayout.CENTER
        );

        bottom.add(
                profile
        );

        bottom.add(
                Box.createVerticalStrut(15)
        );

        JButton logoutButton =
                createLogoutButton();

        bottom.add(
                logoutButton
        );

        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );

        return sidebar;
    }

    // =========================================================
    // SIDEBAR ITEM
    // =========================================================

    private JPanel createSidebarItem(
            String icon,
            String text,
            boolean active,
            java.awt.event.ActionListener action
    ) {

        JPanel item =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        item.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        item.setPreferredSize(
                new Dimension(
                        220,
                        48
                )
        );

        item.setBorder(
                new EmptyBorder(
                        8,
                        12,
                        8,
                        12
                )
        );

        item.setBackground(
                active
                        ? new Color(
                        34,
                        91,
                        125
                )
                        : NAVY_DARK
        );

        JLabel iconLabel =
                new JLabel(
                        icon,
                        SwingConstants.CENTER
                );

        iconLabel.setPreferredSize(
                new Dimension(
                        30,
                        30
                )
        );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        iconLabel.setForeground(
                active
                        ? WHITE
                        : new Color(
                        160,
                        190,
                        205
                )
        );

        JLabel textLabel =
                new JLabel(
                        text
                );

        textLabel.setFont(
                new Font(
                        "Arial",
                        active
                                ? Font.BOLD
                                : Font.PLAIN,
                        13
                )
        );

        textLabel.setForeground(
                WHITE
        );

        item.add(
                iconLabel,
                BorderLayout.WEST
        );

        item.add(
                textLabel,
                BorderLayout.CENTER
        );

        if (action != null) {

            item.setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            item.addMouseListener(
                    new java.awt.event.MouseAdapter() {

                        @Override
                        public void mouseClicked(
                                java.awt.event.MouseEvent e
                        ) {

                            action.actionPerformed(
                                    new java.awt.event.ActionEvent(
                                            item,
                                            java.awt.event.ActionEvent.ACTION_PERFORMED,
                                            text
                                    )
                            );
                        }

                        @Override
                        public void mouseEntered(
                                java.awt.event.MouseEvent e
                        ) {

                            item.setBackground(
                                    new Color(
                                            28,
                                            73,
                                            105
                                    )
                            );
                        }

                        @Override
                        public void mouseExited(
                                java.awt.event.MouseEvent e
                        ) {

                            item.setBackground(
                                    NAVY_DARK
                            );
                        }
                    }
            );
        }

        return item;
    }

    private void showStudents() {
        pageLayout.show(pageContainer, STUDENTS_PAGE);
    }

    private void showAnnouncements() {
        pageLayout.show(pageContainer, ANNOUNCEMENTS_PAGE);
    }

    private void showDashboard() {
        pageLayout.show(pageContainer, DASHBOARD_PAGE);
    }

    private void showStudyMaterials() {
        pageLayout.show(pageContainer, STUDY_MATERIALS_PAGE);
    }

    private void showEvents() {
        pageLayout.show(pageContainer, EVENTS_PAGE);
    }

    private void showDoubts() {
        pageLayout.show(pageContainer, DOUBTS_PAGE);
    }

    private void showFaculty() {
        pageLayout.show(pageContainer, FACULTY_PAGE);
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(
                BACKGROUND
        );

        // -----------------------------------------------------
        // TOP HEADER
        // -----------------------------------------------------

        content.add(
                createTopHeader(),
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                0,
                                20
                        )
                );

        center.setOpaque(false);

        center.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        30,
                        30
                )
        );

        center.add(
                createWelcomePanel(),
                BorderLayout.NORTH
        );

        center.add(
                createModuleSection(),
                BorderLayout.CENTER
        );

        content.add(
                center,
                BorderLayout.CENTER
        );

        return content;
    }

    // =========================================================
    // TOP HEADER
    // =========================================================

    private JPanel createTopHeader() {

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
                                16,
                                30,
                                16,
                                30
                        )
                )
        );

        JLabel pageTitle =
                new JLabel(
                        "Admin Dashboard"
                );

        pageTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        pageTitle.setForeground(
                TEXT_DARK
        );

        JLabel portal =
                new JLabel(
                        "CampusConnect  •  Admin Portal"
                );

        portal.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        portal.setForeground(
                TEXT_GRAY
        );

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.add(pageTitle);

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(portal);

        header.add(
                left,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // RIGHT ADMIN
        // -----------------------------------------------------

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        right.setOpaque(false);

        JLabel online =
                new JLabel(
                        "● Online"
                );

        online.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        online.setForeground(
                GREEN
        );

        JLabel admin =
                new JLabel(
                        adminName
                );

        admin.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        admin.setForeground(
                TEXT_DARK
        );

        right.add(online);
        right.add(admin);

        header.add(
                right,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // WELCOME PANEL
    // =========================================================

    private JPanel createWelcomePanel() {

        JPanel welcome =
                new RoundedPanel(
                        NAVY,
                        NAVY,
                        22
                );

        welcome.setLayout(
                new BorderLayout()
        );

        welcome.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
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

        JLabel welcomeTitle =
                new JLabel(
                        "Welcome back, "
                                + adminName
                                + "!"
                );

        welcomeTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        welcomeTitle.setForeground(
                WHITE
        );

        JLabel subtitle =
                new JLabel(
                        "Manage your CampusConnect platform from one place."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(
                        207,
                        225,
                        235
                )
        );

        text.add(
                welcomeTitle
        );

        text.add(
                Box.createVerticalStrut(7)
        );

        text.add(
                subtitle
        );

        welcome.add(
                text,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // ADMIN BADGE
        // -----------------------------------------------------

        JLabel badge =
                new JLabel(
                        "ADMIN"
                );

        badge.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        badge.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        badge.setForeground(
                WHITE
        );

        badge.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                93,
                                190,
                                200
                        ),
                        1
                )
        );

        badge.setPreferredSize(
                new Dimension(
                        95,
                        38
                )
        );

        welcome.add(
                badge,
                BorderLayout.EAST
        );

        return welcome;
    }

    // =========================================================
    // MODULE SECTION
    // =========================================================

    private JPanel createModuleSection() {

        JPanel section =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        section.setOpaque(false);

        // -----------------------------------------------------
        // SECTION TITLE
        // -----------------------------------------------------

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Management Modules"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(
                TEXT_DARK
        );

        JLabel subtitle =
                new JLabel(
                        "Choose a module to manage CampusConnect data"
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

        section.add(
                heading,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // CARDS
        // -----------------------------------------------------

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                18,
                                18
                        )
                );

        cards.setOpaque(false);

        cards.add(
                createModuleCard(
                        "S",
                        "Manage Students",
                        "View and manage registered students",
                        BLUE,
                        e -> showStudents()
                )
        );

        cards.add(
                createModuleCard(
                        "A",
                        "Announcements",
                        "Create and manage campus announcements",
                        TEAL,
                        e -> showAnnouncements()
                )
        );

        cards.add(
                createModuleCard(
                        "M",
                        "Study Materials",
                        "Upload and manage study resources",
                        PURPLE,
                        e -> new ManageStudyMaterialsFrame()
                )
        );

        cards.add(
                createModuleCard(
                        "E",
                        "Campus Events",
                        "Create and manage upcoming events",
                        ORANGE,
                        e -> showEvents()
                )
        );

        cards.add(
                createModuleCard(
                        "D",
                        "Student Doubts",
                        "Review questions and provide answers",
                        GREEN,
                        e -> showDoubts()
                )
        );

        // Empty sixth space

        cards.add(
                createModuleCard(
                        "F",
                        "Manage Faculty",
                        "Review and approve faculty registrations",
                        BLUE,
                        e -> showFaculty()
                )
        );

        section.add(
                cards,
                BorderLayout.CENTER
        );

        return section;
    }

    // =========================================================
    // MODULE CARD
    // =========================================================

    private JPanel createModuleCard(
            String icon,
            String title,
            String description,
            Color accent,
            java.awt.event.ActionListener action
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        WHITE,
                        BORDER,
                        20
                );

        card.setLayout(
                new BorderLayout(
                        15,
                        10
                )
        );

        card.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        18,
                        20
                )
        );

        // -----------------------------------------------------
        // ICON
        // -----------------------------------------------------

        JLabel iconLabel =
                new JLabel(
                        icon,
                        SwingConstants.CENTER
                );

        iconLabel.setPreferredSize(
                new Dimension(
                        52,
                        52
                )
        );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        iconLabel.setForeground(
                accent
        );

        iconLabel.setOpaque(true);

        iconLabel.setBackground(
                new Color(
                        accent.getRed(),
                        accent.getGreen(),
                        accent.getBlue(),
                        25
                )
        );

        card.add(
                iconLabel,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // TEXT
        // -----------------------------------------------------

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
                        16
                )
        );

        titleLabel.setForeground(
                TEXT_DARK
        );

        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + description
                                + "</html>"
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        descriptionLabel.setForeground(
                TEXT_GRAY
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(7)
        );

        text.add(
                descriptionLabel
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // ARROW
        // -----------------------------------------------------

        JLabel arrow =
                new JLabel(
                        "›"
                );

        arrow.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        arrow.setForeground(
                accent
        );

        arrow.setVerticalAlignment(
                SwingConstants.CENTER
        );

        card.add(
                arrow,
                BorderLayout.EAST
        );

        // -----------------------------------------------------
        // CLICK
        // -----------------------------------------------------

        card.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        card.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        action.actionPerformed(
                                new java.awt.event.ActionEvent(
                                        card,
                                        java.awt.event.ActionEvent.ACTION_PERFORMED,
                                        title
                                )
                        );
                    }

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        card.setBackground(
                                new Color(
                                        248,
                                        251,
                                        253
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        card.setBackground(
                                WHITE
                        );
                    }
                }
        );

        return card;
    }

    // =========================================================
    // LOGOUT BUTTON
    // =========================================================

    private JButton createLogoutButton() {

        JButton logout =
                new JButton(
                        "Logout"
                );

        logout.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        logout.setForeground(
                new Color(
                        255,
                        190,
                        190
                )
        );

        logout.setBackground(
                new Color(
                        35,
                        62,
                        84
                )
        );

        logout.setFocusPainted(
                false
        );

        logout.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        76,
                                        102,
                                        122
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                9,
                                15,
                                9,
                                15
                        )
                )
        );

        logout.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        logout.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        logout.addActionListener(
                e -> {

                    int choice =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Are you sure you want to logout?",
                                    "Confirm Logout",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                    if (
                            choice ==
                                    JOptionPane.YES_OPTION
                    ) {

                        dispose();

                        new LoginFrame();
                    }
                }
        );

        return logout;
    }

    // =========================================================
    // ADMIN INITIAL
    // =========================================================

    private String getInitial() {

        if (
                adminName == null ||
                        adminName.trim().isEmpty()
        ) {

            return "A";
        }

        return adminName
                .trim()
                .substring(
                        0,
                        1
                )
                .toUpperCase();
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
                        new AdminDashboardFrame(
                                "Admin"
                        )
        );
    }
}
