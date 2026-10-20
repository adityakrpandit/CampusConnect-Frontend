package com.campusconnect.ui.admin;

import com.campusconnect.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * CampusConnect - Manage Faculty
 * Improved admin UI for reviewing and managing faculty registration requests.
 */
public class ManageFacultyFrame extends JPanel {

    private static final Color NAVY = new Color(19, 57, 96);
    private static final Color NAVY_DARK = new Color(13, 42, 72);
    private static final Color TEAL = new Color(22, 166, 178);
    private static final Color BACKGROUND = new Color(237, 247, 250);
    private static final Color BORDER = new Color(211, 226, 235);
    private static final Color TEXT = new Color(35, 57, 78);
    private static final Color MUTED = new Color(105, 128, 148);
    private static final Color GREEN = new Color(38, 153, 96);
    private static final Color RED = new Color(211, 65, 72);
    private static final Color AMBER = new Color(211, 145, 35);

    private final DefaultTableModel tableModel;
    private final JTable facultyTable;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField searchField;
    private final JLabel totalValue;
    private final JLabel pendingValue;
    private final JLabel approvedValue;
    private final JLabel rejectedValue;
    private final JLabel statusLabel;
    private final Runnable onClose;

    public ManageFacultyFrame(Runnable onClose) {
        this.onClose = onClose;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        tableModel = new DefaultTableModel(
                new String[]{"Faculty ID", "Name", "Email", "Department", "Designation", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        facultyTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        facultyTable.setRowSorter(sorter);
        searchField = new JTextField();
        totalValue = new JLabel("0");
        pendingValue = new JLabel("0");
        approvedValue = new JLabel("0");
        rejectedValue = new JLabel("0");
        statusLabel = new JLabel("Ready");

        buildUI();
        loadFaculty();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(root, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel(new BorderLayout(18, 8));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(22, 28, 20, 28));

        JPanel titleGroup = new JPanel(new BorderLayout(15, 0));
        titleGroup.setOpaque(false);
        JLabel icon = new JLabel("FC", SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(TEAL);
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("Arial", Font.BOLD, 23));
        icon.setPreferredSize(new Dimension(64, 64));

        JPanel titleText = new JPanel();
        titleText.setOpaque(false);
        titleText.setLayout(new BoxLayout(titleText, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("CAMPUSCONNECT  /  ADMIN PORTAL");
        brand.setFont(new Font("Arial", Font.BOLD, 11));
        brand.setForeground(TEAL);
        JLabel title = new JLabel("Manage Faculty");
        title.setFont(new Font("Arial", Font.BOLD, 29));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Review faculty registrations and manage approval status.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        titleText.add(brand);
        titleText.add(Box.createVerticalStrut(3));
        titleText.add(title);
        titleText.add(Box.createVerticalStrut(4));
        titleText.add(subtitle);
        titleGroup.add(icon, BorderLayout.WEST);
        titleGroup.add(titleText, BorderLayout.CENTER);

        JPanel brandRight = new JPanel();
        brandRight.setOpaque(false);
        brandRight.setLayout(new BoxLayout(brandRight, BoxLayout.Y_AXIS));
        JLabel cc = new JLabel("CC", SwingConstants.CENTER);
        cc.setOpaque(true);
        cc.setBackground(NAVY);
        cc.setForeground(Color.WHITE);
        cc.setFont(new Font("Arial", Font.BOLD, 18));
        cc.setPreferredSize(new Dimension(54, 54));
        cc.setMaximumSize(new Dimension(54, 54));
        cc.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel brandName = new JLabel("CampusConnect");
        brandName.setFont(new Font("Arial", Font.BOLD, 17));
        brandName.setForeground(TEXT);
        brandName.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel portal = new JLabel("Admin Portal");
        portal.setFont(new Font("Arial", Font.PLAIN, 12));
        portal.setForeground(MUTED);
        portal.setAlignmentX(Component.RIGHT_ALIGNMENT);
        brandRight.add(cc);
        brandRight.add(Box.createVerticalStrut(5));
        brandRight.add(brandName);
        brandRight.add(portal);

        header.add(titleGroup, BorderLayout.CENTER);
        header.add(brandRight, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(18, 24, 16, 24));
        root.add(content, BorderLayout.CENTER);

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);
        stats.add(createStatCard("TOTAL FACULTY", totalValue, NAVY));
        stats.add(createStatCard("PENDING REVIEW", pendingValue, AMBER));
        stats.add(createStatCard("APPROVED", approvedValue, GREEN));
        stats.add(createStatCard("REJECTED", rejectedValue, RED));
        content.add(stats, BorderLayout.NORTH);

        JPanel tableCard = new JPanel(new BorderLayout(0, 14));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(18, 20, 16, 20)));
        content.add(tableCard, BorderLayout.CENTER);

        JPanel tableHeader = new JPanel(new BorderLayout(12, 8));
        tableHeader.setOpaque(false);
        JPanel headingStack = new JPanel();
        headingStack.setOpaque(false);
        headingStack.setLayout(new BoxLayout(headingStack, BoxLayout.Y_AXIS));
        JLabel tableTitle = new JLabel("Faculty Registrations");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 20));
        tableTitle.setForeground(NAVY);
        JLabel tableSubtitle = new JLabel("Select a pending request to approve or reject it.");
        tableSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        tableSubtitle.setForeground(MUTED);
        headingStack.add(tableTitle);
        headingStack.add(Box.createVerticalStrut(4));
        headingStack.add(tableSubtitle);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchLabel.setForeground(TEXT);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setToolTipText("Search by name, email, department, designation or status");
        searchField.setPreferredSize(new Dimension(245, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(7, 10, 7, 10)));
        JButton refreshButton = createButton("Refresh", NAVY, Color.WHITE);
        refreshButton.addActionListener(e -> loadFaculty());
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(refreshButton, BorderLayout.EAST);

