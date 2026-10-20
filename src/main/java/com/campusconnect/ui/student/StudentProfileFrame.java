
package com.campusconnect.ui.student;

import com.campusconnect.config.DatabaseConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * CampusConnect
 * Student Profile Frame
 *
 * IMPORTANT:
 * Constructor remains:
 *
 *      StudentProfileFrame(String email)
 *
 * Dashboard connection will NOT break.
 */
public class StudentProfileFrame extends JFrame {

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BACKGROUND =
            new Color(228, 243, 250);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color DARK_BLUE =
            new Color(18, 58, 95);

    private static final Color TEAL =
            new Color(12, 169, 181);

    private static final Color TEAL_DARK =
            new Color(8, 137, 150);

    private static final Color LIGHT_TEAL =
            new Color(222, 247, 250);

    private static final Color TEXT_DARK =
            new Color(30, 50, 70);

    private static final Color TEXT_GRAY =
            new Color(95, 115, 135);

    private static final Color BORDER =
            new Color(220, 230, 237);

    private static final Color SOFT_BLUE =
            new Color(235, 245, 252);

    private static final Color SOFT_GREEN =
            new Color(228, 249, 243);

    private static final Color SOFT_PURPLE =
            new Color(241, 236, 255);

    private static final Color SOFT_ORANGE =
            new Color(255, 245, 228);

    // =====================================================
    // STUDENT EMAIL
    // =====================================================

    private final String studentEmail;

    // =====================================================
    // COMPONENTS
    // =====================================================

    private JLabel nameLabel;
    private JLabel courseLabel;
    private JLabel emailLabel;

    private JLabel enrollmentValue;
    private JLabel fullNameValue;
    private JLabel courseValue;
    private JLabel semesterValue;
    private JLabel phoneValue;

    // Direct labels for Personal Information
    private JLabel phoneInfoValue;
    private JLabel emailInfoValue;

    // Dynamic values for the left profile summary
    private JLabel summarySemesterValue;
    private JLabel summaryCourseValue;
    private JLabel summaryEnrollmentValue;

    private JLabel profileImageLabel;

    private JTextArea bioArea;
    private JLabel skillsValue;

    private JLabel githubValue;
    private JLabel linkedinValue;

    private JLabel createdAtValue;

    // =====================================================
    // STUDENT DATA
    // =====================================================

