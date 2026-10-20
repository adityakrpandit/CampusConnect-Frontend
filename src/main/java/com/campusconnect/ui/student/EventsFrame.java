package com.campusconnect.ui.student;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventsFrame extends JFrame {

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color NAVY = new Color(24, 61, 105);
    private static final Color DARK_NAVY = new Color(15, 45, 82);
    private static final Color TEAL = new Color(21, 158, 171);
    private static final Color LIGHT_TEAL = new Color(231, 248, 250);
    private static final Color BACKGROUND = new Color(244, 248, 252);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT = new Color(31, 48, 69);
    private static final Color MUTED = new Color(100, 120, 140);
    private static final Color BORDER = new Color(220, 229, 238);
    private static final Color GREEN = new Color(28, 156, 103);
    private static final Color ORANGE = new Color(231, 145, 39);
    private static final Color RED = new Color(205, 75, 75);

    // =====================================================
    // COMPONENTS
    // =====================================================

    private JPanel eventsContainer;
    private JTextField searchField;
    private JLabel totalEventsLabel;
    private JLabel resultLabel;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String EVENTS_API_URL =
            "https://campusconnect-backend-grn0.onrender.com/api/events";

    private List<ApiEvent> allEvents = new ArrayList<>();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EventsFrame() {

        setTitle("CampusConnect - Events");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setSize(1200, 750);

        // =================================================
        // MAIN FRAME
        // =================================================

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        // =================================================
        // HEADER
        // =================================================

        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // =================================================
        // EVENTS AREA
        // =================================================

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(18, 28, 18, 28));

        // =================================================
        // TOOLBAR
        // =================================================

        JPanel toolbar = createToolbar();
        contentPanel.add(toolbar, BorderLayout.NORTH);

        // =================================================
        // EVENTS CONTAINER
        // =================================================

        eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(BACKGROUND);
        eventsContainer.setBorder(new EmptyBorder(5, 0, 15, 0));

        JScrollPane scrollPane = new JScrollPane(eventsContainer);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(BACKGROUND);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================
        // BOTTOM BAR
        // =================================================

        JPanel bottomBar = createBottomBar();
        contentPanel.add(bottomBar, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // =================================================
        // LOAD EVENTS
        // =================================================

        loadEvents();

        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
    }

    // =====================================================
    // HEADER
    // =====================================================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0,
                                BORDER
                        ),
                        new EmptyBorder(22, 30, 22, 30)
                )
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);

        JLabel brandLabel = new JLabel("CAMPUSCONNECT");
        brandLabel.setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        brandLabel.setForeground(TEAL);

        JLabel titleLabel = new JLabel("Campus Events");
        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 30)
        );
        titleLabel.setForeground(DARK_NAVY);

        JLabel subtitleLabel = new JLabel(
                "Discover upcoming events, activities and important campus programs."
        );
        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );
        subtitleLabel.setForeground(MUTED);

        titlePanel.add(brandLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitleLabel);

        // =================================================
        // EVENT COUNT CARD
        // =================================================

        JPanel countCard = new JPanel();
        countCard.setLayout(new BoxLayout(countCard, BoxLayout.Y_AXIS));
        countCard.setBackground(LIGHT_TEAL);
        countCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(190, 230, 234)
                        ),
                        new EmptyBorder(12, 22, 12, 22)
                )
        );

        JLabel countTitle = new JLabel("TOTAL EVENTS");
        countTitle.setFont(
                new Font("Arial", Font.BOLD, 11)
        );
        countTitle.setForeground(TEAL);
        countTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalEventsLabel = new JLabel("0");
        totalEventsLabel.setFont(
                new Font("Arial", Font.BOLD, 27)
        );
        totalEventsLabel.setForeground(DARK_NAVY);
        totalEventsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countSubtitle = new JLabel("Campus events");
        countSubtitle.setFont(
                new Font("Arial", Font.PLAIN, 11)
        );
        countSubtitle.setForeground(MUTED);
        countSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        countCard.add(countTitle);
        countCard.add(Box.createVerticalStrut(2));
        countCard.add(totalEventsLabel);
        countCard.add(countSubtitle);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(countCard, BorderLayout.EAST);

        return header;
    }

    // =====================================================
    // TOOLBAR
    // =====================================================

    private JPanel createToolbar() {

        JPanel toolbar = new JPanel(new BorderLayout(15, 0));
        toolbar.setBackground(WHITE);
        toolbar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(14, 16, 14, 16)
                )
        );

        // =================================================
        // LEFT SIDE
        // =================================================

        JPanel leftPanel = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                0,
                0
        ));

        leftPanel.setBackground(WHITE);

        JLabel eventsLabel = new JLabel("All Events");
        eventsLabel.setFont(
                new Font("Arial", Font.BOLD, 17)
        );
        eventsLabel.setForeground(TEXT);

        resultLabel = new JLabel("  ");
        resultLabel.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );
        resultLabel.setForeground(MUTED);

        leftPanel.add(eventsLabel);
        leftPanel.add(resultLabel);

        // =================================================
        // SEARCH
        // =================================================

        JPanel rightPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                8,
                0
        ));

        rightPanel.setBackground(WHITE);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(270, 38));
        searchField.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(0, 12, 0, 12)
                )
        );

        searchField.setToolTipText(
                "Search event name, venue or description"
        );

        JButton refreshButton = createButton(
                "Refresh",
                NAVY,
                WHITE
        );

        refreshButton.setPreferredSize(
                new Dimension(105, 38)
        );

        refreshButton.addActionListener(
                e -> loadEvents()
        );

        rightPanel.add(searchField);
        rightPanel.add(refreshButton);

        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);

        // =================================================
        // SEARCH LISTENER
        // =================================================

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        filterEvents();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        filterEvents();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        filterEvents();
                    }
                }
        );

        return toolbar;
    }

    // =====================================================
    // BOTTOM BAR
    // =====================================================

    private JPanel createBottomBar() {

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(BACKGROUND);
        bottomBar.setBorder(
                new EmptyBorder(12, 0, 0, 0)
        );

        JLabel infoLabel = new JLabel(
                "Click any event card to view complete details."
        );

        infoLabel.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );
        infoLabel.setForeground(MUTED);

        JButton closeButton = createButton(
                "Close",
                DARK_NAVY,
                WHITE
        );

        closeButton.setPreferredSize(
                new Dimension(105, 38)
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        bottomBar.add(infoLabel, BorderLayout.WEST);
        bottomBar.add(closeButton, BorderLayout.EAST);

        return bottomBar;
    }

    // =====================================================
    // LOAD EVENTS
    // =====================================================

    private void loadEvents() {

        resultLabel.setText("(Loading events...)");

        SwingWorker<List<ApiEvent>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ApiEvent> doInBackground() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(EVENTS_API_URL))
                        .timeout(Duration.ofSeconds(90))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    throw new IOException("Events API returned HTTP " + response.statusCode()
                            + ": " + response.body());
                }

                JsonNode root = objectMapper.readTree(response.body());
                if (!root.isArray()) {
                    throw new IOException("Unexpected Events API response format.");
                }

                List<ApiEvent> events = new ArrayList<>();
                for (JsonNode node : root) {
                    String name = textValue(node, "event_name", "eventName", "title");
                    String date = textValue(node, "event_date", "eventDate", "date");
                    String venue = textValue(node, "venue", "location");
                    String description = textValue(node, "description", "details");
                    String imagePath = textValue(node, "image_path", "imagePath");
                    events.add(new ApiEvent(name, date, venue, description, imagePath));
                }
                return events;
            }

            @Override
            protected void done() {
                try {
                    allEvents = get();
                    totalEventsLabel.setText(String.valueOf(allEvents.size()));
                    filterEvents();
                    System.out.println("Loaded " + allEvents.size() + " events from Render API");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultLabel.setText("(Unable to load events)");
                    showError("Unable to load events from Render API.\n\n"
                            + "Please check your internet connection or try Refresh again.\n\n"
                            + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private String textValue(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode value = node.get(fieldName);
            if (value != null && !value.isNull()) {
                return value.asText("");
            }
        }
        return "";
    }

    private static class ApiEvent {
        private final String eventName;
        private final String eventDate;
        private final String venue;
        private final String description;
        private final String imagePath;

        private ApiEvent(String eventName, String eventDate, String venue,
                         String description, String imagePath) {
            this.eventName = eventName;
            this.eventDate = eventDate;
            this.venue = venue;
            this.description = description;
            this.imagePath = imagePath;
        }

        public String getEventName() { return eventName; }
        public String getEventDate() { return eventDate; }
        public String getVenue() { return venue; }
        public String getDescription() { return description; }
        public String getImagePath() { return imagePath; }
    }

    // =====================================================
    // DISPLAY EVENTS
    // =====================================================

    private void displayEvents(List<ApiEvent> events) {

        eventsContainer.removeAll();

        resultLabel.setText(
                "(" + events.size() + " event" +
                        (events.size() == 1 ? "" : "s") + ")"
        );

        if (events.isEmpty()) {

            JPanel emptyPanel = createEmptyState();

            eventsContainer.add(emptyPanel);

        } else {

            for (ApiEvent event : events) {

                JPanel card = createEventCard(event);

                eventsContainer.add(card);

                eventsContainer.add(
                        Box.createVerticalStrut(12)
                );
            }
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    // =====================================================
    // SEARCH / FILTER
    // =====================================================

    private void filterEvents() {

        String query = searchField.getText()
                .trim()
                .toLowerCase();

        if (query.isEmpty()) {

            displayEvents(allEvents);
            return;
        }

        List<ApiEvent> filteredEvents = new ArrayList<>();

        for (ApiEvent event : allEvents) {

            String name = safeString(
                    event.getEventName()
            ).toLowerCase();

            String venue = safeString(
                    event.getVenue()
            ).toLowerCase();

            String description = safeString(
                    event.getDescription()
            ).toLowerCase();

            if (name.contains(query)
                    || venue.contains(query)
                    || description.contains(query)) {

                filteredEvents.add(event);
            }
        }

        displayEvents(filteredEvents);
    }

    // =====================================================
    // EVENT CARD
    // =====================================================

    private JPanel createEventCard(ApiEvent event) {

        JPanel card = new JPanel(new BorderLayout(18, 0));

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        170
                )
        );

        // =================================================
        // LEFT ICON
        // =================================================

        JPanel iconPanel = createEventIconPanel(
                event.getEventName()
        );

        card.add(
                iconPanel,
                BorderLayout.WEST
        );

        // =================================================
        // CENTER
        // =================================================

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );
        centerPanel.setBackground(WHITE);

        // Event name
        JLabel eventName = new JLabel(
                safeString(event.getEventName())
        );

        eventName.setFont(
                new Font("Arial", Font.BOLD, 19)
        );
        eventName.setForeground(DARK_NAVY);

        // Status
        JPanel statusPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                )
        );

        statusPanel.setBackground(WHITE);

        JLabel statusLabel = createStatusLabel(
                event.getEventDate()
        );

        statusPanel.add(statusLabel);

        // Description
        String description = safeString(
                event.getDescription()
        );

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:650px;'>" +
                        escapeHtml(
                                description
                        ) +
                        "</div></html>"
        );

        descriptionLabel.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );
        descriptionLabel.setForeground(MUTED);

        // Date and venue
        JPanel metaPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        18,
                        0
                )
        );

        metaPanel.setBackground(WHITE);

        JLabel dateLabel = new JLabel(
                "Date: " +
                        safeString(
                                event.getEventDate()
                        )
        );

        dateLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        dateLabel.setForeground(TEAL);

        JLabel venueLabel = new JLabel(
                "Venue: " +
                        safeString(
                                event.getVenue()
                        )
        );

        venueLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        venueLabel.setForeground(MUTED);

        metaPanel.add(dateLabel);
        metaPanel.add(venueLabel);

        centerPanel.add(eventName);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(statusPanel);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(descriptionLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(metaPanel);

        card.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // RIGHT SIDE
        // =================================================

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(
                new BoxLayout(
                        rightPanel,
                        BoxLayout.Y_AXIS
                )
        );
        rightPanel.setBackground(WHITE);

        JLabel viewIcon = new JLabel("›");
        viewIcon.setFont(
                new Font("Arial", Font.BOLD, 30)
        );
        viewIcon.setForeground(TEAL);
        viewIcon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel viewLabel = new JLabel("View Details");
        viewLabel.setFont(
                new Font("Arial", Font.BOLD, 11)
        );
        viewLabel.setForeground(TEAL);
        viewLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(viewIcon);
        rightPanel.add(Box.createVerticalStrut(2));
        rightPanel.add(viewLabel);
        rightPanel.add(Box.createVerticalGlue());

        card.add(
                rightPanel,
                BorderLayout.EAST
        );

        // =================================================
        // CLICK EVENT
        // =================================================

        MouseAdapter clickListener = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                card.setBackground(
                        new Color(248, 252, 254)
                );

                updateChildBackground(
                        card,
                        new Color(248, 252, 254)
                );

                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        new Color(150, 215, 222)
                                ),
                                new EmptyBorder(
                                        18,
                                        18,
                                        18,
                                        18
                                )
                        )
                );

                card.setCursor(
                        new Cursor(Cursor.HAND_CURSOR)
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                card.setBackground(WHITE);

                updateChildBackground(
                        card,
                        WHITE
                );

                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        BORDER
                                ),
                                new EmptyBorder(
                                        18,
                                        18,
                                        18,
                                        18
                                )
                        )
                );

                card.setCursor(
                        new Cursor(Cursor.DEFAULT_CURSOR)
                );
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 1) {

                    showEventDetails(event);
                }
            }
        };

        addMouseListenerRecursively(
                card,
                clickListener
        );

        return card;
    }

    // =====================================================
    // EVENT ICON
    // =====================================================

    private JPanel createEventIconPanel(
            String eventName
    ) {

        JPanel iconPanel = new JPanel(
                new GridBagLayout()
        );

        iconPanel.setPreferredSize(
                new Dimension(78, 105)
        );

        String name = safeString(
                eventName
        ).toLowerCase();

        String icon = "★";
        Color iconColor = NAVY;

        if (name.contains("event")
                || name.contains("fest")
                || name.contains("function")) {

            icon = "★";
            iconColor = TEAL;

        } else if (name.contains("sport")
                || name.contains("game")
                || name.contains("cricket")
                || name.contains("football")) {

            icon = "●";
            iconColor = ORANGE;

        } else if (name.contains("seminar")
                || name.contains("workshop")
                || name.contains("lecture")) {

            icon = "▣";
            iconColor = NAVY;

        } else if (name.contains("cultural")
                || name.contains("dance")
                || name.contains("music")) {

            icon = "♫";
            iconColor = new Color(140, 82, 180);

        } else if (name.contains("holiday")
                || name.contains("celebration")) {

            icon = "◆";
            iconColor = GREEN;

        } else if (name.contains("exam")
                || name.contains("test")) {

            icon = "✓";
            iconColor = RED;
        }

        iconPanel.setBackground(
                new Color(
                        iconColor.getRed(),
                        iconColor.getGreen(),
                        iconColor.getBlue(),
                        25
                )
        );

        JLabel iconLabel = new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        31
                )
        );

        iconLabel.setForeground(iconColor);

        iconPanel.add(iconLabel);

        return iconPanel;
    }

    // =====================================================
    // STATUS LABEL
    // =====================================================

    private JLabel createStatusLabel(
            Object eventDate
    ) {

        String status = getEventStatus(eventDate);

        Color background;
        Color foreground;

        if ("TODAY".equals(status)) {

            background = new Color(255, 241, 218);
            foreground = ORANGE;

        } else if ("UPCOMING".equals(status)) {

            background = new Color(225, 247, 237);
            foreground = GREEN;

        } else {

            background = new Color(239, 242, 246);
            foreground = MUTED;
        }

        JLabel label = new JLabel(
                "  " + status + "  "
        );

        label.setFont(
                new Font("Arial", Font.BOLD, 10)
        );

        label.setForeground(foreground);
        label.setOpaque(true);
        label.setBackground(background);

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        4,
                        5,
                        4,
                        5
                )
        );

        return label;
    }

    // =====================================================
    // EVENT STATUS
    // =====================================================

    private String getEventStatus(Object eventDate) {

        if (eventDate == null) {
            return "UPCOMING";
        }

        String dateText = eventDate.toString();

        try {

            LocalDate eventDateValue =
                    LocalDate.parse(
                            dateText
                    );

            LocalDate today =
                    LocalDate.now();

            if (eventDateValue.isEqual(today)) {

                return "TODAY";

            } else if (eventDateValue.isAfter(today)) {

                return "UPCOMING";

            } else {

                return "PAST";
            }

        } catch (DateTimeParseException e) {

            return "UPCOMING";
        }
    }

    // =====================================================
    // EVENT DETAILS POPUP
    // =====================================================

    private void showEventDetails(ApiEvent event) {

        JDialog dialog = new JDialog(
                this,
                "Event Details",
                true
        );

        dialog.setSize(
                620,
                520
        );

        dialog.setLocationRelativeTo(this);

        JPanel main = new JPanel(
                new BorderLayout()
        );

        main.setBackground(BACKGROUND);

        // =================================================
        // POPUP HEADER
        // =================================================

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(NAVY);

        header.setBorder(
                new EmptyBorder(
                        22,
                        25,
                        22,
                        25
                )
        );

        JLabel headerTitle = new JLabel(
                "EVENT DETAILS"
        );

        headerTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        headerTitle.setForeground(
                new Color(170, 235, 238)
        );

        JLabel title = new JLabel(
                safeString(
                        event.getEventName()
                )
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        title.setForeground(WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );
        titlePanel.setBackground(NAVY);

        titlePanel.add(headerTitle);
        titlePanel.add(
                Box.createVerticalStrut(5)
        );
        titlePanel.add(title);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        main.add(
                header,
                BorderLayout.NORTH
        );

        // =================================================
        // DETAILS
        // =================================================

        JPanel details = new JPanel();
        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        details.setBackground(WHITE);
        details.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        details.add(
                createDetailRow(
                        "DATE",
                        safeString(
                                event.getEventDate()
                        )
                )
        );

        details.add(
                Box.createVerticalStrut(15)
        );

        details.add(
                createDetailRow(
                        "VENUE",
                        safeString(
                                event.getVenue()
                        )
                )
        );

        details.add(
                Box.createVerticalStrut(20)
        );

        JLabel descriptionTitle =
                new JLabel("DESCRIPTION");

        descriptionTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        descriptionTitle.setForeground(TEAL);

        details.add(
                descriptionTitle
        );

        details.add(
                Box.createVerticalStrut(8)
        );

        JTextArea descriptionArea =
                new JTextArea(
                        safeString(
                                event.getDescription()
                        )
                );

        descriptionArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        descriptionArea.setForeground(TEXT);
        descriptionArea.setBackground(
                new Color(248, 250, 252)
        );

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        descriptionArea.setBorder(
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

        JScrollPane descriptionScroll =
                new JScrollPane(
                        descriptionArea
                );

        descriptionScroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        details.add(
                descriptionScroll
        );

        main.add(
                details,
                BorderLayout.CENTER
        );

        // =================================================
        // CLOSE
        // =================================================

        JPanel bottom = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        bottom.setBackground(WHITE);
        bottom.setBorder(
                new EmptyBorder(
                        5,
                        20,
                        15,
                        20
                )
        );

        JButton closeButton =
                createButton(
                        "Close",
                        NAVY,
                        WHITE
                );

        closeButton.setPreferredSize(
                new Dimension(
                        105,
                        38
                )
        );

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        bottom.add(closeButton);

        main.add(
                bottom,
                BorderLayout.SOUTH
        );

        dialog.add(main);

        dialog.setVisible(true);
    }

    // =====================================================
    // DETAIL ROW
    // =====================================================

    private JPanel createDetailRow(
            String title,
            String value
    ) {

        JPanel panel = new JPanel(
                new BorderLayout(
                        15,
                        0
                )
        );

        panel.setBackground(WHITE);

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setPreferredSize(
                new Dimension(
                        80,
                        35
                )
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        titleLabel.setForeground(TEAL);

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        valueLabel.setForeground(TEXT);

        panel.add(
                titleLabel,
                BorderLayout.WEST
        );

        panel.add(
                valueLabel,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =====================================================
    // EMPTY STATE
    // =====================================================

    private JPanel createEmptyState() {

        JPanel panel = new JPanel();
        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(WHITE);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                50,
                                30,
                                50,
                                30
                        )
                )
        );

        panel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel icon = new JLabel("○");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        40
                )
        );

        icon.setForeground(
                new Color(170, 185, 200)
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title = new JLabel(
                "No Events Found"
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(TEXT);

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message = new JLabel(
                "There are no events matching your search."
        );

        message.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        message.setForeground(MUTED);

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(icon);
        panel.add(
                Box.createVerticalStrut(8)
        );
        panel.add(title);
        panel.add(
                Box.createVerticalStrut(5)
        );
        panel.add(message);

        return panel;
    }

    // =====================================================
    // BUTTON
    // =====================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(foreground);
        button.setBackground(background);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safeString(Object value) {

        if (value == null) {
            return "Not specified";
        }

        String text = value.toString().trim();

        if (text.isEmpty()) {
            return "Not specified";
        }

        return text;
    }

    // =====================================================
    // HTML ESCAPE
    // =====================================================

    private String escapeHtml(String text) {

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    // =====================================================
    // ADD MOUSE LISTENER TO ALL CHILDREN
    // =====================================================

    private void addMouseListenerRecursively(
            Component component,
            MouseAdapter listener
    ) {

        component.addMouseListener(listener);

        if (component instanceof Container) {

            Component[] children =
                    ((Container) component)
                            .getComponents();

            for (Component child : children) {

                addMouseListenerRecursively(
                        child,
                        listener
                );
            }
        }
    }

    // =====================================================
    // UPDATE BACKGROUND RECURSIVELY
    // =====================================================

    private void updateChildBackground(
            Component component,
            Color color
    ) {

        if (component instanceof JPanel) {

            component.setBackground(color);
        }

        if (component instanceof Container) {

            Component[] children =
                    ((Container) component)
                            .getComponents();

            for (Component child : children) {

                updateChildBackground(
                        child,
                        color
                );
            }
        }
    }

    // =====================================================
    // ERROR MESSAGE
    // =====================================================

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "CampusConnect",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                EventsFrame::new
        );
    }
}
