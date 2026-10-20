package com.campusconnect.ui.student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import com.campusconnect.config.DatabaseConnection;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * CampusConnect - Modern Student Dashboard
 *
 * UI:
 * - Modern sidebar
 * - Active navigation indicator
 * - Modern top header
 * - Rounded dashboard cards
 * - Academic overview
 * - Upcoming events
 * - Campus notices
 * - Quick access
 * - Campus activity feed
 *
 * Existing functionality:
 * - Student Profile
 * - Study Materials
 * - Announcements
 * - Events
 * - Doubt / Query
 * - Logout
 * - Embedded module loading
 */
public class DashboardFrame extends JFrame {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color NAVY =
            new Color(20, 55, 95);

    private static final Color NAVY_DARK =
            new Color(12, 39, 70);

    private static final Color NAVY_LIGHT =
            new Color(31, 77, 122);

    private static final Color BLUE =
            new Color(48, 116, 205);

    private static final Color BLUE_DARK =
            new Color(35, 91, 170);

    private static final Color BLUE_LIGHT =
            new Color(232, 242, 255);

    private static final Color TEAL =
            new Color(30, 165, 180);

    private static final Color TEAL_LIGHT =
            new Color(228, 248, 250);

    private static final Color GREEN =
            new Color(35, 155, 92);

    private static final Color GREEN_LIGHT =
            new Color(231, 248, 238);

    private static final Color ORANGE =
            new Color(242, 145, 35);

    private static final Color ORANGE_LIGHT =
            new Color(255, 245, 229);

    private static final Color RED =
            new Color(220, 75, 75);

    private static final Color RED_LIGHT =
            new Color(255, 235, 235);

    private static final Color BACKGROUND =
            new Color(245, 248, 252);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color TEXT =
            new Color(25, 42, 62);

    private static final Color TEXT_GRAY =
            new Color(105, 120, 140);

    private static final Color BORDER =
            new Color(220, 227, 236);

    private static final Color CARD_BACKGROUND =
            new Color(255, 255, 255);

    // =========================================================
    // USER DATA
    // =========================================================

    private String userEmail;

    private String studentName;

    private String studentCourse = "N/A";

    private String studentSemester = "N/A";

    private String studentCgpa = "N/A";

    private String studentStatus = "N/A";

    private String studentPhotoPath;

    // =========================================================
    // MAIN UI
    // =========================================================

    private JPanel mainContentPanel;

    private CardLayout cardLayout;

    private JPanel sidebar;

    private JPanel topBar;

    private JLabel headerNameLabel;

    private JLabel profileNameLabel;

    private JLabel profileEmailLabel;

    private JLabel profileAvatarLabel;

    private JLabel welcomeLabel;

    // =========================================================
    // NAVIGATION
    // =========================================================

    private final Map<String, JPanel> moduleCache =
            new HashMap<>();

    private final Map<String, JButton> navigationButtons =
            new HashMap<>();

    private String activeNavigation =
            "Dashboard";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DashboardFrame(String userEmail) {

        this.userEmail =
                userEmail == null
                        ? ""
                        : userEmail.trim();

        this.studentName =
                getNameFromEmail(this.userEmail);

        // Load this logged-in student's latest academic data from MySQL.
        loadStudentData();

        initializeFrame();

        initializeUI();

        showDashboard();
    }

    // =========================================================
    // LOAD LOGGED-IN STUDENT DATA
    // =========================================================


    private void loadStudentData() {

        if (userEmail == null || userEmail.trim().isEmpty()) {
            return;
        }

        String sql =
                "SELECT name, course, semester, cgpa, status, profile_photo " +
                        "FROM students WHERE email = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, userEmail);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String databaseName = resultSet.getString("name");
                    if (databaseName != null && !databaseName.trim().isEmpty()) {
                        studentName = databaseName.trim();
                    }

                    String databaseCourse = resultSet.getString("course");
                    studentCourse = (databaseCourse == null || databaseCourse.trim().isEmpty())
                            ? "N/A" : databaseCourse.trim();

                    int semesterValue = resultSet.getInt("semester");
                    studentSemester = resultSet.wasNull()
                            ? "N/A" : String.valueOf(semesterValue);

                    BigDecimal cgpaValue = resultSet.getBigDecimal("cgpa");
                    studentCgpa = cgpaValue == null
                            ? "N/A"
                            : cgpaValue.setScale(
                            2,
                            java.math.RoundingMode.HALF_UP
                    ).toPlainString();

                    String databaseStatus = resultSet.getString("status");
                    studentStatus = (databaseStatus == null || databaseStatus.trim().isEmpty())
                            ? "N/A" : databaseStatus.trim();

                    // Load the logged-in student's profile photo path
                    studentPhotoPath = resultSet.getString("profile_photo");

                    if (studentPhotoPath != null) {
                        studentPhotoPath = studentPhotoPath.trim();

                        if (studentPhotoPath.isEmpty()) {
                            studentPhotoPath = null;
                        }
                    }

                } else {
                    System.err.println(
                            "No student record found for email: " + userEmail
                    );
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Unable to load student academic details from the database.\n"
                            + ex.getMessage(),
                    "CampusConnect Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // FRAME INITIALIZATION
    // =========================================================

    private void initializeFrame() {

        setTitle(
                "CampusConnect - Student Dashboard"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(
                        1150,
                        720
                )
        );

        setSize(
                1500,
                900
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout()
        );
    }

    // =========================================================
    // MAIN UI
    // =========================================================

    private void initializeUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

        // Sidebar
        sidebar =
                createSidebar();

        root.add(
                sidebar,
                BorderLayout.WEST
        );

        // Right side
        JPanel rightSide =
                new JPanel(
                        new BorderLayout()
                );

        rightSide.setBackground(
                BACKGROUND
        );

        // Header
        topBar =
                createTopBar();

        rightSide.add(
                topBar,
                BorderLayout.NORTH
        );

        // Main content
        cardLayout =
                new CardLayout();

        mainContentPanel =
                new JPanel(
                        cardLayout
                );

        mainContentPanel.setBackground(
                BACKGROUND
        );

        rightSide.add(
                mainContentPanel,
                BorderLayout.CENTER
        );

        root.add(
                rightSide,
                BorderLayout.CENTER
        );

        add(
                root,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setPreferredSize(
                new Dimension(298, 0)
        );

        panel.setBackground(
                NAVY
        );

        // =====================================================
        // SIDEBAR TOP
        // =====================================================

        JPanel top =
                new JPanel();

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        top.setBackground(
                NAVY
        );

        top.setBorder(
                new EmptyBorder(
                        28,
                        18,
                        15,
                        18
                )
        );

        // =====================================================
        // BRAND
        // =====================================================

        JPanel brandPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                0
                        )
                );

