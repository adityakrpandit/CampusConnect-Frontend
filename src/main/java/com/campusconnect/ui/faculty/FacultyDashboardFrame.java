package com.campusconnect.ui.faculty;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import com.campusconnect.config.DatabaseConnection;

public class FacultyDashboardFrame extends JFrame {

    private final String facultyName;
    private final String facultyEmail;

    private final Color NAVY = new Color(19, 57, 96);
    private final Color NAVY_LIGHT = new Color(31, 76, 118);
    private final Color BACKGROUND = new Color(243, 247, 251);
    private final Color TEAL = new Color(20, 150, 160);
    private final Color TEXT = new Color(58, 77, 96);
    private final Color MUTED = new Color(112, 130, 148);
    private final Color BORDER = new Color(224, 232, 239);
    private final Color WHITE = Color.WHITE;

    private JPanel contentPanel;
    private JLabel dashboardItem;
    private JLabel materialsItem;
    private JLabel announcementsItem;
    private JLabel eventsItem;
    private JLabel doubtsItem;
    private JLabel profileItem;
    private String facultyPhotoPath;

    public FacultyDashboardFrame(String facultyName, String email) {
        this.facultyName = facultyName == null || facultyName.trim().isEmpty()
                ? "Faculty Member" : facultyName.trim();
        this.facultyEmail = email == null ? "" : email.trim();
        this.facultyPhotoPath = loadFacultyPhotoPath();

        setTitle("CampusConnect | Faculty Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 650));
        setSize(1240, 780);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        mainPanel.add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout(0, 22));
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(28, 32, 28, 32));
        contentPanel.add(createHeader("Faculty Dashboard"), BorderLayout.NORTH);
        contentPanel.add(createDashboardContent(), BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(28, 18, 22, 18));