        tableHeader.add(headingStack, BorderLayout.WEST);
        tableHeader.add(searchPanel, BorderLayout.EAST);
        tableCard.add(tableHeader, BorderLayout.NORTH);

        configureTable();
        JScrollPane scrollPane = new JScrollPane(facultyTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateFilter() {
                String text = searchField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { updateFilter(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { updateFilter(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { updateFilter(); }
        });

        // Footer actions
        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setOpaque(false);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(MUTED);
        footer.add(statusLabel, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        JButton clearButton = createButton("Clear Selection", new Color(103, 122, 140), Color.WHITE);
        JButton rejectButton = createButton("Reject Request", RED, Color.WHITE);
        JButton approveButton = createButton("Approve Request", GREEN, Color.WHITE);
        JButton closeButton = createButton("Close", NAVY, Color.WHITE);
        clearButton.addActionListener(e -> facultyTable.clearSelection());
        rejectButton.addActionListener(e -> updateFacultyStatus("REJECTED"));
        approveButton.addActionListener(e -> updateFacultyStatus("APPROVED"));
        closeButton.addActionListener(e -> {
            if (onClose != null) {
                onClose.run();
            }
        });
        actions.add(clearButton);
        actions.add(rejectButton);
        actions.add(approveButton);
        actions.add(closeButton);
        footer.add(actions, BorderLayout.EAST);
        tableCard.add(footer, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String labelText, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(14, 16, 14, 16)));
        JLabel accentBlock = new JLabel(" ");
        accentBlock.setOpaque(true);
        accentBlock.setBackground(accent);
        accentBlock.setPreferredSize(new Dimension(5, 48));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 10));
        label.setForeground(MUTED);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 25));
        valueLabel.setForeground(accent);
        text.add(label);
        text.add(Box.createVerticalStrut(5));
        text.add(valueLabel);
        card.add(accentBlock, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(Math.max(105, button.getPreferredSize().width + 16), 38));
        return button;
    }

    private void configureTable() {
        facultyTable.setFont(new Font("Arial", Font.PLAIN, 13));
        facultyTable.setForeground(TEXT);
        facultyTable.setBackground(Color.WHITE);
        facultyTable.setSelectionBackground(new Color(216, 239, 243));
        facultyTable.setSelectionForeground(NAVY_DARK);
        facultyTable.setRowHeight(36);
        facultyTable.setShowGrid(true);
        facultyTable.setGridColor(new Color(224, 234, 240));
        facultyTable.setFillsViewportHeight(true);
        facultyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        facultyTable.setAutoCreateRowSorter(false);
        facultyTable.setIntercellSpacing(new Dimension(1, 1));

        facultyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        facultyTable.getTableHeader().setForeground(Color.WHITE);
        facultyTable.getTableHeader().setBackground(NAVY);
        facultyTable.getTableHeader().setPreferredSize(new Dimension(0, 42));
        facultyTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        facultyTable.getColumnModel().getColumn(0).setCellRenderer(center);
        facultyTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected && value != null) {
                    String status = value.toString().toUpperCase();
                    if ("APPROVED".equals(status)) label.setForeground(GREEN);
                    else if ("REJECTED".equals(status)) label.setForeground(RED);
                    else label.setForeground(AMBER);
                }
                label.setFont(new Font("Arial", Font.BOLD, 12));
                return label;
            }
        });
        facultyTable.getColumnModel().getColumn(0).setPreferredWidth(85);
        facultyTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        facultyTable.getColumnModel().getColumn(2).setPreferredWidth(240);
        facultyTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        facultyTable.getColumnModel().getColumn(4).setPreferredWidth(160);
        facultyTable.getColumnModel().getColumn(5).setPreferredWidth(115);
    }

    private void loadFaculty() {
        tableModel.setRowCount(0);
        String sql = "SELECT faculty_id, name, email, department, designation, status FROM faculty " +
                "ORDER BY CASE WHEN status = 'PENDING' THEN 0 ELSE 1 END, created_at DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("faculty_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("department"),
                        resultSet.getString("designation"),
                        resultSet.getString("status")
                });
            }
            updateStatistics();
            statusLabel.setText("Showing " + tableModel.getRowCount() + " faculty record(s)");
        } catch (SQLException ex) {
            statusLabel.setText("Could not load faculty records");
            JOptionPane.showMessageDialog(this,
                    "Unable to load faculty records.\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatistics() {
        int total = tableModel.getRowCount();
        int pending = 0;
        int approved = 0;
        int rejected = 0;
        for (int row = 0; row < total; row++) {
            Object value = tableModel.getValueAt(row, 5);
            String status = value == null ? "" : value.toString().trim().toUpperCase();
            if ("PENDING".equals(status)) pending++;
            else if ("APPROVED".equals(status)) approved++;
            else if ("REJECTED".equals(status)) rejected++;
        }
        totalValue.setText(String.valueOf(total));
        pendingValue.setText(String.valueOf(pending));
        approvedValue.setText(String.valueOf(approved));
        rejectedValue.setText(String.valueOf(rejected));
    }

    private void updateFacultyStatus(String newStatus) {
        int viewRow = facultyTable.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a faculty record first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = facultyTable.convertRowIndexToModel(viewRow);
        int facultyId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String currentStatus = String.valueOf(tableModel.getValueAt(selectedRow, 5));
        if (!"PENDING".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Only pending faculty requests can be approved or rejected.",
                    "Action Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + newStatus.toLowerCase() + " this faculty request?",
                "Confirm Action", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmation != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE faculty SET status = ?, reviewed_at = CURRENT_TIMESTAMP " +
                "WHERE faculty_id = ? AND status = 'PENDING'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setInt(2, facultyId);
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 1) {
                JOptionPane.showMessageDialog(this,
                        "Faculty request " + newStatus.toLowerCase() + " successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "The request was already processed. Please refresh the list.",
                        "Update Failed", JOptionPane.WARNING_MESSAGE);
            }
            loadFaculty();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to update faculty status.\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CampusConnect - Manage Faculty");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1050, 650));
            frame.setSize(1240, 790);
            frame.setLocationRelativeTo(null);
            ManageFacultyFrame panel = new ManageFacultyFrame(frame::dispose);
            frame.setContentPane(panel);
            frame.setVisible(true);
        });
    }
}