        brandPanel.setBackground(
                NAVY
        );



        // Display the complete logo without cropping or stretching it.
        URL logoUrl = getClass().getResource("/campusconnect_logo.png");

        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setVerticalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(180, 120));
        logo.setMinimumSize(new Dimension(180, 120));
        logo.setOpaque(false);

        if (logoUrl != null) {
            Image originalImage = new ImageIcon(logoUrl).getImage();
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);

            if (originalWidth > 0 && originalHeight > 0) {
                // Keep the image's original aspect ratio so the logo is not cut off.
                double scale = Math.min(
                        180.0 / originalWidth,
                        120.0 / originalHeight
                );

                int scaledWidth = Math.max(1, (int) Math.round(originalWidth * scale));
                int scaledHeight = Math.max(1, (int) Math.round(originalHeight * scale));

                Image scaledImage = originalImage.getScaledInstance(
                        scaledWidth,
                        scaledHeight,
                        Image.SCALE_SMOOTH
                );
                logo.setIcon(new ImageIcon(scaledImage));
            } else {
                logo.setText("CC");
                logo.setFont(new Font("Arial", Font.BOLD, 30));
                logo.setForeground(WHITE);
            }
        } else {
            // Fallback if the logo file is not in src/main/resources.
            logo.setText("CC");
            logo.setFont(new Font("Arial", Font.BOLD, 30));
            logo.setForeground(WHITE);
        }

        brandPanel.setPreferredSize(new Dimension(260, 125));
        brandPanel.setMaximumSize(new Dimension(260, 125));
        brandPanel.add(logo);

        top.add(
                brandPanel
        );

        top.add(
                Box.createVerticalStrut(4)
        );

        JLabel brandName = new JLabel("CampusConnect");

        brandName.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        brandName.setForeground(WHITE);

        brandName.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandName.setHorizontalAlignment(SwingConstants.CENTER);

        brandName.setMaximumSize(
                new Dimension(260, 32)
        );

        brandName.setPreferredSize(
                new Dimension(260, 32)
        );

        brandName.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        brandName.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        brandName.setForeground(
                WHITE
        );

        top.add(
                brandName
        );

        top.add(
                Box.createVerticalStrut(3)
        );

        JLabel portalLabel =
                new JLabel(
                        "Student Portal"
                );

        portalLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        portalLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        portalLabel.setForeground(
                new Color(
                        185,
                        210,
                        235
                )
        );

        top.add(
                portalLabel
        );

        top.add(
                Box.createVerticalStrut(32)
        );

        // =====================================================
        // MENU TITLE
        // =====================================================

        JLabel menuTitle =
                new JLabel(
                        "MAIN MENU"
                );

        menuTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        menuTitle.setForeground(
                new Color(
                        165,
                        195,
                        225
                )
        );

        menuTitle.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        10,
                        0
                )
        );

        top.add(
                menuTitle
        );

        // =====================================================
        // MENU
        // =====================================================

        JPanel menu =
                new JPanel();

        menu.setLayout(
                new BoxLayout(
                        menu,
                        BoxLayout.Y_AXIS
                )
        );

        menu.setBackground(
                NAVY
        );

        addNavigationButton(
                menu,
                MenuIcon.Type.HOME,
                "Dashboard"
        );

        addNavigationButton(
                menu,
                MenuIcon.Type.USER,
                "My Profile"
        );

        addNavigationButton(
                menu,
                MenuIcon.Type.BOOK,
                "Study Materials"
        );

        addNavigationButton(
                menu,
                MenuIcon.Type.BELL,
                "Announcements"
        );

        addNavigationButton(
                menu,
                MenuIcon.Type.CALENDAR,
                "Events"
        );

        addNavigationButton(
                menu,
                MenuIcon.Type.HELP,
                "Doubt / Query"
        );

        top.add(
                menu
        );

        panel.add(
                top,
                BorderLayout.NORTH
        );

        // =====================================================
        // SIDEBAR BOTTOM
        // =====================================================

        JPanel bottom =
                new JPanel();

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBackground(
                NAVY
        );

        bottom.setBorder(
                new EmptyBorder(
                        12,
                        14,
                        20,
                        14
                )
        );

        // =====================================================
        // USER CARD
        // =====================================================

        RoundedPanel userCard =
                new RoundedPanel(
                        16,
                        new Color(
                                30,
                                79,
                                128
                        )
                );

        userCard.setLayout(
                new BorderLayout(
                        10,
                        0
                )
        );

        userCard.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JLabel smallAvatar =
                createAvatarLabel(
                        45
                );

        userCard.add(
                smallAvatar,
                BorderLayout.WEST
        );

        JPanel userInfo =
                new JPanel();

        userInfo.setLayout(
                new BoxLayout(
                        userInfo,
                        BoxLayout.Y_AXIS
                )
        );

        userInfo.setOpaque(
                false
        );

        profileNameLabel =
                new JLabel(
                        studentName
                );

        profileNameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        profileNameLabel.setForeground(
                WHITE
        );

        profileEmailLabel =
                new JLabel(
                        shortenEmail(
                                userEmail
                        )
                );

        profileEmailLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        profileEmailLabel.setForeground(
                new Color(
                        205,
                        225,
                        242
                )
        );

        userInfo.add(
                profileNameLabel
        );

        userInfo.add(
                Box.createVerticalStrut(
                        4
                )
        );

        userInfo.add(
                profileEmailLabel
        );

        userCard.add(
                userInfo,
                BorderLayout.CENTER
        );

        bottom.add(
                userCard
        );

        bottom.add(
                Box.createVerticalStrut(
                        12
                )
        );

        // =====================================================
        // LOGOUT
        // =====================================================

        JButton logoutButton =
                createLogoutButton();

        logoutButton.addActionListener(
                e -> logout()
        );

        bottom.add(
                logoutButton
        );

        panel.add(
                bottom,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // =========================================================
    // ADD NAVIGATION BUTTON
    // =========================================================

    private void addNavigationButton(
            JPanel menu,
            MenuIcon.Type iconType,
            String text
    ) {

        JButton button =
                createNavigationButton(
                        iconType,
                        text
                );

        navigationButtons.put(
                text,
                button
        );

        button.addActionListener(
                e -> {

                    setActiveNavigation(
                            text
                    );

                    switch (text) {

                        case "Dashboard":
                            showDashboard();
                            break;

                        case "My Profile":
                            showProfile();
                            break;

                        case "Study Materials":
                            showStudyMaterials();
                            break;

                        case "Announcements":
                            showAnnouncements();
                            break;

                        case "Events":
                            showEvents();
                            break;

                        case "Doubt / Query":
                            showDoubts();
                            break;
                    }
                }
        );

        menu.add(
                button
        );

        menu.add(
                Box.createVerticalStrut(
                        5
                )
        );
    }

    // =========================================================
    // ACTIVE NAVIGATION
    // =========================================================

    private void setActiveNavigation(
            String text
    ) {

        activeNavigation =
                text;

        for (
                Map.Entry<String, JButton> entry
                : navigationButtons.entrySet()
        ) {

            JButton button =
                    entry.getValue();

            if (
                    entry.getKey()
                            .equals(text)
            ) {

                button.setBackground(
                        NAVY_LIGHT
                );

                button.putClientProperty(
                        "active",
                        true
                );

            } else {

                button.setBackground(
                        NAVY
                );

                button.putClientProperty(
                        "active",
                        false
                );
            }
        }
    }

    // =========================================================
    // NAVIGATION BUTTON
    // =========================================================

    private JButton createNavigationButton(
            MenuIcon.Type iconType,
            String text
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        54
                )
        );

        button.setPreferredSize(
                new Dimension(
                        248,
                        54
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setBackground(
                NAVY
        );

        button.setForeground(
                WHITE
        );

        button.setBorder(
                new EmptyBorder(
                        0,
                        12,
                        0,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setContentAreaFilled(
                true
        );

        button.setOpaque(
                true
        );

        button.setUI(
                new BasicButtonUI()
        );

        // =====================================================
        // ICON
        // =====================================================

        JLabel iconLabel =
                new JLabel(
                        new MenuIcon(
                                iconType
                        )
                );

        iconLabel.setPreferredSize(
                new Dimension(
                        38,
                        42
                )
        );

        iconLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        iconLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        // =====================================================
        // TEXT
        // =====================================================

        JLabel textLabel =
                new JLabel(
                        "<html><div style='width:155px'>"
                                + text
                                + "</div></html>"
                );

        textLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        textLabel.setForeground(
                WHITE
        );

        button.add(
                iconLabel,
                BorderLayout.WEST
        );

        button.add(
                textLabel,
                BorderLayout.CENTER
        );

        // =====================================================
        // HOVER
        // =====================================================

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (
                                Boolean.TRUE.equals(
                                        button.getClientProperty(
                                                "active"
                                        )
                                )
                        ) {

                            button.setBackground(
                                    BLUE_DARK
                            );

                        } else {

                            button.setBackground(
                                    NAVY_LIGHT
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (
                                Boolean.TRUE.equals(
                                        button.getClientProperty(
                                                "active"
                                        )
                                )
                        ) {

                            button.setBackground(
                                    NAVY_LIGHT
                            );

                        } else {

                            button.setBackground(
                                    NAVY
                            );
                        }
                    }
                }
        );

        return button;
    }

    // =========================================================
    // SIDEBAR MENU ICONS
    // =========================================================

    private static final class MenuIcon
            implements Icon {

        private enum Type {
            HOME,
            USER,
            BOOK,
            BELL,
            CALENDAR,
            HELP
        }

        private final Type type;

        private MenuIcon(
                Type type
        ) {

            this.type =
                    type;
        }

        @Override
        public int getIconWidth() {

            return 22;
        }

        @Override
        public int getIconHeight() {

            return 22;
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    new Color(
                            220,
                            238,
                            250
                    )
            );

            g2.setStroke(
                    new BasicStroke(
                            1.8f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            int cx =
                    x + 11;

            switch (type) {

                case HOME -> {

                    Polygon roof =
                            new Polygon();

                    roof.addPoint(
                            cx,
                            y + 3
                    );

                    roof.addPoint(
                            x + 3,
                            y + 11
                    );

                    roof.addPoint(
                            x + 19,
                            y + 11
                    );

                    g2.drawPolygon(
                            roof
                    );

                    g2.drawRoundRect(
                            x + 6,
                            y + 10,
                            10,
                            9,
                            2,
                            2
                    );
                }

                case USER -> {

                    g2.drawOval(
                            x + 7,
                            y + 3,
                            8,
                            8
                    );

                    g2.drawArc(
                            x + 4,
                            y + 10,
                            14,
                            10,
                            0,
                            180
                    );
                }

                case BOOK -> {

                    g2.drawRoundRect(
                            x + 3,
                            y + 4,
                            16,
                            15,
                            2,
                            2
                    );

                    g2.drawLine(
                            cx,
                            y + 4,
                            cx,
                            y + 19
                    );
                }

                case BELL -> {

                    g2.drawArc(
                            x + 5,
                            y + 4,
                            12,
                            13,
                            0,
                            180
                    );

                    g2.drawLine(
                            x + 5,
                            y + 10,
                            x + 5,
                            y + 15
                    );

                    g2.drawLine(
                            x + 17,
                            y + 10,
                            x + 17,
                            y + 15
                    );

                    g2.drawLine(
                            x + 3,
                            y + 15,
                            x + 19,
                            y + 15
                    );

                    g2.fillOval(
                            x + 9,
                            y + 17,
                            4,
                            3
                    );
                }

                case CALENDAR -> {

                    g2.drawRoundRect(
                            x + 3,
                            y + 5,
                            16,
                            14,
                            2,
                            2
                    );

                    g2.drawLine(
                            x + 3,
                            y + 9,
                            x + 19,
                            y + 9
                    );

                    g2.drawLine(
                            x + 7,
                            y + 3,
                            x + 7,
                            y + 7
                    );

                    g2.drawLine(
                            x + 15,
                            y + 3,
                            x + 15,
                            y + 7
                    );
                }

                case HELP -> {

                    g2.drawOval(
                            x + 3,
                            y + 3,
                            16,
                            16
                    );

                    g2.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    13
                            )
                    );

                    g2.drawString(
                            "?",
                            x + 8,
                            y + 15
                    );
                }
            }

            g2.dispose();
        }
    }

    // =========================================================
    // LOGOUT BUTTON
    // =========================================================

    private JButton createLogoutButton() {

        JButton button =
                new JButton(
                        "Logout",
                        new LogoutIcon()
                );

        button.setIconTextGap(
                10
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                new Color(
                        240,
                        248,
                        255
                )
        );

        button.setBackground(
                NAVY
        );

        button.setBorder(
                new EmptyBorder(
                        12,
                        15,
                        12,
                        15
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setContentAreaFilled(
                true
        );

        button.setOpaque(
                true
        );

        button.setUI(
                new BasicButtonUI()
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        165,
                                        55,
                                        65
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                NAVY
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private JPanel createTopBar() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                WHITE
        );

        panel.setPreferredSize(
                new Dimension(
                        0,
                        94
                )
        );

        panel.setBorder(
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

        // =====================================================
        // LEFT
        // =====================================================

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.setOpaque(
                false
        );

        welcomeLabel =
                new JLabel(
                        "Welcome, " +
                                studentName +
                                "!"
                );

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        welcomeLabel.setForeground(
                NAVY_DARK
        );

        JLabel subtitle =
                new JLabel(
                        "Here's what's happening with your academics today."
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
                welcomeLabel
        );

        left.add(
                Box.createVerticalStrut(
                        4
                )
        );

        left.add(
                subtitle
        );

        panel.add(
                left,
                BorderLayout.WEST
        );

        // =====================================================
        // RIGHT PROFILE
        // =====================================================

        JPanel profile =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        profile.setOpaque(
                false
        );

        profileAvatarLabel =
                createAvatarLabel(
                        52
                );

        profile.add(
                profileAvatarLabel
        );

        JPanel profileText =
                new JPanel();

        profileText.setLayout(
                new BoxLayout(
                        profileText,
                        BoxLayout.Y_AXIS
                )
        );

        profileText.setOpaque(
                false
        );

        headerNameLabel =
                new JLabel(
                        studentName
                );

        headerNameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        headerNameLabel.setForeground(
                TEXT
        );

        JLabel studentLabel =
                new JLabel(
                        "N/A".equals(studentCourse) ? "Student" : studentCourse + " Student"
                );

        studentLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        studentLabel.setForeground(
                TEXT_GRAY
        );

        profileText.add(
                headerNameLabel
        );

        profileText.add(
                Box.createVerticalStrut(
                        8
                )
        );

        profileText.add(
                studentLabel
        );

        profile.add(
                profileText
        );

        panel.add(
                profile,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // AVATAR
    // =========================================================


    private JLabel createAvatarLabel(int size) {

        JLabel avatar = new JLabel();

        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setVerticalAlignment(SwingConstants.CENTER);

        avatar.setPreferredSize(new Dimension(size, size));
        avatar.setMinimumSize(new Dimension(size, size));

        avatar.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        Math.max(16, size / 2)
                )
        );

        ImageIcon icon = loadStudentImage(size);

        if (icon != null) {

            avatar.setIcon(icon);
            avatar.setText("");
            avatar.setOpaque(false);

        } else {

            avatar.setIcon(null);
            avatar.setText(getInitials(studentName));
            avatar.setOpaque(true);
            avatar.setBackground(TEAL);
            avatar.setForeground(WHITE);
        }

        return avatar;
    }

    // =========================================================
    // LOAD STUDENT IMAGE
    // =========================================================


    private ImageIcon loadStudentImage(int size) {

        try {

            // 1. Load the logged-in student's photo from the database path
            if (studentPhotoPath != null && !studentPhotoPath.trim().isEmpty()) {

                File studentFile = new File(studentPhotoPath.trim());

                if (studentFile.isFile()) {

                    Image image = new ImageIcon(studentFile.getAbsolutePath())
                            .getImage()
                            .getScaledInstance(
                                    size,
                                    size,
                                    Image.SCALE_SMOOTH
                            );

                    return new ImageIcon(image);
                }
            }

            // 2. Fallback: check images available in resources
            String[] paths = {
                    "/student.jpg",
                    "/student.png",
                    "/images/student.jpg",
                    "/images/student.png"
            };

            for (String path : paths) {

                URL url = getClass().getResource(path);

                if (url != null) {

                    Image image = new ImageIcon(url)
                            .getImage()
                            .getScaledInstance(
                                    size,
                                    size,
                                    Image.SCALE_SMOOTH
                            );

                    return new ImageIcon(image);
                }
            }

            // 3. Fallback: check the project resources folder
            File file = new File("src/main/resources/student.jpg");

            if (file.isFile()) {

                Image image = new ImageIcon(file.getAbsolutePath())
                        .getImage()
                        .getScaledInstance(
                                size,
                                size,
                                Image.SCALE_SMOOTH
                        );

                return new ImageIcon(image);
            }

        } catch (Exception ex) {
            System.err.println("Unable to load student profile photo: "
                    + ex.getMessage());
        }

        return null;
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void showDashboard() {

        setActiveNavigation(
                "Dashboard"
        );

        JPanel dashboard =
                createDashboardHome();

        showPanel(
                "DASHBOARD",
                dashboard
        );
    }

    // =========================================================
    // DASHBOARD HOME
    // =========================================================

    private JPanel createDashboardHome() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

        root.setBorder(
                new EmptyBorder(
                        24,
                        28,
                        24,
                        28
                )
        );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                BACKGROUND
        );

        // =====================================================
        // HERO
        // =====================================================

        JPanel hero =
                createHeroPanel();

        content.add(
                hero
        );

        content.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // STATISTICS
        // =====================================================

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                14,
                                0
                        )
                );

        stats.setBackground(
                BACKGROUND
        );

        stats.add(
                createStatCard(
                        "CGPA",
                        studentCgpa,
                        "Current Performance",
                        BLUE,
                        BLUE_LIGHT
                )
        );

        stats.add(
                createStatCard(
                        "Semester",
                        studentSemester,
                        "Current Semester",
                        TEAL,
                        TEAL_LIGHT
                )
        );

        stats.add(
                createStatCard(
                        "Course",
                        studentCourse,
                        "Academic Program",
                        ORANGE,
                        ORANGE_LIGHT
                )
        );

        stats.add(
                createStatCard(
                        "Status",
                        studentStatus,
                        "Student Account",
                        GREEN,
                        GREEN_LIGHT
                )
        );

        content.add(
                stats
        );

        content.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // TOP INFORMATION
        // =====================================================

        JPanel topCards =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                18,
                                0
                        )
                );

        topCards.setBackground(
                BACKGROUND
        );

        topCards.add(
                createEventCard()
        );

        topCards.add(
                createNoticeCard()
        );

        content.add(
                topCards
        );

        content.add(
                Box.createVerticalStrut(
                        18
                )
        );

        // =====================================================
        // BOTTOM
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                18,
                                0
                        )
                );

        bottom.setBackground(
                BACKGROUND
        );

        bottom.add(
                createQuickAccess()
        );

        bottom.add(
                createActivityFeed()
        );

        content.add(
                bottom
        );

        // =====================================================
        // SCROLL
        // =====================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        content
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.setBackground(
                BACKGROUND
        );

        scrollPane.getViewport()
                .setBackground(
                        BACKGROUND
                );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getVerticalScrollBar().setBlockIncrement(90);

        root.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return root;
    }

    // =========================================================
    // HERO PANEL
    // =========================================================

    private JPanel createHeroPanel() {

        RoundedPanel hero =
                new RoundedPanel(
                        20,
                        NAVY
                );

        hero.setLayout(
                new BorderLayout()
        );

        hero.setBorder(
                new EmptyBorder(
                        22,
                        24,
                        22,
                        24
                )
        );

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.setOpaque(
                false
        );

        JLabel title =
                new JLabel(
                        "Your Campus, Connected."
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(
                WHITE
        );

        JLabel description =
                new JLabel(
                        "<html>Stay updated with your studies, "
                                + "events, announcements and campus activities.</html>"
                );

        description.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        description.setForeground(
                new Color(
                        205,
                        225,
                        242
                )
        );

        left.add(
                title
        );

        left.add(
                Box.createVerticalStrut(
                        7
                )
        );

        left.add(
                description
        );

        hero.add(
                left,
                BorderLayout.CENTER
        );

        // =====================================================
        // HERO BADGE
        // =====================================================

        RoundedPanel badge =
                new RoundedPanel(
                        15,
                        TEAL
                );

        badge.setLayout(
                new BorderLayout()
        );

        badge.setPreferredSize(
                new Dimension(
                        125,
                        72
                )
        );

        JLabel badgeTitle =
                new JLabel(
                        "N/A".equals(studentSemester)
                                ? "SEMESTER N/A"
                                : "SEMESTER " + studentSemester
                );

        badgeTitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        badgeTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        badgeTitle.setForeground(
                WHITE
        );

        badge.add(
                badgeTitle,
                BorderLayout.CENTER
        );

        hero.add(
                badge,
                BorderLayout.EAST
        );

        return hero;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            String value,
            String subtitle,
            Color accent,
            Color lightColor
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        16,
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
                        16,
                        16,
                        16,
                        16
                )
        );

        // Accent
        JPanel accentPanel =
                new JPanel();

        accentPanel.setBackground(
                accent
        );

        accentPanel.setPreferredSize(
                new Dimension(
                        5,
                        65
                )
        );

        card.add(
                accentPanel,
                BorderLayout.WEST
        );

        // Content
        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setOpaque(
                false
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
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
                        23
                )
        );

        valueLabel.setForeground(
                accent
        );

        JLabel subtitleLabel =
                new JLabel(
                        subtitle
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        subtitleLabel.setForeground(
                TEXT_GRAY
        );

        content.add(
                titleLabel
        );

        content.add(
                Box.createVerticalStrut(
                        4
                )
        );

        content.add(
                valueLabel
        );

        content.add(
                Box.createVerticalStrut(
                        2
                )
        );

        content.add(
                subtitleLabel
        );

        card.add(
                content,
                BorderLayout.CENTER
        );

        // Light badge
        JLabel badge =
                new JLabel(
                        "●"
                );

        badge.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        badge.setForeground(
                accent
        );

        card.add(
                badge,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // ACADEMIC CARD
    // =========================================================

    private JPanel createInfoCard(
            String title,
            String label1,
            String value1,
            String label2,
            String value2,
            Color accent
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                createSectionTitle(
                        title
                ),
                BorderLayout.NORTH
        );

        JPanel values =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                15,
                                10
                        )
                );

        values.setOpaque(
                false
        );

        values.add(
                createSmallLabel(
                        label1
                )
        );

        values.add(
                createSmallLabel(
                        label2
                )
        );

        values.add(
                createValueLabel(
                        value1,
                        accent
                )
        );

        values.add(
                createValueLabel(
                        value2,
                        GREEN
                )
        );

        card.add(
                values,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // EVENT CARD
    // =========================================================

    private JPanel createEventCard() {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                createSectionTitle(
                        "Upcoming Events"
                ),
                BorderLayout.NORTH
        );

        JPanel list =
                new JPanel();

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.setOpaque(
                false
        );

        list.add(
                createListItem(
                        "Annual Cultural Fest",
                        "15 Oct 2026  •  University Auditorium",
                        BLUE
                )
        );

        list.add(
                Box.createVerticalStrut(
                        8
                )
        );

        list.add(
                createListItem(
                        "Tech Innovation Day",
                        "22 Oct 2026  •  Computer Lab 1",
                        ORANGE
                )
        );

        list.add(
                Box.createVerticalStrut(
                        8
                )
        );

        list.add(
                createListItem(
                        "Sports Day",
                        "05 Nov 2026  •  University Ground",
                        GREEN
                )
        );

        card.add(
                list,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // NOTICE CARD
    // =========================================================

    private JPanel createNoticeCard() {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                createSectionTitle(
                        "Campus Notices"
                ),
                BorderLayout.NORTH
        );

        JPanel list =
                new JPanel();

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.setOpaque(
                false
        );

        list.add(
                createNoticeItem(
                        "Campus Event",
                        "Annual Campus Event registration is now open for all students.",
                        BLUE
                )
        );

        list.add(
                Box.createVerticalStrut(
                        8
                )
        );

        list.add(
                createNoticeItem(
                        "Holiday Notice",
                        "The university will remain closed on the upcoming public holiday.",
                        ORANGE
                )
        );

        list.add(
                Box.createVerticalStrut(
                        8
                )
        );

        list.add(
                createNoticeItem(
                        "Project Submission",
                        "All students must submit their project before the final submission deadline.",
                        GREEN
                )
        );

        card.add(
                list,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // QUICK ACCESS
    // =========================================================

    private JPanel createQuickAccess() {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        15
                )
        );

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                createSectionTitle(
                        "Quick Access"
                ),
                BorderLayout.NORTH
        );

        JPanel buttons =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                0
                        )
                );

        buttons.setOpaque(
                false
        );

        buttons.add(
                createQuickButton(
                        "BOOK",
                        "Study\nMaterials",
                        TEAL,
                        e -> showStudyMaterials()
                )
        );

        buttons.add(
                createQuickButton(
                        "BELL",
                        "Announcements",
                        BLUE,
                        e -> showAnnouncements()
                )
        );

        buttons.add(
                createQuickButton(
                        "CALENDAR",
                        "Events",
                        ORANGE,
                        e -> showEvents()
                )
        );

        card.add(
                buttons,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // QUICK BUTTON
    // =========================================================

    private JButton createQuickButton(
            String icon,
            String text,
            Color accent,
            java.awt.event.ActionListener listener
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setBackground(
                new Color(
                        249,
                        251,
                        254
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                true
        );

        button.setOpaque(
                true
        );

        button.setContentAreaFilled(
                true
        );

        button.setUI(
                new BasicButtonUI()
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                12,
                                8,
                                12,
                                8
                        )
                )
        );

        JLabel iconLabel =
                new JLabel(
                        createQuickIcon(
                                icon,
                                accent
                        )
                );

        iconLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JLabel textLabel =
                new JLabel(
                        "<html><center>"
                                + text.replace(
                                "\n",
                                "<br>"
                        )
                                + "</center></html>"
                );

        textLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        textLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        textLabel.setForeground(
                TEXT
        );

        button.add(
                iconLabel,
                BorderLayout.CENTER
        );

        button.add(
                textLabel,
                BorderLayout.SOUTH
        );

        button.addActionListener(
                listener
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        235,
                                        244,
                                        250
                                )
                        );

                        textLabel.setForeground(
                                accent
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        249,
                                        251,
                                        254
                                )
                        );

                        textLabel.setForeground(
                                TEXT
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // QUICK ICON
    // =========================================================

    private Icon createQuickIcon(
            String type,
            Color color
    ) {

        return new Icon() {

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
                    Component c,
                    Graphics g,
                    int x,
                    int y
            ) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        color
                );

                g2.setStroke(
                        new BasicStroke(
                                2f,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                int cx =
                        x + 17;

                if (
                        type.equals(
                                "BOOK"
                        )
                ) {

                    g2.drawRoundRect(
                            x + 5,
                            y + 5,
                            24,
                            24,
                            4,
                            4
                    );

                    g2.drawLine(
                            cx,
                            y + 5,
                            cx,
                            y + 29
                    );

                } else if (
                        type.equals(
                                "BELL"
                        )
                ) {

                    g2.drawArc(
                            x + 9,
                            y + 5,
                            16,
                            17,
                            0,
                            180
                    );

                    g2.drawLine(
                            x + 9,
                            y + 14,
                            x + 9,
                            y + 22
                    );

                    g2.drawLine(
                            x + 25,
                            y + 14,
                            x + 25,
                            y + 22
                    );

                    g2.drawLine(
                            x + 6,
                            y + 22,
                            x + 28,
                            y + 22
                    );

                    g2.fillOval(
                            x + 15,
                            y + 24,
                            4,
                            4
                    );

                } else {

                    g2.drawRoundRect(
                            x + 5,
                            y + 7,
                            24,
                            22,
                            4,
                            4
                    );

                    g2.drawLine(
                            x + 5,
                            y + 13,
                            x + 29,
                            y + 13
                    );

                    g2.drawLine(
                            x + 11,
                            y + 4,
                            x + 11,
                            y + 10
                    );

                    g2.drawLine(
                            x + 23,
                            y + 4,
                            x + 23,
                            y + 10
                    );
                }

                g2.dispose();
            }
        };
    }

    // =========================================================
    // ACTIVITY FEED
    // =========================================================

    private JPanel createActivityFeed() {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        15
                )
        );

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                createSectionTitle(
                        "Campus Activity Feed"
                ),
                BorderLayout.NORTH
        );

        JPanel feed =
                new JPanel();

        feed.setLayout(
                new BoxLayout(
                        feed,
                        BoxLayout.Y_AXIS
                )
        );

        feed.setOpaque(
                false
        );

        feed.add(
                createFeedItem(
                        "C",
                        "Campus Event",
                        "Annual Campus Event registration is now open for all students.",
                        "04 Oct 2026",
                        BLUE
                )
        );

        feed.add(
                Box.createVerticalStrut(
                        8
                )
        );

        feed.add(
                createFeedItem(
                        "H",
                        "Holiday Notice",
                        "The university will remain closed on the upcoming public holiday.",
                        "02 Oct 2026",
                        ORANGE
                )
        );

        feed.add(
                Box.createVerticalStrut(
                        8
                )
        );

        feed.add(
                createFeedItem(
                        "P",
                        "Project Submission",
                        "All students must submit their project before the final submission deadline.",
                        "28 Sep 2026",
                        GREEN
                )
        );

        card.add(
                feed,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // FEED ITEM
    // =========================================================

    private JPanel createFeedItem(
            String letter,
            String title,
            String message,
            String date,
            Color accent
    ) {

        RoundedPanel item =
                new RoundedPanel(
                        12,
                        new Color(
                                249,
                                251,
                                254
                        )
                );

        item.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        item.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JLabel avatar =
                new JLabel(
                        letter
                );

        avatar.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        avatar.setVerticalAlignment(
                SwingConstants.CENTER
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

        avatar.setBackground(
                accent
        );

        avatar.setOpaque(
                true
        );

        avatar.setPreferredSize(
                new Dimension(
                        42,
                        48
                )
        );

        item.add(
                avatar,
                BorderLayout.WEST
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(
                false
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        titleLabel.setForeground(
                TEXT
        );

        JLabel messageLabel =
                new JLabel(
                        "<html>"
                                + message
                                + "</html>"
                );

        messageLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        messageLabel.setForeground(
                TEXT_GRAY
        );

        JLabel dateLabel =
                new JLabel(
                        date
                );

        dateLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        9
                )
        );

        dateLabel.setForeground(
                accent
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(
                        3
                )
        );

        text.add(
                messageLabel
        );

        text.add(
                Box.createVerticalStrut(
                        3
                )
        );

        text.add(
                dateLabel
        );

        item.add(
                text,
                BorderLayout.CENTER
        );

        return item;
    }

    // =========================================================
    // LIST ITEM
    // =========================================================

    private JPanel createListItem(
            String title,
            String subtitle,
            Color accent
    ) {

        RoundedPanel item =
                new RoundedPanel(
                        10,
                        new Color(
                                249,
                                251,
                                254
                        )
                );

        item.setLayout(
                new BorderLayout(
                        10,
                        0
                )
        );

        item.setBorder(
                new EmptyBorder(
                        9,
                        9,
                        9,
                        9
                )
        );

        JPanel bar =
                new JPanel();

        bar.setBackground(
                accent
        );

        bar.setPreferredSize(
                new Dimension(
                        5,
                        48
                )
        );

        item.add(
                bar,
                BorderLayout.WEST
        );

        JPanel textPanel =
                new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setOpaque(
                false
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        titleLabel.setForeground(
                TEXT
        );

        JLabel subtitleLabel =
                new JLabel(
                        "<html>"
                                + subtitle
                                + "</html>"
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        subtitleLabel.setForeground(
                TEXT_GRAY
        );

        textPanel.add(
                titleLabel
        );

        textPanel.add(
                Box.createVerticalStrut(
                        4
                )
        );

        textPanel.add(
                subtitleLabel
        );

        item.add(
                textPanel,
                BorderLayout.CENTER
        );

        return item;
    }

    // =========================================================
    // NOTICE ITEM
    // =========================================================

    private JPanel createNoticeItem(
            String title,
            String message,
            Color accent
    ) {

        return createListItem(
                title,
                message,
                accent
        );
    }

    // =========================================================
    // CARD PANEL
    // =========================================================

    private JPanel createCardPanel() {

        return new RoundedPanel(
                18,
                WHITE
        );
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private JLabel createSectionTitle(
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
                        16
                )
        );

        label.setForeground(
                TEXT
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
                        Font.PLAIN,
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
            String text,
            Color color
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        label.setForeground(
                color
        );

        return label;
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel {

        private final int radius;

        private final Color fillColor;

        RoundedPanel(
                int radius,
                Color fillColor
        ) {

            this.radius =
                    radius;

            this.fillColor =
                    fillColor;

            setOpaque(
                    false
            );
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

            // Soft shadow
            if (
                    fillColor.equals(
                            WHITE
                    )
            ) {

                g2.setColor(
                        new Color(
                                20,
                                55,
                                95,
                                20
                        )
                );

                g2.fillRoundRect(
                        2,
                        4,
                        getWidth() - 4,
                        getHeight() - 5,
                        radius,
                        radius
                );
            }

            // Main card
            g2.setColor(
                    fillColor
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 3,
                    getHeight() - 4,
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
    // PROFILE
    // =========================================================

    private void showProfile() {

        showModule(
                "PROFILE",
                "com.campusconnect.ui.student.StudentProfileFrame"
        );
    }

    // =========================================================
    // STUDY MATERIALS
    // =========================================================

    private void showStudyMaterials() {

        showModule(
                "STUDY_MATERIALS",
                "com.campusconnect.ui.student.StudyMaterialsFrame"
        );
    }

    // =========================================================
    // ANNOUNCEMENTS
    // =========================================================

    private void showAnnouncements() {

        showModule(
                "ANNOUNCEMENTS",
                "com.campusconnect.ui.student.AnnouncementsFrame"
        );
    }

    // =========================================================
    // EVENTS
    // =========================================================

    private void showEvents() {

        showModule(
                "EVENTS",
                "com.campusconnect.ui.student.EventsFrame"
        );
    }

    // =========================================================
    // DOUBTS
    // =========================================================

    private void showDoubts() {

        showModule(
                "DOUBTS",
                "com.campusconnect.ui.student.DoubtFrame"
        );
    }

    // =========================================================
    // SHOW MODULE
    // =========================================================

    private void showModule(
            String key,
            String className
    ) {

        try {

            JPanel cached =
                    moduleCache.get(
                            key
                    );

            if (
                    cached != null
            ) {

                showPanel(
                        key,
                        cached
                );

                return;
            }

            Object module =
                    createModuleInstance(
                            className
                    );

            if (
                    module == null
            ) {

                showModuleError(
                        "Unable to create module:\n"
                                + className
                );

                return;
            }

            JPanel modulePanel =
                    convertToPanel(
                            module
                    );

            if (
                    modulePanel == null
            ) {

                showModuleError(
                        "This module cannot be displayed inside the dashboard:\n"
                                + className
                );

                return;
            }

            styleModuleButtons(
                    modulePanel
            );

            moduleCache.put(
                    key,
                    modulePanel
            );

            showPanel(
                    key,
                    modulePanel
            );

        } catch (
                Exception ex
        ) {

            ex.printStackTrace();

            showModuleError(
                    "Unable to open module.\n\n"
                            + className
                            + "\n\n"
                            + ex.getMessage()
            );
        }
    }

    // =========================================================
    // CREATE MODULE INSTANCE
    // =========================================================

    private Object createModuleInstance(
            String className
    ) throws Exception {

        Class<?> clazz =
                Class.forName(
                        className
                );

        // =====================================================
        // STRING CONSTRUCTOR
        // =====================================================

        try {

            Constructor<?> constructor =
                    clazz.getConstructor(
                            String.class
                    );

            return constructor.newInstance(
                    userEmail
            );

        } catch (
                NoSuchMethodException ignored
        ) {
        }

        // =====================================================
        // STRING, STRING CONSTRUCTOR
        // =====================================================

        try {

            Constructor<?> constructor =
                    clazz.getConstructor(
                            String.class,
                            String.class
                    );

            return constructor.newInstance(
                    studentName,
                    userEmail
            );

        } catch (
                NoSuchMethodException ignored
        ) {
        }

        // =====================================================
        // NO ARGUMENT CONSTRUCTOR
        // =====================================================

        Constructor<?> constructor =
                clazz.getConstructor();

        return constructor.newInstance();
    }

    // =========================================================
    // STYLE MODULE BUTTONS
    // =========================================================

    private void styleModuleButtons(
            Component component
    ) {

        if (
                component instanceof JButton button
        ) {

            button.setUI(
                    new BasicButtonUI()
            );

            button.setOpaque(
                    true
            );

            button.setContentAreaFilled(
                    true
            );

            button.setFocusPainted(
                    false
            );

            button.setBorderPainted(
                    false
            );

            button.setBackground(
                    BLUE
            );

            button.setForeground(
                    WHITE
            );

            button.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            if (
                                    button.isEnabled()
                            ) {

                                button.setBackground(
                                        TEAL
                                );
                            }
                        }

                        @Override
                        public void mouseExited(
                                MouseEvent e
                        ) {

                            if (
                                    button.isEnabled()
                            ) {

                                button.setBackground(
                                        BLUE
                                );
                            }
                        }
                    }
            );
        }

        if (
                component instanceof Container container
        ) {

            for (
                    Component child
                    : container.getComponents()
            ) {

                styleModuleButtons(
                        child
                );
            }
        }
    }

    // =========================================================
    // CONVERT MODULE TO PANEL
    // =========================================================

    private JPanel convertToPanel(
            Object module
    ) {

        // =====================================================
        // ALREADY JPanel
        // =====================================================

        if (
                module instanceof JPanel
        ) {

            return (JPanel) module;
        }

        // =====================================================
        // JFrame
        // =====================================================

        if (
                module instanceof JFrame
        ) {

            JFrame frame =
                    (JFrame) module;

            Container content =
                    frame.getContentPane();

            frame.setContentPane(
                    new JPanel()
            );

            frame.dispose();

            JPanel wrapper =
                    new JPanel(
                            new BorderLayout()
                    );

            wrapper.setBackground(
                    BACKGROUND
            );

            wrapper.add(
                    content,
                    BorderLayout.CENTER
            );

            return wrapper;
        }

        // =====================================================
        // OTHER JCOMPONENT
        // =====================================================

        if (
                module instanceof JComponent
        ) {

            JPanel wrapper =
                    new JPanel(
                            new BorderLayout()
                    );

            wrapper.setBackground(
                    BACKGROUND
            );

            wrapper.add(
                    (JComponent) module,
                    BorderLayout.CENTER
            );

            return wrapper;
        }

        return null;
    }

    // =========================================================
    // SHOW PANEL
    // =========================================================

    private void showPanel(
            String key,
            JPanel panel
    ) {

        if (
                panel.getParent() == null
        ) {

            mainContentPanel.add(
                    panel,
                    key
            );
        }

        cardLayout.show(
                mainContentPanel,
                key
        );

        mainContentPanel.revalidate();

        mainContentPanel.repaint();
    }

    // =========================================================
    // MODULE ERROR
    // =========================================================

    private void showModuleError(
            String message
    ) {

        JPanel errorPanel =
                new JPanel(
                        new BorderLayout()
                );

        errorPanel.setBackground(
                BACKGROUND
        );

        errorPanel.setBorder(
                new EmptyBorder(
                        50,
                        50,
                        50,
                        50
                )
        );

        JLabel title =
                new JLabel(
                        "Unable to Open Module"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(
                RED
        );

        JLabel details =
                new JLabel(
                        "<html>"
                                + message.replace(
                                "\n",
                                "<br>"
                        )
                                + "</html>"
                );

        details.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        details.setForeground(
                TEXT_GRAY
        );

        JPanel center =
                new JPanel();

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        center.setOpaque(
                false
        );

        center.add(
                title
        );

        center.add(
                Box.createVerticalStrut(
                        15
                )
        );

        center.add(
                details
        );

        errorPanel.add(
                center,
                BorderLayout.NORTH
        );

        showPanel(
                "ERROR",
                errorPanel
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                result ==
                        JOptionPane.YES_OPTION
        ) {

            dispose();

            SwingUtilities.invokeLater(
                    () -> {

                        try {

                            new com.campusconnect.ui.login.LoginFrame()
                                    .setVisible(
                                            true
                                    );

                        } catch (
                                Exception ex
                        ) {

                            ex.printStackTrace();

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Unable to open Login screen.",
                                    "CampusConnect",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
            );
        }
    }

    // =========================================================
    // GET NAME FROM EMAIL
    // =========================================================

    private String getNameFromEmail(
            String email
    ) {

        if (
                email == null
                        ||
                        email.trim().isEmpty()
        ) {

            return "Student";
        }

        int atIndex =
                email.indexOf(
                        "@"
                );

        String value =
                email.substring(
                        0,
                        atIndex > 0
                                ? atIndex
                                : email.length()
                );

        value =
                value.replace(
                        ".",
                        " "
                );

        value =
                value.replace(
                        "_",
                        " "
                );

        value =
                value.replace(
                        "-",
                        " "
                );

        String[] words =
                value.trim().split(
                        "\\s+"
                );

        StringBuilder result =
                new StringBuilder();

        for (
                String word
                : words
        ) {

            if (
                    word.isEmpty()
            ) {

                continue;
            }

            result.append(
                    Character.toUpperCase(
                            word.charAt(0)
                    )
            );

            if (
                    word.length() > 1
            ) {

                result.append(
                        word.substring(
                                1
                        ).toLowerCase()
                );
            }

            result.append(
                    " "
            );
        }

        String name =
                result.toString().trim();

        if (
                name.isEmpty()
        ) {

            return "Student";
        }

        return name;
    }

    // =========================================================
    // GET INITIALS
    // =========================================================

    private String getInitials(
            String name
    ) {

        if (
                name == null
                        ||
                        name.trim().isEmpty()
        ) {

            return "S";
        }

        String[] words =
                name.trim().split(
                        "\\s+"
                );

        if (
                words.length == 1
        ) {

            return String.valueOf(
                    Character.toUpperCase(
                            words[0].charAt(0)
                    )
            );
        }

        return ""
                + Character.toUpperCase(
                words[0].charAt(0)
        )
                + Character.toUpperCase(
                words[words.length - 1]
                        .charAt(0)
        );
    }

    // =========================================================
    // SHORT EMAIL
    // =========================================================

    private String shortenEmail(
            String email
    ) {

        if (
                email == null
                        ||
                        email.length() <= 25
        ) {

            return email;
        }

        return email.substring(
                0,
                22
        ) + "...";
    }

    // =========================================================
    // LOGOUT ICON
    // =========================================================

    private static final class LogoutIcon
            implements Icon {

        @Override
        public int getIconWidth() {

            return 22;
        }

        @Override
        public int getIconHeight() {

            return 22;
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    Color.WHITE
            );

            g2.setStroke(
                    new BasicStroke(
                            1.8f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawLine(
                    x + 4,
                    y + 4,
                    x + 4,
                    y + 18
            );

            g2.drawLine(
                    x + 4,
                    y + 4,
                    x + 12,
                    y + 4
            );

            g2.drawLine(
                    x + 4,
                    y + 18,
                    x + 12,
                    y + 18
            );

            g2.drawLine(
                    x + 9,
                    y + 11,
                    x + 19,
                    y + 11
            );

            g2.drawLine(
                    x + 15,
                    y + 7,
                    x + 19,
                    y + 11
            );

            g2.drawLine(
                    x + 15,
                    y + 15,
                    x + 19,
                    y + 11
            );

            g2.dispose();
        }
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (
                            Exception ignored
                    ) {
                    }

                    DashboardFrame dashboard =
                            new DashboardFrame(
                                    "aditya@gmail.com"
                            );

                    dashboard.setVisible(
                            true
                    );
                }
        );
    }
}