        // Brand block: use the same CampusConnect logo as the Student Dashboard.
        JPanel brandRow = new JPanel();
        brandRow.setOpaque(false);
        brandRow.setLayout(new BoxLayout(brandRow, BoxLayout.Y_AXIS));
        brandRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));

        JLabel logoMark = new JLabel();
        logoMark.setPreferredSize(new Dimension(180, 125));
        logoMark.setMinimumSize(new Dimension(180, 125));
        logoMark.setMaximumSize(new Dimension(180, 125));
        logoMark.setHorizontalAlignment(SwingConstants.CENTER);
        logoMark.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIcon = loadImageIcon("/campusconnect_logo.png", 180, 125);
        if (logoIcon != null) {
            logoMark.setIcon(logoIcon);
            logoMark.setOpaque(false);
        } else {
            logoMark.setText("CC");
            logoMark.setOpaque(true);
            logoMark.setBackground(TEAL);
            logoMark.setForeground(WHITE);
            logoMark.setFont(new Font("Arial", Font.BOLD, 26));
        }

        JLabel brand = new JLabel("CampusConnect");
        brand.setForeground(WHITE);
        brand.setFont(new Font("Arial", Font.BOLD, 22));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel portalSubLabel = new JLabel("Faculty Portal");
        portalSubLabel.setForeground(new Color(175, 200, 220));
        portalSubLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        portalSubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandRow.add(logoMark);
        brandRow.add(Box.createVerticalStrut(2));
        brandRow.add(brand);
        brandRow.add(Box.createVerticalStrut(5));
        brandRow.add(portalSubLabel);

        JLabel portalLabel = new JLabel("FACULTY WORKSPACE");
        portalLabel.setForeground(new Color(164, 198, 222));
        portalLabel.setFont(new Font("Arial", Font.BOLD, 11));
        portalLabel.setBorder(new EmptyBorder(26, 8, 14, 0));
        portalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dashboardItem = createSidebarItem("▦", "Dashboard", true);
        materialsItem = createSidebarItem("▤", "Study Materials", false);
        announcementsItem = createSidebarItem("✉", "Announcements", false);
        eventsItem = createSidebarItem("▣", "Events", false);
        doubtsItem = createSidebarItem("?", "Student Doubts", false);
        profileItem = createSidebarItem("●", "My Profile", false);

        dashboardItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDashboard();
            }
        });

        materialsItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openStudyMaterials();
            }
        });

        announcementsItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openAnnouncements();
            }
        });

        eventsItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openEvents();
            }
        });

        doubtsItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDoubts();
            }
        });

        profileItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openFacultyProfile();
            }
        });

        sidebar.add(brandRow);
        sidebar.add(portalLabel);
        sidebar.add(dashboardItem);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(materialsItem);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(announcementsItem);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(eventsItem);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(doubtsItem);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(profileItem);
        sidebar.add(Box.createVerticalGlue());

        JPanel profilePanel = new JPanel();
        profilePanel.setOpaque(false);
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePanel.setBorder(new EmptyBorder(14, 8, 15, 8));

        JLabel avatar = createFacultyAvatar(52);
        avatar.setPreferredSize(new Dimension(52, 52));
        avatar.setMaximumSize(new Dimension(52, 52));
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel profileName = new JLabel(facultyName);
        profileName.setForeground(WHITE);
        profileName.setFont(new Font("Arial", Font.BOLD, 14));
        profileName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel profileRole = new JLabel("Faculty Account");
        profileRole.setForeground(new Color(175, 200, 220));
        profileRole.setFont(new Font("Arial", Font.PLAIN, 12));
        profileRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        profilePanel.add(avatar);
        profilePanel.add(Box.createVerticalStrut(12));
        profilePanel.add(profileName);
        profilePanel.add(Box.createVerticalStrut(4));
        profilePanel.add(profileRole);

        JButton logoutButton = new JButton("↪   Logout");
        logoutButton.setForeground(WHITE);
        logoutButton.setBackground(TEAL);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new com.campusconnect.ui.login.LoginFrame();
            }
        });

        sidebar.add(profilePanel);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(logoutButton);

        return sidebar;
    }

    private JLabel createSidebarItem(String iconText, String text, boolean selected) {
        JLabel item = new JLabel(iconText + "    " + text);
        item.setForeground(selected ? WHITE : new Color(210, 224, 236));
        item.setFont(new Font("Arial", Font.BOLD, 14));
        item.setOpaque(true);
        item.setBackground(selected ? NAVY_LIGHT : NAVY);
        item.setBorder(new EmptyBorder(15, 14, 15, 10));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return item;
    }

    private JPanel createHeader(String pageTitle) {
        JPanel header = new JPanel(new BorderLayout(15, 10));
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(20, 24, 20, 24)
        ));

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(pageTitle);
        heading.setFont(new Font("Arial", Font.BOLD, 27));
        heading.setForeground(NAVY);

        JLabel subtitle = new JLabel("CampusConnect  /  Faculty Portal");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titles.add(heading);
        titles.add(Box.createVerticalStrut(7));
        titles.add(subtitle);

        JLabel status = new JLabel("●  ONLINE");
        status.setFont(new Font("Arial", Font.BOLD, 12));
        status.setForeground(new Color(32, 145, 87));
        status.setBorder(new EmptyBorder(0, 8, 0, 0));

        header.add(titles, BorderLayout.CENTER);
        header.add(status, BorderLayout.EAST);
        return header;
    }

    private JPanel createDashboardContent() {
        JPanel dashboard = new JPanel();
        dashboard.setOpaque(false);
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));

        JPanel welcomeCard = new JPanel(new BorderLayout(18, 12));
        welcomeCard.setBackground(WHITE);
        welcomeCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(26, 28, 26, 28)
        ));
        welcomeCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel welcomeText = new JPanel();
        welcomeText.setOpaque(false);
        welcomeText.setLayout(new BoxLayout(welcomeText, BoxLayout.Y_AXIS));

        JLabel greeting = new JLabel("Welcome back, " + facultyName + "!");
        greeting.setFont(new Font("Arial", Font.BOLD, 24));
        greeting.setForeground(NAVY);

        JLabel message = new JLabel("<html>Your faculty workspace is ready.<br>"
                + "Manage and share learning resources with students from one place.</html>");
        message.setFont(new Font("Arial", Font.PLAIN, 14));
        message.setForeground(MUTED);

        welcomeText.add(greeting);
        welcomeText.add(Box.createVerticalStrut(12));
        welcomeText.add(message);

        JPanel facultyIdentity = new JPanel();
        facultyIdentity.setOpaque(false);
        facultyIdentity.setLayout(new BoxLayout(facultyIdentity, BoxLayout.Y_AXIS));
        facultyIdentity.setBorder(new EmptyBorder(0, 12, 0, 0));

        JLabel heroPhoto = createFacultyAvatar(86);
        heroPhoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel facultyBadge = new JLabel("FACULTY ACCOUNT");
        facultyBadge.setOpaque(true);
        facultyBadge.setBackground(new Color(224, 246, 246));
        facultyBadge.setForeground(new Color(13, 119, 129));
        facultyBadge.setFont(new Font("Arial", Font.BOLD, 10));
        facultyBadge.setBorder(new EmptyBorder(7, 10, 7, 10));
        facultyBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        facultyIdentity.add(heroPhoto);
        facultyIdentity.add(Box.createVerticalStrut(9));
        facultyIdentity.add(facultyBadge);

        welcomeCard.add(welcomeText, BorderLayout.CENTER);
        welcomeCard.add(facultyIdentity, BorderLayout.EAST);

        JLabel sectionTitle = new JLabel("Your Workspace");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 20));
        sectionTitle.setForeground(NAVY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionSubtitle = new JLabel("Quick access to your faculty tools");
        sectionSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        sectionSubtitle.setForeground(MUTED);
        sectionSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel moduleCard = createModuleCard(
                "▤",
                "Study Materials Library",
                "<html>Browse resources shared by Admin and faculty.<br>"
                        + "Upload and manage your own study materials.</html>",
                "Open Study Materials",
                this::openStudyMaterials
        );

        JPanel announcementsCard = createModuleCard(
                "✉",
                "Announcements",
                "<html>View campus updates and publish announcements for students.<br>"
                        + "You can manage announcements created by your faculty account.</html>",
                "Open Announcements",
                this::openAnnouncements
        );

        JPanel eventsCard = createModuleCard(
                "▣",
                "Campus Events",
                "<html>View upcoming campus events and publish your own events.<br>"
                        + "You can manage events created by your faculty account.</html>",
                "Open Events",
                this::openEvents
        );

        JPanel doubtsCard = createModuleCard(
                "?",
                "Student Doubts",
                "<html>Review student questions and attached images.<br>"
                        + "Provide answers and update doubt status.</html>",
                "Open Student Doubts",
                this::openDoubts
        );

        JPanel infoCard = new JPanel(new BorderLayout(12, 8));
        infoCard.setBackground(new Color(233, 246, 250));
        infoCard.setBorder(new EmptyBorder(18, 20, 18, 20));
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoIcon = new JLabel("i");
        infoIcon.setOpaque(true);
        infoIcon.setBackground(TEAL);
        infoIcon.setForeground(WHITE);
        infoIcon.setFont(new Font("Arial", Font.BOLD, 15));
        infoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        infoIcon.setPreferredSize(new Dimension(30, 30));

        JPanel infoText = new JPanel();
        infoText.setOpaque(false);
        infoText.setLayout(new BoxLayout(infoText, BoxLayout.Y_AXIS));

        JLabel infoTitle = new JLabel("Quick tip");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 14));
        infoTitle.setForeground(NAVY);

        JLabel infoMessage = new JLabel(
                "Use Study Materials to access shared resources or publish your own learning content."
        );
        infoMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        infoMessage.setForeground(TEXT);

        infoText.add(infoTitle);
        infoText.add(Box.createVerticalStrut(5));
        infoText.add(infoMessage);

        infoCard.add(infoIcon, BorderLayout.WEST);
        infoCard.add(infoText, BorderLayout.CENTER);

        JLabel footer = new JLabel("CampusConnect • One Platform for Every Student Need");
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        dashboard.add(welcomeCard);
        dashboard.add(Box.createVerticalStrut(24));
        dashboard.add(sectionTitle);
        dashboard.add(Box.createVerticalStrut(5));
        dashboard.add(sectionSubtitle);
        dashboard.add(Box.createVerticalStrut(14));
        dashboard.add(moduleCard);
        dashboard.add(Box.createVerticalStrut(14));
        dashboard.add(announcementsCard);
        dashboard.add(Box.createVerticalStrut(14));
        dashboard.add(eventsCard);
        dashboard.add(Box.createVerticalStrut(14));
        dashboard.add(doubtsCard);
        dashboard.add(Box.createVerticalStrut(18));
        dashboard.add(infoCard);
        dashboard.add(Box.createVerticalGlue());
        dashboard.add(Box.createVerticalStrut(18));
        dashboard.add(footer);

        return dashboard;
    }

    private JPanel createModuleCard(String iconText, String titleText, String descriptionText,
                                    String buttonText, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(18, 10));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(22, 22, 22, 22)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel icon = new JLabel(iconText);
        icon.setOpaque(true);
        icon.setBackground(new Color(224, 246, 246));
        icon.setForeground(TEAL);
        icon.setFont(new Font("Arial", Font.BOLD, 27));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setPreferredSize(new Dimension(58, 58));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Arial", Font.BOLD, 17));
        title.setForeground(NAVY);

        JLabel description = new JLabel(descriptionText);
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        description.setForeground(MUTED);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(description);

        JButton button = new JButton(buttonText + "  →");
        button.setForeground(WHITE);
        button.setBackground(TEAL);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(190, 42));
        button.addActionListener(e -> action.run());

        card.add(icon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(button, BorderLayout.EAST);
        return card;
    }

    private void showDashboard() {
        setSelectedNavigation("dashboard");
        contentPanel.removeAll();
        contentPanel.add(createHeader("Faculty Dashboard"), BorderLayout.NORTH);
        contentPanel.add(createDashboardContent(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void openStudyMaterials() {
        setSelectedNavigation("materials");

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        FacultyStudyMaterialsFrame studyMaterialsFrame =
                new FacultyStudyMaterialsFrame(
                        facultyName,
                        facultyEmail
                );

        contentPanel.add(
                studyMaterialsFrame,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void openAnnouncements() {
        setSelectedNavigation("announcements");

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        FacultyAnnouncementsFrame announcementsFrame =
                new FacultyAnnouncementsFrame(
                        facultyName,
                        facultyEmail,
                        true,
                        this::showDashboard
                );

        contentPanel.add(
                announcementsFrame.getContentPane(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }



    private void openEvents() {
        setSelectedNavigation("events");

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        FacultyEventsFrame eventsFrame =
                new FacultyEventsFrame(
                        facultyName,
                        facultyEmail,
                        true,
                        this::showDashboard
                );

        contentPanel.add(
                eventsFrame.getContentPane(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void openDoubts() {
        setSelectedNavigation("doubts");

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        FacultyDoubtsFrame doubtsFrame =
                new FacultyDoubtsFrame(
                        facultyName,
                        facultyEmail,
                        true,
                        this::showDashboard
                );

        contentPanel.add(
                doubtsFrame.getContentPane(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void openFacultyProfile() {
        setSelectedNavigation("profile");

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        FacultyProfileFrame profilePanel =
                new FacultyProfileFrame(facultyEmail);

        contentPanel.add(profilePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setSelectedNavigation(String selected) {
        setNavigationItemStyle(dashboardItem, "dashboard".equals(selected));
        setNavigationItemStyle(materialsItem, "materials".equals(selected));
        setNavigationItemStyle(announcementsItem, "announcements".equals(selected));
        setNavigationItemStyle(eventsItem, "events".equals(selected));
        setNavigationItemStyle(doubtsItem, "doubts".equals(selected));
        setNavigationItemStyle(profileItem, "profile".equals(selected));
    }

    private void setNavigationItemStyle(JLabel item, boolean selected) {
        item.setBackground(selected ? NAVY_LIGHT : NAVY);
        item.setForeground(selected ? WHITE : new Color(210, 224, 236));
    }

    /** Loads the current faculty photo path from the faculty table. */
    private String loadFacultyPhotoPath() {
        if (facultyEmail == null || facultyEmail.isEmpty()) return null;
        String sql = "SELECT photo_path FROM faculty WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, facultyEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return resultSet.getString("photo_path");
            }
        } catch (Exception ex) {
            // Dashboard must still open if the optional photo column is not available.
            System.err.println("Faculty photo could not be loaded: " + ex.getMessage());
        }
        return null;
    }

    /** Creates a photo avatar; initials are shown if no photo has been saved yet. */
    private JLabel createFacultyAvatar(int size) {
        JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(size, size));
        avatar.setMinimumSize(new Dimension(size, size));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setVerticalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(224, 246, 246));
        avatar.setForeground(TEAL);
        avatar.setFont(new Font("Arial", Font.BOLD, Math.max(16, size / 3)));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(203, 231, 234), 2));

        ImageIcon photo = loadImageIconFromPath(facultyPhotoPath, size, size);
        if (photo != null) {
            avatar.setIcon(photo);
            avatar.setText("");
        } else {
            avatar.setText(getInitials(facultyName));
        }
        return avatar;
    }

    /** Loads an image stored under src/main/resources or another classpath resource. */
    private ImageIcon loadImageIcon(String resourcePath, int width, int height) {
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input == null) return null;
            BufferedImage image = ImageIO.read(input);
            if (image == null) return null;
            double scale = Math.min((double) width / image.getWidth(),
                    (double) height / image.getHeight());
            int scaledWidth = Math.max(1, (int) Math.round(image.getWidth() * scale));
            int scaledHeight = Math.max(1, (int) Math.round(image.getHeight() * scale));
            Image scaled = image.getScaledInstance(
                    scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            return null;
        }
    }

    /** Loads a faculty photo using its saved database path. */
    private ImageIcon loadImageIconFromPath(String path, int width, int height) {
        if (path == null || path.trim().isEmpty()) return null;
        try {
            File file = new File(path.trim());
            if (!file.isFile()) return null;
            BufferedImage image = ImageIO.read(file);
            if (image == null) return null;
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception ex) {
            return null;
        }
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return "F";
        }
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1))
                .toUpperCase();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        null,
                        "Please log in through LoginFrame to open the Faculty Dashboard "
                                + "with the authenticated faculty account.",
                        "CampusConnect",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
    }
}