    private String loadedName = "";
    private String loadedEmail = "";
    private String loadedCourse = "";
    private String loadedSemester = "";
    private String loadedPhone = "";
    private String loadedEnrollment = "";
    private String loadedPhoto = "";
    private String loadedBio = "";
    private String loadedSkills = "";
    private String loadedGithub = "";
    private String loadedLinkedin = "";
    private String loadedCreatedAt = "";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public StudentProfileFrame(String email) {

        this.studentEmail =
                email == null
                        ? ""
                        : email.trim();

        // =================================================
        // FRAME SETTINGS
        // =================================================

        setTitle(
                "CampusConnect - My Profile"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(1000, 700)
        );

        setLayout(
                new BorderLayout()
        );

        // =================================================
        // MAIN PANEL
        // =================================================

        JPanel mainPanel =
                new CampusBackgroundPanel();

        mainPanel.setLayout(
                new BorderLayout()
        );

        // =================================================
        // HEADER
        // =================================================

        JPanel headerPanel =
                createHeader();

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =================================================
        // CONTENT
        // =================================================

        JPanel contentPanel =
                new JPanel(
                        new GridBagLayout()
                );

        contentPanel.setOpaque(false);

        contentPanel.setBorder(
                new EmptyBorder(
                        10,
                        18,
                        18,
                        18
                )
        );

        // =================================================
        // LEFT PROFILE CARD
        // =================================================

        JPanel profileCard =
                createProfileCard();

        // =================================================
        // RIGHT SIDE
        // =================================================

        JPanel rightPanel =
                new JPanel();

        rightPanel.setLayout(
                new BoxLayout(
                        rightPanel,
                        BoxLayout.Y_AXIS
                )
        );

        rightPanel.setOpaque(false);

        // Personal information
        JPanel personalCard =
                createPersonalInformationCard();

        // Additional information
        JPanel additionalCard =
                createAdditionalInformationCard();

        rightPanel.add(
                personalCard
        );

        rightPanel.add(
                Box.createVerticalStrut(12)
        );

        rightPanel.add(
                additionalCard
        );

        // =================================================
        // GRID BAG
        // =================================================

        GridBagConstraints leftGbc =
                new GridBagConstraints();

        leftGbc.gridx = 0;
        leftGbc.gridy = 0;

        leftGbc.weightx = 0.35;
        leftGbc.weighty = 1.0;

        leftGbc.fill =
                GridBagConstraints.BOTH;

        leftGbc.anchor =
                GridBagConstraints.NORTHWEST;

        leftGbc.insets =
                new Insets(
                        0,
                        0,
                        0,
                        8
                );

        contentPanel.add(
                profileCard,
                leftGbc
        );

        GridBagConstraints rightGbc =
                new GridBagConstraints();

        rightGbc.gridx = 1;
        rightGbc.gridy = 0;

        rightGbc.weightx = 0.65;
        rightGbc.weighty = 1.0;

        rightGbc.fill =
                GridBagConstraints.BOTH;

        rightGbc.anchor =
                GridBagConstraints.NORTHWEST;

        rightGbc.insets =
                new Insets(
                        0,
                        8,
                        0,
                        0
                );

        contentPanel.add(
                rightPanel,
                rightGbc
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // ADD MAIN PANEL
        // =================================================

        add(
                mainPanel
        );

        // =================================================
        // LOAD STUDENT DATA
        // =================================================

        loadStudentData();

        // =================================================
        // WINDOW
        // =================================================

        setSize(
                1200,
                780
        );

        setLocationRelativeTo(
                null
        );

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setVisible(
                true
        );
    }

    // =====================================================
    // HEADER
    // =====================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(255, 255, 255, 232)
        );
        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );

        // -------------------------------------------------
        // LEFT
        // -------------------------------------------------

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setOpaque(false);

        titlePanel.setBorder(
                new EmptyBorder(
                        18,
                        24,
                        16,
                        10
                )
        );

        JLabel brand =
                new JLabel(
                        "CAMPUSCONNECT  •  STUDENT PORTAL"
                );

        brand.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        brand.setForeground(
                TEAL_DARK
        );

        titlePanel.add(
                brand
        );

        titlePanel.add(
                Box.createVerticalStrut(4)
        );

        JLabel title =
                new JLabel(
                        "My Profile"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        29
                )
        );

        title.setForeground(
                DARK_BLUE
        );

        JLabel subtitle =
                new JLabel(
                        "View and manage your personal and academic information."
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

        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(
                subtitle
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // -------------------------------------------------
        // RIGHT
        // -------------------------------------------------

        JPanel rightHeader =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                15,
                                18
                        )
                );

        rightHeader.setOpaque(false);

        JLabel welcome =
                new JLabel(
                        "Welcome, Student"
                );

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        welcome.setForeground(
                DARK_BLUE
        );

        JButton closeButton =
                createHeaderButton(
                        "Close"
                );

        closeButton.addActionListener(
                e -> dispose()
        );

        rightHeader.add(
                welcome
        );

        rightHeader.add(
                closeButton
        );

        header.add(
                rightHeader,
                BorderLayout.EAST
        );

        return header;
    }

    // =====================================================
    // LEFT PROFILE CARD
    // =====================================================

    private JPanel createProfileCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                0,
                                0,
                                15,
                                0
                        )
                )
        );

        // =================================================
        // PROFILE TOP AREA
        // =================================================

        JPanel top =
                new ProfileHeroPanel();

        top.setLayout(
                new BorderLayout()
        );

        top.setPreferredSize(
                new Dimension(
                        350,
                        230
                )
        );

        // Decorative area
        JPanel decoration =
                new JPanel();

        decoration.setOpaque(false);

        decoration.setPreferredSize(
                new Dimension(
                        1,
                        25
                )
        );

        top.add(
                decoration,
                BorderLayout.NORTH
        );

        // Profile photo
        profileImageLabel =
                new JLabel(
                        createInitialImage(
                                "S",
                                145
                        )
                );

        profileImageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        profileImageLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        JPanel imageWrapper =
                new JPanel(
                        new GridBagLayout()
                );

        imageWrapper.setOpaque(false);

        imageWrapper.add(
                profileImageLabel
        );

        top.add(
                imageWrapper,
                BorderLayout.CENTER
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        // =================================================
        // PROFILE DETAILS
        // =================================================

        JPanel profileDetails =
                new JPanel();

        profileDetails.setLayout(
                new BoxLayout(
                        profileDetails,
                        BoxLayout.Y_AXIS
                )
        );

        profileDetails.setBackground(
                WHITE
        );

        profileDetails.setBorder(
                new EmptyBorder(
                        12,
                        20,
                        0,
                        20
                )
        );

        // Name
        nameLabel =
                new JLabel(
                        "Student Name",
                        SwingConstants.CENTER
                );

        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        nameLabel.setForeground(
                DARK_BLUE
        );

        nameLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        profileDetails.add(
                nameLabel
        );

        // Course
        courseLabel =
                new JLabel(
                        "BCA Student",
                        SwingConstants.CENTER
                );

        courseLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        courseLabel.setForeground(
                TEXT_GRAY
        );

        courseLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        profileDetails.add(
                Box.createVerticalStrut(3)
        );

        profileDetails.add(
                courseLabel
        );

        // Active badge
        JLabel activeBadge =
                new JLabel(
                        "●  Active Student"
                );

        activeBadge.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        activeBadge.setForeground(
                new Color(
                        15,
                        155,
                        100
                )
        );

        activeBadge.setOpaque(true);

        activeBadge.setBackground(
                new Color(
                        225,
                        250,
                        238
                )
        );

        activeBadge.setBorder(
                new EmptyBorder(
                        6,
                        12,
                        6,
                        12
                )
        );

        activeBadge.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        profileDetails.add(
                Box.createVerticalStrut(9)
        );

        profileDetails.add(
                activeBadge
        );

        // =================================================
        // SUMMARY CARDS
        // =================================================

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                8,
                                0
                        )
                );

        summaryPanel.setBackground(
                WHITE
        );

        summaryPanel.setBorder(
                new EmptyBorder(
                        20,
                        0,
                        15,
                        0
                )
        );

        JPanel semesterCard =
                createSmallStatCard(
                        "-",
                        "Semester",
                        SOFT_BLUE
                );

        JPanel courseCard =
                createSmallStatCard(
                        "-",
                        "Course",
                        SOFT_ORANGE
                );

        JPanel enrollmentCard =
                createSmallStatCard(
                        "-",
                        "Enrollment",
                        SOFT_PURPLE
                );

        summaryPanel.add(
                semesterCard
        );

        summaryPanel.add(
                courseCard
        );

        summaryPanel.add(
                enrollmentCard
        );

        profileDetails.add(
                summaryPanel
        );

        // Divider
        JSeparator separator =
                new JSeparator();

        separator.setForeground(
                BORDER
        );

        profileDetails.add(
                separator
        );

        // =================================================
        // CONTACT INFORMATION
        // =================================================

        JPanel contactPanel =
                new JPanel();

        contactPanel.setLayout(
                new BoxLayout(
                        contactPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contactPanel.setBackground(
                WHITE
        );

        contactPanel.setBorder(
                new EmptyBorder(
                        12,
                        4,
                        5,
                        4
                )
        );

        emailLabel =
                createContactLabel(
                        "✉",
                        "Email",
                        "Loading..."
                );

        contactPanel.add(
                emailLabel
        );

        phoneValue =
                createContactLabel(
                        "☎",
                        "Phone",
                        "Loading..."
                );

        contactPanel.add(
                Box.createVerticalStrut(10)
        );

        contactPanel.add(
                phoneValue
        );

        createdAtValue =
                createContactLabel(
                        "▣",
                        "Joined",
                        "Loading..."
                );

        contactPanel.add(
                Box.createVerticalStrut(10)
        );

        contactPanel.add(
                createdAtValue
        );

        profileDetails.add(
                contactPanel
        );

        card.add(
                profileDetails,
                BorderLayout.CENTER
        );

        return card;
    }

    // =====================================================
    // PERSONAL INFORMATION CARD
    // =====================================================

    private JPanel createPersonalInformationCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                15,
                                12
                        )
                )
        );

        // Header
        JPanel header =
                createSectionHeader(
                        "●",
                        "Personal Information",
                        "Edit Profile"
                );

        card.add(
                header,
                BorderLayout.NORTH
        );

        // Details
        JPanel details =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                12,
                                10
                        )
                );

        details.setBackground(
                WHITE
        );

        details.setBorder(
                new EmptyBorder(
                        12,
                        5,
                        0,
                        5
                )
        );

        // Enrollment
        enrollmentValue =
                createInformationValue(
                        "Loading..."
                );

        details.add(
                createInformationItem(
                        "▣",
                        "Enrollment No.",
                        enrollmentValue
                )
        );

        // Course
        courseValue =
                createInformationValue(
                        "Loading..."
                );

        details.add(
                createInformationItem(
                        "◆",
                        "Course",
                        courseValue
                )
        );

        // Full name
        fullNameValue =
                createInformationValue(
                        "Loading..."
                );

        details.add(
                createInformationItem(
                        "●",
                        "Full Name",
                        fullNameValue
                )
        );

        // Semester
        semesterValue =
                createInformationValue(
                        "Loading..."
                );

        details.add(
                createInformationItem(
                        "▥",
                        "Semester",
                        semesterValue
                )
        );

        // Phone
        phoneInfoValue =
                createInformationValue(
                        "Loading..."
                );

        details.add(
                createInformationItem(
                        "☎",
                        "Phone Number",
                        phoneInfoValue
                )
        );

        // Email
        emailInfoValue =
                createInformationValue(
                        "Loading..."
                );

        details.add(
                createInformationItem(
                        "✉",
                        "Email",
                        emailInfoValue
                )
        );

        card.add(
                details,
                BorderLayout.CENTER
        );

        return card;
    }

    // =====================================================
    // ADDITIONAL INFORMATION CARD
    // =====================================================

    private JPanel createAdditionalInformationCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );

        JPanel header =
                createSimpleSectionHeader(
                        "★",
                        "About & Skills"
                );

        card.add(
                header,
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        content.setBackground(
                WHITE
        );

        content.setBorder(
                new EmptyBorder(
                        10,
                        5,
                        5,
                        5
                )
        );

        // =================================================
        // BIO
        // =================================================

        JPanel bioPanel =
                new JPanel(
                        new BorderLayout()
                );

        bioPanel.setBackground(
                new Color(
                        248,
                        251,
                        253
                )
        );

        bioPanel.setBorder(
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

        JLabel bioTitle =
                new JLabel(
                        "About Me"
                );

        bioTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        bioTitle.setForeground(
                DARK_BLUE
        );

        bioArea =
                new JTextArea(
                        "Loading..."
                );

        bioArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        bioArea.setForeground(
                TEXT_GRAY
        );

        bioArea.setBackground(
                new Color(
                        248,
                        251,
                        253
                )
        );

        bioArea.setLineWrap(
                true
        );

        bioArea.setWrapStyleWord(
                true
        );

        bioArea.setEditable(
                false
        );

        bioArea.setBorder(
                new EmptyBorder(
                        8,
                        0,
                        0,
                        0
                )
        );

        bioPanel.add(
                bioTitle,
                BorderLayout.NORTH
        );

        bioPanel.add(
                bioArea,
                BorderLayout.CENTER
        );

        content.add(
                bioPanel
        );

        // =================================================
        // SKILLS
        // =================================================

        JPanel skillsPanel =
                new JPanel(
                        new BorderLayout()
                );

        skillsPanel.setBackground(
                new Color(
                        248,
                        251,
                        253
                )
        );

        skillsPanel.setBorder(
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

        JLabel skillsTitle =
                new JLabel(
                        "Skills"
                );

        skillsTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        skillsTitle.setForeground(
                DARK_BLUE
        );

        skillsValue =
                new JLabel(
                        "Loading..."
                );

        skillsValue.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        skillsValue.setForeground(
                TEXT_GRAY
        );

        skillsValue.setVerticalAlignment(
                SwingConstants.TOP
        );

        skillsValue.setBorder(
                new EmptyBorder(
                        8,
                        0,
                        0,
                        0
                )
        );

        skillsPanel.add(
                skillsTitle,
                BorderLayout.NORTH
        );

        skillsPanel.add(
                skillsValue,
                BorderLayout.CENTER
        );

        // Links
        JPanel links =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                5
                        )
                );

        links.setBackground(
                new Color(
                        248,
                        251,
                        253
                )
        );

        githubValue =
                createLinkLabel(
                        "GitHub"
                );

        linkedinValue =
                createLinkLabel(
                        "LinkedIn"
                );

        links.add(
                githubValue
        );

        links.add(
                linkedinValue
        );

        skillsPanel.add(
                links,
                BorderLayout.SOUTH
        );

        content.add(
                skillsPanel
        );

        card.add(
                content,
                BorderLayout.CENTER
        );

        return card;
    }

    // =====================================================
    // CREATE CARD
    // =====================================================

    private JPanel createCard() {

        JPanel panel =
                new RoundedPanel(
                        24,
                        WHITE
                );

        panel.setOpaque(false);

        return panel;
    }

    // =====================================================
    // SECTION HEADER
    // =====================================================

    private JPanel createSectionHeader(
            String icon,
            String title,
            String buttonText
    ) {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                LIGHT_TEAL
        );

        header.setBorder(
                new EmptyBorder(
                        7,
                        10,
                        7,
                        8
                )
        );

        JLabel titleLabel =
                new JLabel(
                        icon + "  " + title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        titleLabel.setForeground(
                DARK_BLUE
        );

        header.add(
                titleLabel,
                BorderLayout.WEST
        );

        JButton editButton =
                new JButton(
                        buttonText
                );

        editButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        editButton.setForeground(
                WHITE
        );

        editButton.setBackground(
                TEAL
        );

        editButton.setFocusPainted(
                false
        );

        editButton.setBorder(
                BorderFactory.createEmptyBorder(
                        7,
                        12,
                        7,
                        12
                )
        );

        editButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        /*
         * Existing project connection is not changed.
         *
         * Edit functionality can be connected later
         * with EditStudentProfileFrame.
         */

        editButton.addActionListener(
                e -> {

                    try {

                        new EditStudentProfileFrame(
                                studentEmail
                        );

                    } catch (Exception ex) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Edit Profile module is not available yet.",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
        );

        header.add(
                editButton,
                BorderLayout.EAST
        );

        return header;
    }

    // =====================================================
    // SIMPLE SECTION HEADER
    // =====================================================

    private JPanel createSimpleSectionHeader(
            String icon,
            String title
    ) {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                LIGHT_TEAL
        );

        header.setBorder(
                new EmptyBorder(
                        7,
                        10,
                        7,
                        10
                )
        );

        JLabel label =
                new JLabel(
                        icon + "  " + title
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                DARK_BLUE
        );

        header.add(
                label,
                BorderLayout.WEST
        );

        return header;
    }

    // =====================================================
    // INFORMATION ITEM
    // =====================================================

    private JPanel createInformationItem(
            String icon,
            String title,
            JLabel value
    ) {

        JPanel item =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        item.setBackground(
                WHITE
        );

        JPanel iconBox =
                new JPanel(
                        new GridBagLayout()
                );

        iconBox.setBackground(
                SOFT_BLUE
        );

        iconBox.setPreferredSize(
                new Dimension(
                        38,
                        42
                )
        );

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        iconLabel.setForeground(
                TEAL_DARK
        );

        iconBox.add(
                iconLabel
        );

        item.add(
                iconBox,
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

        text.setBackground(
                WHITE
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
                DARK_BLUE
        );

        value.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        value.setForeground(
                TEXT_GRAY
        );

        value.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        230,
                                        236,
                                        241
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(
                value
        );

        item.add(
                text,
                BorderLayout.CENTER
        );

        return item;
    }

    // =====================================================
    // INFORMATION VALUE
    // =====================================================

    private JLabel createInformationValue(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setOpaque(
                true
        );

        label.setBackground(
                new Color(
                        241,
                        246,
                        249
                )
        );

        return label;
    }

    // =====================================================
    // SMALL STAT CARD
    // =====================================================

    private JPanel createSmallStatCard(
            String mainText,
            String title,
            Color background
    ) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                background
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        235,
                                        240
                                )
                        ),
                        new EmptyBorder(
                                8,
                                5,
                                8,
                                5
                        )
                )
        );

        JLabel main =
                new JLabel(
                        mainText,
                        SwingConstants.CENTER
                );

        // Keep references so these values can be populated from the database.
        if ("Semester".equals(title)) {
            summarySemesterValue = main;
        } else if ("Course".equals(title)) {
            summaryCourseValue = main;
        } else if ("Enrollment".equals(title)) {
            summaryEnrollmentValue = main;
        }

        main.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        main.setToolTipText(
                mainText
        );

        main.setForeground(
                DARK_BLUE
        );

        main.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );
        main.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        24
                )
        );

        JLabel small =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        small.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        9
                )
        );

        small.setForeground(
                TEXT_GRAY
        );

        small.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                main
        );

        panel.add(
                Box.createVerticalStrut(3)
        );

        panel.add(
                small
        );

        return panel;
    }

    // =====================================================
    // OVERVIEW BOX
    // =====================================================

    private JPanel createOverviewBox(
            String icon,
            String title,
            String value,
            Color background
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        panel.setBackground(
                background
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JLabel iconLabel =
                new JLabel(
                        icon,
                        SwingConstants.CENTER
                );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        iconLabel.setForeground(
                TEAL_DARK
        );

        iconLabel.setOpaque(
                true
        );

        iconLabel.setBackground(
                new Color(
                        255,
                        255,
                        255,
                        150
                )
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        40,
                        40
                )
        );

        panel.add(
                iconLabel,
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
                        16
                )
        );

        valueLabel.setForeground(
                DARK_BLUE
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(3)
        );

        text.add(
                valueLabel
        );

        panel.add(
                text,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =====================================================
    // CONTACT LABEL
    // =====================================================

    private JLabel createContactLabel(
            String icon,
            String title,
            String value
    ) {

        JLabel label =
                new JLabel(
                        "<html>"
                                + "<b>"
                                + icon
                                + "  "
                                + value
                                + "</b>"
                                + "<br>"
                                + "<font color='#718096' size='2'>"
                                + title
                                + "</font>"
                                + "</html>"
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        label.setForeground(
                TEXT_DARK
        );

        return label;
    }

    // =====================================================
    // LINK LABEL
    // =====================================================

    private JLabel createLinkLabel(
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
                TEAL_DARK
        );

        label.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        label.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        195,
                                        225,
                                        230
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                9,
                                5,
                                9
                        )
                )
        );

        label.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        String url = null;

                        if (
                                text.equals("GitHub")
                                        && loadedGithub != null
                        ) {

                            url =
                                    loadedGithub;

                        } else if (
                                text.equals("LinkedIn")
                                        && loadedLinkedin != null
                        ) {

                            url =
                                    loadedLinkedin;
                        }

                        openUrl(url);
                    }
                }
        );

        return label;
    }

    // =====================================================
    // HEADER BUTTON
    // =====================================================

    private JButton createHeaderButton(
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
                        11
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                TEAL
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        7,
                        15,
                        7,
                        15
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
    // CREATE PROFILE INITIAL
    // =====================================================

    private ImageIcon createInitialImage(
            String text,
            int size
    ) {

        BufferedImage image =
                new BufferedImage(
                        size,
                        size,
                        BufferedImage.TYPE_INT_ARGB
                );

        Graphics2D g =
                image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setColor(
                TEAL
        );

        g.fillOval(
                3,
                3,
                size - 6,
                size - 6
        );

        g.setColor(
                WHITE
        );

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        size / 3
                )
        );

        FontMetrics fm =
                g.getFontMetrics();

        int x =
                (size - fm.stringWidth(text))
                        / 2;

        int y =
                (size - fm.getHeight())
                        / 2
                        + fm.getAscent();

        g.drawString(
                text,
                x,
                y
        );

        g.dispose();

        return new ImageIcon(
                image
        );
    }

    // =====================================================
    // LOAD PROFILE IMAGE
    // =====================================================

    private void loadProfileImage(
            String photoPath,
            String studentName
    ) {

        if (
                photoPath == null
                        || photoPath.trim().isEmpty()
        ) {

            profileImageLabel.setIcon(
                    createInitialImage(
                            getInitials(studentName),
                            155
                    )
            );

            return;
        }

        try {

            File file =
                    new File(
                            photoPath
                    );

            if (!file.exists()) {

                profileImageLabel.setIcon(
                        createInitialImage(
                                getInitials(
                                        studentName
                                ),
                                145
                        )
                );

                return;
            }

            BufferedImage image =
                    ImageIO.read(
                            file
                    );

            if (image == null) {

                throw new Exception(
                        "Invalid image"
                );
            }

            Image scaled =
                    image.getScaledInstance(
                            145,
                            145,
                            Image.SCALE_SMOOTH
                    );

            profileImageLabel.setIcon(
                    new ImageIcon(
                            scaled
                    )
            );

        } catch (Exception e) {

            profileImageLabel.setIcon(
                    createInitialImage(
                            getInitials(studentName),
                            155
                    )
            );
        }
    }

    // =====================================================
    // INITIALS
    // =====================================================

    private String getInitials(
            String name
    ) {

        if (
                name == null
                        || name.trim().isEmpty()
        ) {

            return "S";
        }

        String[] parts =
                name.trim().split(
                        "\\s+"
                );

        if (parts.length == 1) {

            return parts[0]
                    .substring(
                            0,
                            1
                    )
                    .toUpperCase();
        }

        return (
                parts[0].substring(0, 1)
                        + parts[parts.length - 1]
                        .substring(0, 1)
        ).toUpperCase();
    }

    // =====================================================
    // LOAD STUDENT DATA
    // =====================================================

    private void loadStudentData() {

        /*
         * We are using the logged-in student's email.
         *
         * Therefore:
         *
         * Student A -> Student A profile
         * Student B -> Student B profile
         * Student C -> Student C profile
         *
         * No hard-coded student details.
         */

        String sql =
                "SELECT " +
                        "student_id, " +
                        "name, " +
                        "email, " +
                        "course, " +
                        "semester, " +
                        "phone, " +
                        "enrollment_no, " +
                        "profile_photo, " +
                        "bio, " +
                        "skills_summary, " +
                        "github_url, " +
                        "linkedin_url, " +
                        "created_at " +
                        "FROM students " +
                        "WHERE email = ?";

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
                    studentEmail
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    // =====================================
                    // GET DATA
                    // =====================================

                    loadedName =
                            safeString(
                                    resultSet.getString(
                                            "name"
                                    )
                            );

                    loadedEmail =
                            safeString(
                                    resultSet.getString(
                                            "email"
                                    )
                            );

                    loadedCourse =
                            safeString(
                                    resultSet.getString(
                                            "course"
                                    )
                            );

                    loadedPhone =
                            safeString(
                                    resultSet.getString(
                                            "phone"
                                    )
                            );

                    loadedEnrollment =
                            safeString(
                                    resultSet.getString(
                                            "enrollment_no"
                                    )
                            );

                    loadedPhoto =
                            safeString(
                                    resultSet.getString(
                                            "profile_photo"
                                    )
                            );

                    loadedBio =
                            safeString(
                                    resultSet.getString(
                                            "bio"
                                    )
                            );

                    loadedSkills =
                            safeString(
                                    resultSet.getString(
                                            "skills_summary"
                                    )
                            );

                    loadedGithub =
                            safeString(
                                    resultSet.getString(
                                            "github_url"
                                    )
                            );

                    loadedLinkedin =
                            safeString(
                                    resultSet.getString(
                                            "linkedin_url"
                                    )
                            );

                    // =====================================
                    // SEMESTER
                    // =====================================

                    Object semesterObject =
                            resultSet.getObject(
                                    "semester"
                            );

                    if (
                            semesterObject != null
                    ) {

                        loadedSemester =
                                String.valueOf(
                                        semesterObject
                                );

                    } else {

                        loadedSemester =
                                "Not available";
                    }

                    // =====================================
                    // CREATED DATE
                    // =====================================

                    Timestamp createdAt =
                            resultSet.getTimestamp(
                                    "created_at"
                            );

                    if (createdAt != null) {

                        loadedCreatedAt =
                                new SimpleDateFormat(
                                        "dd MMM yyyy"
                                ).format(
                                        createdAt
                                );

                    } else {

                        loadedCreatedAt =
                                "Not available";
                    }

                    // =====================================
                    // UPDATE UI
                    // =====================================

                    updateUIWithStudentData();

                } else {

                    showStudentNotFound();
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

    // =====================================================
    // UPDATE UI
    // =====================================================

    private void updateUIWithStudentData() {

        // Left profile
        nameLabel.setText(
                loadedName.isEmpty()
                        ? "Student"
                        : loadedName
        );

        courseLabel.setText(
                loadedCourse.isEmpty()
                        ? "Student"
                        : loadedCourse + " Student"
        );

        emailLabel.setText(
                "<html>"
                        + "<b>✉  "
                        + safeDisplay(
                        loadedEmail
                )
                        + "</b>"
                        + "<br>"
                        + "<font color='#718096' size='2'>"
                        + "Email"
                        + "</font>"
                        + "</html>"
        );

        phoneValue.setText(
                "<html>"
                        + "<b>☎  "
                        + safeDisplay(
                        loadedPhone
                )
                        + "</b>"
                        + "<br>"
                        + "<font color='#718096' size='2'>"
                        + "Phone"
                        + "</font>"
                        + "</html>"
        );

        createdAtValue.setText(
                "<html>"
                        + "<b>▣  "
                        + safeDisplay(
                        loadedCreatedAt
                )
                        + "</b>"
                        + "<br>"
                        + "<font color='#718096' size='2'>"
                        + "Joined"
                        + "</font>"
                        + "</html>"
        );

        // Personal information
        enrollmentValue.setText(
                safeDisplay(
                        loadedEnrollment
                )
        );

        fullNameValue.setText(
                safeDisplay(
                        loadedName
                )
        );

        courseValue.setText(
                safeDisplay(
                        loadedCourse
                )
        );

        semesterValue.setText(
                safeDisplay(
                        loadedSemester
                )
        );

        // Personal Information: update the actual labels directly.
        phoneInfoValue.setText(
                safeDisplay(
                        loadedPhone
                )
        );

        emailInfoValue.setText(
                safeDisplay(
                        loadedEmail
                )
        );

        // Left profile summary: use the real database values.
        summarySemesterValue.setText(
                safeDisplay(
                        loadedSemester
                )
        );

        summaryCourseValue.setText(
                safeDisplay(
                        loadedCourse
                )
        );

        summaryEnrollmentValue.setText(
                safeDisplay(
                        loadedEnrollment
                )
        );

        // Bio
        bioArea.setText(
                loadedBio.isEmpty()
                        ? "No bio added yet."
                        : loadedBio
        );

        // Skills
        skillsValue.setText(
                "<html>"
                        + formatSkills(
                        loadedSkills
                )
                        + "</html>"
        );

        // Links
        if (
                loadedGithub.isEmpty()
        ) {

            githubValue.setText(
                    "GitHub"
            );

            githubValue.setForeground(
                    TEXT_GRAY
            );

        } else {

            githubValue.setText(
                    "GitHub ↗"
            );

            githubValue.setForeground(
                    TEAL_DARK
            );
        }

        if (
                loadedLinkedin.isEmpty()
        ) {

            linkedinValue.setText(
                    "LinkedIn"
            );

            linkedinValue.setForeground(
                    TEXT_GRAY
            );

        } else {

            linkedinValue.setText(
                    "LinkedIn ↗"
            );

            linkedinValue.setForeground(
                    TEAL_DARK
            );
        }

        // Profile image
        loadProfileImage(
                loadedPhoto,
                loadedName
        );

        repaint();
    }

    // =====================================================
    // FORMAT SKILLS
    // =====================================================

    private String formatSkills(
            String skills
    ) {

        if (
                skills == null
                        || skills.trim().isEmpty()
        ) {

            return "No skills added yet.";
        }

        String[] skillArray =
                skills.split(
                        ","
                );

        StringBuilder builder =
                new StringBuilder();

        for (
                String skill :
                skillArray
        ) {

            String trimmed =
                    skill.trim();

            if (
                    !trimmed.isEmpty()
            ) {

                builder.append(
                        "<span style='background:#E8F8FA;"
                                + "padding:5px;'>"
                                + trimmed
                                + "</span> "
                );
            }
        }

        return builder.toString();
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safeString(
            String value
    ) {

        return value == null
                ? ""
                : value.trim();
    }

    // =====================================================
    // SAFE DISPLAY
    // =====================================================

    private String safeDisplay(
            String value
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            return "Not available";
        }

        return value;
    }

    // =====================================================
    // OPEN URL
    // =====================================================

    private void openUrl(
            String url
    ) {

        if (
                url == null
                        || url.trim().isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "This profile link is not available.",
                    "Link Not Available",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        try {

            if (
                    Desktop.isDesktopSupported()
            ) {

                Desktop.getDesktop().browse(
                        new URI(
                                url
                        )
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        url,
                        "Profile Link",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open link:\n"
                            + url,
                    "Link Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // STUDENT NOT FOUND
    // =====================================================

    private void showStudentNotFound() {

        nameLabel.setText(
                "Student Not Found"
        );

        courseLabel.setText(
                "CampusConnect Student"
        );

        emailLabel.setText(
                "<html>"
                        + "<b>✉  "
                        + safeDisplay(
                        studentEmail
                )
                        + "</b>"
                        + "<br>"
                        + "<font color='#718096' size='2'>"
                        + "Email"
                        + "</font>"
                        + "</html>"
        );

        JOptionPane.showMessageDialog(
                this,
                "Student details not found for:\n"
                        + studentEmail,
                "Student Not Found",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =====================================================
    // ROUNDED PANEL
    // =====================================================

    private static class RoundedPanel extends JPanel {

        private final int radius;
        private final Color backgroundColor;

        RoundedPanel(int radius, Color backgroundColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(backgroundColor);
            g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1,
                    radius, radius
            );
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =====================================================
    // CAMPUSCONNECT BACKGROUND
    // =====================================================

    private static class CampusBackgroundPanel extends JPanel {

        CampusBackgroundPanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(239, 249, 253),
                    w, h, new Color(216, 239, 248)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, w, h);

            // Soft teal shapes inspired by the Login screen.
            g2.setColor(new Color(20, 166, 180, 28));
            g2.fillOval(-180, h / 2 - 220, 560, 560);
            g2.fillOval(w - 360, -170, 500, 500);

            g2.setColor(new Color(20, 55, 95, 18));
            g2.fillOval(w - 260, h - 230, 420, 420);

            g2.setColor(new Color(255, 255, 255, 100));
            g2.fillOval(w / 2 - 150, -230, 500, 500);

            // Decorative dots.
            g2.setColor(new Color(20, 166, 180, 45));
            for (int x = 28; x < 180; x += 22) {
                for (int y = 150; y < 260; y += 22) {
                    g2.fillOval(x, y, 5, 5);
                }
            }

            g2.dispose();
        }
    }

    // =====================================================
    // PROFILE HERO BACKGROUND
    // =====================================================

    private static class ProfileHeroPanel extends JPanel {

        ProfileHeroPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(7, 83, 111),
                    w, h, new Color(19, 166, 180)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(255, 255, 255, 35));
            g2.fillOval(w - 220, -150, 380, 380);

            g2.setColor(new Color(255, 255, 255, 22));
            g2.fillOval(-120, h - 130, 300, 300);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () ->
                        new StudentProfileFrame(
                                "aditya@gmail.com"
                        )
        );
    }
}

