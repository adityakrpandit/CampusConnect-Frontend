package com.campusconnect.ui.student;

import com.campusconnect.model.StudyMaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StudyMaterialsFrame extends JFrame {

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BACKGROUND =
            new Color(232, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color DARK_BLUE =
            new Color(22, 55, 100);

    private static final Color TEAL =
            new Color(25, 160, 175);

    private static final Color BLUE =
            new Color(45, 105, 220);

    private static final Color GREEN =
            new Color(30, 155, 95);

    private static final Color TEXT_DARK =
            new Color(25, 40, 60);

    private static final Color TEXT_GRAY =
            new Color(95, 110, 130);

    private static final Color BORDER =
            new Color(210, 225, 233);

    // =====================================================
    // COMPONENTS
    // =====================================================

    private JTable materialsTable;

    private DefaultTableModel tableModel;

    private JLabel totalMaterialsLabel;

    private JLabel selectedMaterialLabel;

    private JButton openButton;

    private JButton refreshButton;

    private JLabel sourceStatusLabel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public StudyMaterialsFrame() {

        setTitle(
                "CampusConnect - Study Materials"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(
                        1050,
                        680
                )
        );

        setSize(
                1150,
                720
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(
                BACKGROUND
        );

        setLayout(
                new BorderLayout()
        );

        // =================================================
        // HEADER
        // =================================================

        add(
                createHeader(),
                BorderLayout.NORTH
        );

        // =================================================
        // CENTER
        // =================================================

        add(
                createCenterPanel(),
                BorderLayout.CENTER
        );

        // =================================================
        // LOAD DATA
        // =================================================

        loadStudyMaterials();

        setVisible(true);
    }

    // =====================================================
    // HEADER
    // =====================================================

    private JPanel createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                WHITE
        );

        headerPanel.setBorder(
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
                                16,
                                30
                        )
                )
        );

        // =================================================
        // LEFT
        // =================================================

        JPanel leftPanel =
                new JPanel();

        leftPanel.setLayout(
                new BoxLayout(
                        leftPanel,
                        BoxLayout.X_AXIS
                )
        );

        leftPanel.setBackground(
                WHITE
        );

        JPanel iconBox =
                createIconBox(
                        "▤",
                        TEAL,
                        62
                );

        leftPanel.add(
                iconBox
        );

        leftPanel.add(
                Box.createHorizontalStrut(
                        14
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(
                WHITE
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

        JLabel titleLabel =
                new JLabel(
                        "Study Materials"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        29
                )
        );

        titleLabel.setForeground(
                TEXT_DARK
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Access notes, PDFs and online learning resources"
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(
                TEXT_GRAY
        );

        titlePanel.add(
                smallTitle
        );

        titlePanel.add(
                Box.createVerticalStrut(2)
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

        leftPanel.add(
                titlePanel
        );

        headerPanel.add(
                leftPanel,
                BorderLayout.WEST
        );

        // =================================================
        // RIGHT TOTAL
        // =================================================

        JPanel totalCard =
                new JPanel();

        totalCard.setLayout(
                new BoxLayout(
                        totalCard,
                        BoxLayout.Y_AXIS
                )
        );

        totalCard.setBackground(
                new Color(
                        232,
                        247,
                        249
                )
        );

        totalCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        190,
                                        225,
                                        230
                                )
                        ),
                        new EmptyBorder(
                                8,
                                18,
                                8,
                                18
                        )
                )
        );

        JLabel totalTitle =
                new JLabel(
                        "TOTAL MATERIALS"
                );

        totalTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        totalTitle.setForeground(
                TEAL
        );

        totalMaterialsLabel =
                new JLabel(
                        "0"
                );

        totalMaterialsLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        totalMaterialsLabel.setForeground(
                TEXT_DARK
        );

        totalCard.add(
                totalTitle
        );

        totalCard.add(
                totalMaterialsLabel
        );

        headerPanel.add(
                totalCard,
                BorderLayout.EAST
        );

        return headerPanel;
    }

    // =====================================================
    // ICON BOX
    // =====================================================

    private JPanel createIconBox(
            String icon,
            Color background,
            int size
    ) {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                background
        );

        panel.setPreferredSize(
                new Dimension(
                        size,
                        size
                )
        );

        panel.setMinimumSize(
                new Dimension(
                        size,
                        size
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        size,
                        size
                )
        );

        JLabel label =
                new JLabel(
                        icon
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        label.setForeground(
                WHITE
        );

        panel.add(
                label
        );

        return panel;
    }

    // =====================================================
    // CENTER PANEL
    // =====================================================

    private JPanel createCenterPanel() {

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        centerPanel.setBackground(
                BACKGROUND
        );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        16,
                        30,
                        25,
                        30
                )
        );

        // =================================================
        // TOP TOOLBAR
        // =================================================

        JPanel toolbar =
                new JPanel(
                        new BorderLayout()
                );

        toolbar.setBackground(
                BACKGROUND
        );

        JPanel information =
                new JPanel();

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );

        information.setBackground(
                BACKGROUND
        );

        JLabel heading =
                new JLabel(
                        "Available Learning Resources"
                );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        heading.setForeground(
                DARK_BLUE
        );

        JLabel instruction =
                new JLabel(
                        "Select a resource and click Open Resource, or double-click a row."
                );

        instruction.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        instruction.setForeground(
                TEXT_GRAY
        );

        information.add(
                heading
        );

        information.add(
                Box.createVerticalStrut(
                        3
                )
        );

        information.add(
                instruction
        );

        toolbar.add(
                information,
                BorderLayout.WEST
        );

        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setBackground(
                BACKGROUND
        );

        refreshButton =
                createButton(
                        "↻  Refresh",
                        BLUE,
                        105
                );

        openButton =
                createButton(
                        "↗  Open Resource",
                        TEAL,
                        145
                );

        buttons.add(
                refreshButton
        );

        buttons.add(
                openButton
        );

        toolbar.add(
                buttons,
                BorderLayout.EAST
        );

        centerPanel.add(
                toolbar,
                BorderLayout.NORTH
        );

        // =================================================
        // TABLE
        // =================================================

        String[] columns = {
                "ID",
                "TITLE",
                "SUBJECT",
                "DESCRIPTION",
                "RESOURCE"
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

        materialsTable =
                new JTable(
                        tableModel
                );

        materialsTable.setRowHeight(
                48
        );

        materialsTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        materialsTable.setForeground(
                TEXT_DARK
        );

        materialsTable.setBackground(
                WHITE
        );

        materialsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        materialsTable.setGridColor(
                new Color(
                        220,
                        228,
                        235
                )
        );

        materialsTable.setSelectionBackground(
                new Color(
                        218,
                        241,
                        245
                )
        );

        materialsTable.setSelectionForeground(
                DARK_BLUE
        );

        materialsTable.setShowGrid(
                true
        );

        materialsTable.setFillsViewportHeight(
                true
        );

        // =================================================
        // HEADER
        // =================================================

        materialsTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        materialsTable
                .getTableHeader()
                .setBackground(
                        DARK_BLUE
                );

        materialsTable
                .getTableHeader()
                .setForeground(
                        WHITE
                );

        materialsTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                42
                        )
                );

        materialsTable
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        // =================================================
        // COLUMN WIDTHS
        // =================================================

        materialsTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(
                        55
                );

        materialsTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(
                        220
                );

        materialsTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(
                        140
                );

        materialsTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(
                        340
                );

        materialsTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(
                        300
                );

        // =================================================
        // ID RENDERER
        // =================================================

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        materialsTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        centerRenderer
                );

        // =================================================
        // RESOURCE RENDERER
        // =================================================

        materialsTable
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new ResourceRenderer()
                );

        // =================================================
        // SCROLL
        // =================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        materialsTable
                );

        scrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                0,
                                0,
                                0
                        )
                )
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =================================================
        // BOTTOM STATUS
        // =================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setBackground(
                WHITE
        );

        bottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        selectedMaterialLabel =
                new JLabel(
                        "No material selected"
                );

        selectedMaterialLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        selectedMaterialLabel.setForeground(
                TEXT_GRAY
        );

        sourceStatusLabel =
                new JLabel(
                        "🔗 Select a resource to open it"
                );

        sourceStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        sourceStatusLabel.setForeground(
                TEAL
        );

        bottomPanel.add(
                selectedMaterialLabel,
                BorderLayout.WEST
        );

        bottomPanel.add(
                sourceStatusLabel,
                BorderLayout.EAST
        );

        centerPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // =================================================
        // ACTIONS
        // =================================================

        refreshButton.addActionListener(
                e -> loadStudyMaterials()
        );

        openButton.addActionListener(
                e -> openSelectedMaterial()
        );

        // =================================================
        // ROW SELECTION
        // =================================================

        materialsTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (
                                    !e.getValueIsAdjusting()
                                            &&
                                            materialsTable
                                                    .getSelectedRow()
                                                    >= 0
                            ) {

                                updateSelectionInfo();
                            }
                        }
                );

        // =================================================
        // DOUBLE CLICK
        // =================================================

        materialsTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (
                                e.getClickCount() == 2
                                        &&
                                        SwingUtilities
                                                .isLeftMouseButton(e)
                        ) {

                            openSelectedMaterial();
                        }
                    }
                }
        );

        return centerPanel;
    }

    // =====================================================
    // BUTTON
    // =====================================================

    private JButton createButton(
            String text,
            Color background,
            int width
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
                        9,
                        15,
                        9,
                        15
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        width,
                        38
                )
        );

        return button;
    }

    // =====================================================
    // RESOURCE RENDERER
    // =====================================================

    private static class ResourceRenderer
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
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );

            String source =
                    value == null
                            ? ""
                            : value.toString().trim();

            label.setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            8
                    )
            );

            if (
                    source.startsWith(
                            "http://"
                    )
                            ||
                            source.startsWith(
                                    "https://"
                            )
            ) {

                label.setText(
                        "🔗  Online Resource"
                );

                label.setForeground(
                        new Color(
                                25,
                                105,
                                180
                        )
                );

                label.setToolTipText(
                        source
                );

            } else {

                label.setText(
                        "📄  Local File"
                );

                label.setForeground(
                        new Color(
                                55,
                                100,
                                75
                        )
                );

                label.setToolTipText(
                        source
                );
            }

            if (isSelected) {

                label.setBackground(
                        new Color(
                                218,
                                241,
                                245
                        )
                );

            } else {

                label.setBackground(
                        WHITE
                );
            }

            return label;
        }
    }

    // =====================================================
    // UPDATE SELECTION INFO
    // =====================================================

    private void updateSelectionInfo() {

        int row =
                materialsTable.getSelectedRow();

        if (row < 0) {

            selectedMaterialLabel.setText(
                    "No material selected"
            );

            sourceStatusLabel.setText(
                    "🔗 Select a resource to open it"
            );

            return;
        }

        String title =
                String.valueOf(
                        tableModel.getValueAt(
                                row,
                                1
                        )
                );

        String source =
                String.valueOf(
                        tableModel.getValueAt(
                                row,
                                4
                        )
                );

        selectedMaterialLabel.setText(
                "Selected:  " + title
        );

        if (
                source.startsWith(
                        "http://"
                )
                        ||
                        source.startsWith(
                                "https://"
                        )
        ) {

            sourceStatusLabel.setText(
                    "🔗 Online resource ready to open"
            );

        } else {

            sourceStatusLabel.setText(
                    "📄 Local file ready to open"
            );
        }
    }

    // =====================================================
    // OPEN SELECTED MATERIAL
    // =====================================================

    private void openSelectedMaterial() {

        int row =
                materialsTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a study material first.",
                    "No Material Selected",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        Object sourceObject =
                tableModel.getValueAt(
                        row,
                        4
                );

        if (sourceObject == null) {

            showSourceUnavailable();

            return;
        }

        String source =
                sourceObject
                        .toString()
                        .trim();

        if (source.isEmpty()) {

            showSourceUnavailable();

            return;
        }

        openSource(
                source
        );
    }

    // =====================================================
    // SOURCE UNAVAILABLE
    // =====================================================

    private void showSourceUnavailable() {

        JOptionPane.showMessageDialog(
                this,
                "No resource is available for this material.",
                "Resource Not Available",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =====================================================
    // OPEN URL OR LOCAL FILE
    // =====================================================

    private void openSource(
            String source
    ) {

        try {

            // =================================================
            // ONLINE RESOURCE
            // =================================================

            if (
                    source.startsWith(
                            "http://"
                    )
                            ||
                            source.startsWith(
                                    "https://"
                            )
            ) {

                if (
                        !Desktop.isDesktopSupported()
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Browser opening is not supported on this system.",
                            "Browser Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                Desktop desktop =
                        Desktop.getDesktop();

                if (
                        !desktop.isSupported(
                                Desktop.Action.BROWSE
                        )
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Your system does not support opening web links.",
                            "Browser Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                desktop.browse(
                        new URI(
                                source
                        )
                );

                return;
            }

            // =================================================
            // LOCAL FILE
            // =================================================

            File file =
                    new File(
                            source
                    );

            if (
                    !file.isAbsolute()
            ) {

                file =
                        new File(
                                "src/main/resources/study_materials/"
                                        + source
                        );
            }

            if (
                    !file.exists()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "File not found:\n\n"
                                + file.getAbsolutePath(),
                        "File Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (
                    !Desktop.isDesktopSupported()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Your system does not support opening files.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Desktop desktop =
                    Desktop.getDesktop();

            if (
                    !desktop.isSupported(
                            Desktop.Action.OPEN
                    )
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Your system does not support opening this file.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            desktop.open(
                    file
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open resource:\n\n"
                            + e.getMessage(),
                    "Open Resource Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // LOAD MATERIALS
    // =====================================================

    private void loadStudyMaterials() {

        refreshButton.setEnabled(false);
        refreshButton.setText("Loading...");
        sourceStatusLabel.setText("Connecting to CampusConnect API...");

        SwingWorker<List<StudyMaterial>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<StudyMaterial> doInBackground() throws Exception {
                String apiUrl = "https://campusconnect-backend-grn0.onrender.com/api/study-materials";

                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(20))
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .timeout(Duration.ofSeconds(90))
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    throw new IOException("API returned HTTP " + response.statusCode()
                            + ". Response: " + response.body());
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                if (root == null || !root.isArray()) {
                    throw new IOException("Unexpected API response. Expected a JSON array.");
                }

                List<StudyMaterial> materials = new java.util.ArrayList<>();
                for (JsonNode node : root) {
                    StudyMaterial material = new StudyMaterial();
                    material.setMaterialId(node.path("materialId").asInt(0));
                    material.setTitle(node.path("title").asText(""));
                    material.setSubject(node.path("subject").asText(""));
                    material.setDescription(node.path("description").asText(""));
                    material.setFilePath(node.path("filePath").asText(""));
                    materials.add(material);
                }
                return materials;
            }

            @Override
            protected void done() {
                try {
                    List<StudyMaterial> materials = get();
                    tableModel.setRowCount(0);
                    selectedMaterialLabel.setText("No material selected");
                    sourceStatusLabel.setText("🔗 Select a resource to open it");

                    for (StudyMaterial material : materials) {
                        tableModel.addRow(new Object[]{
                                material.getMaterialId(),
                                material.getTitle(),
                                material.getSubject(),
                                material.getDescription(),
                                material.getFilePath()
                        });
                    }

                    totalMaterialsLabel.setText(String.valueOf(materials.size()));
                    sourceStatusLabel.setText(materials.isEmpty()
                            ? "No study materials available"
                            : "Loaded " + materials.size() + " materials from API");

                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    sourceStatusLabel.setText("Unable to load study materials from API");
                    JOptionPane.showMessageDialog(
                            StudyMaterialsFrame.this,
                            "Unable to load study materials from API:\n\n"
                                    + cause.getMessage(),
                            "API Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    refreshButton.setEnabled(true);
                    refreshButton.setText("↻  Refresh");
                }
            }
        };

        worker.execute();
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new StudyMaterialsFrame()
        );
    }
}