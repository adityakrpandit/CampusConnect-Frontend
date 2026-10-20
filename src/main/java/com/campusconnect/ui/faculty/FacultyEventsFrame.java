package com.campusconnect.ui.faculty;
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
/** Faculty event manager styled to match Admin ManageEventsFrame. */
public class FacultyEventsFrame extends JFrame {
    private static final Color PAGE_BG = new Color(232, 247, 251);
    private static final Color WHITE = Color.WHITE;
    private static final Color NAVY = new Color(24, 67, 108);
    private static final Color TEAL = new Color(24, 168, 181);
    private static final Color BLUE = new Color(55, 105, 180);
    private static final Color GREEN = new Color(22, 160, 86);
    private static final Color RED = new Color(218, 55, 55);
    private static final Color GRAY = new Color(102, 120, 140);
    private static final Color LIGHT_BORDER = new Color(205, 222, 232);
    private static final Color TEXT = new Color(35, 55, 75);
    private static final Color MUTED = new Color(95, 118, 140);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 30);
    private static final Font SECTION_FONT = new Font("Arial", Font.BOLD, 21);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 13);
    private final String facultyName;
    private final String facultyEmail;
    private final boolean embedded;
    private final Runnable onClose;
    private int facultyId = -1;
    private JTable eventsTable;
    private DefaultTableModel tableModel;
    private JTextField eventNameField, eventDateField, venueField, searchField;
    private JTextArea descriptionArea;
    private JLabel totalEventsValue, upcomingEventsValue, latestEventValue;
    public FacultyEventsFrame(String facultyName, String facultyEmail) {
        this(facultyName, facultyEmail, false, null);
    }

    public FacultyEventsFrame(String facultyName, String facultyEmail,
                              boolean embedded, Runnable onClose) {
        this.facultyName = facultyName == null || facultyName.trim().isEmpty()
                ? "Faculty Member" : facultyName.trim();
        this.facultyEmail = facultyEmail == null ? "" : facultyEmail.trim();
        this.embedded = embedded;
        this.onClose = onClose;

        if (!embedded) {
            setTitle("CampusConnect - Faculty Events");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(1100, 700));
            setSize(1240, 820);
            setLocationRelativeTo(null);
        }

        if (!loadApprovedFacultyId()) {
            JOptionPane.showMessageDialog(
                    embedded ? null : this,
                    "Unable to identify this approved faculty account. Please log in again.",
                    "Faculty Account",
                    JOptionPane.ERROR_MESSAGE
            );
            closeModule();
            return;
        }

        buildUI();
        loadEvents("");

        if (!embedded) {
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    private void closeModule() {
        if (embedded) {
            if (onClose != null) {
                onClose.run();
            }
        } else {
            dispose();
        }
    }
    private boolean loadApprovedFacultyId() {
        String sql = "SELECT faculty_id FROM faculty WHERE email = ? AND status = 'APPROVED'";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, facultyEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    facultyId = rs.getInt("faculty_id");
                    return true;
                }
            }
        }  catch (SQLException ex) {
            showDatabaseError("Could not verify faculty account", ex);
        }
        return false;
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PAGE_BG);
        content.setBorder(new EmptyBorder(14, 20, 20, 20));

        content.add(createStatsPanel());
        content.add(Box.createVerticalStrut(14));
        content.add(createEventsListCard());
        content.add(Box.createVerticalStrut(14));
        content.add(createAddEventCard());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scroll, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, LIGHT_BORDER), new EmptyBorder(15, 35, 15, 35)));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        left.setBackground(WHITE);
        left.add(createIconBox("▣", TEAL, 68));
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(WHITE);
        JLabel brand = new JLabel("CAMPUSCONNECT");
        brand.setFont(new Font("Arial", Font.BOLD, 13));
        brand.setForeground(TEAL);
        JLabel title = new JLabel("Faculty Events");
        title.setFont(TITLE_FONT);
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Create, manage and organize campus events");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        titleBox.add(brand);
        titleBox.add(title);
        titleBox.add(subtitle);
        left.add(titleBox);
        header.add(left, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        right.setBackground(WHITE);
        right.add(createIconBox("CC", NAVY, 58));
        JLabel campus = new JLabel("<html><b style='font-size:15px'>CampusConnect</b><br><font color='#60758A'>Faculty Portal</font></html>");
        right.add(campus);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createIconBox(String text, Color color, int size) {
        JPanel box = new JPanel(new GridBagLayout());
        box.setBackground(color);
        box.setPreferredSize(new Dimension(size, size));
        JLabel label = new JLabel(text);
        label.setForeground(WHITE);
        label.setFont(new Font("Arial", Font.BOLD, size >= 65 ? 28 : 21));
        box.add(label);
        return box;
    }

    private JPanel createStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 14, 0));
        stats.setOpaque(false);
        totalEventsValue = createStatValue();
        upcomingEventsValue = createStatValue();
        latestEventValue = createStatValue();
        stats.add(createStatCard("▣", "TOTAL EVENTS", totalEventsValue, TEAL));
        stats.add(createStatCard("◷", "UPCOMING EVENTS", upcomingEventsValue, BLUE));
        stats.add(createStatCard("★", "LATEST EVENT", latestEventValue, NAVY));
        return stats;
    }

    private JLabel createStatValue() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Arial", Font.BOLD, 21));
        return label;
    }

    private JPanel createStatCard(String icon, String title, JLabel value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LIGHT_BORDER), new EmptyBorder(12, 14, 12, 14)));
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(Math.min(255, accent.getRed()+205), Math.min(255, accent.getGreen()+205), Math.min(255, accent.getBlue()+205)));
        iconLabel.setForeground(accent);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setPreferredSize(new Dimension(55,55));
        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(WHITE);
        text.setBorder(new EmptyBorder(2,12,0,0));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(MUTED);
        value.setForeground(accent);
        value.setFont(new Font("Arial", Font.BOLD, 19));
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(5));
        text.add(value);
        card.add(iconLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel createEventsListCard() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LIGHT_BORDER), new EmptyBorder(16,20,14,20)));
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(WHITE);
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(WHITE);
        JLabel title = new JLabel("Current Events");
        title.setFont(SECTION_FONT);
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Select an event to view or manage it");
        subtitle.setFont(NORMAL_FONT);
        subtitle.setForeground(MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(3));
        heading.add(subtitle);
        top.add(heading, BorderLayout.WEST);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(WHITE);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220,34));
        searchField.setFont(NORMAL_FONT);
        searchField.setToolTipText("Search events...");
        styleField(searchField);
        JButton refresh = createButton("↻  Refresh", BLUE, WHITE, 105, 34);
        searchPanel.add(new JLabel("⌕"));
        searchPanel.add(searchField);
        searchPanel.add(refresh);
        top.add(searchPanel, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);
        String[] columns = {
                "ID", "EVENT", "DATE", "VENUE", "DESCRIPTION", "OWNER"
        }
                ;
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        }
        ;
        eventsTable = new JTable(tableModel);
        eventsTable.setFont(NORMAL_FONT);
        eventsTable.setForeground(TEXT);
        eventsTable.setBackground(WHITE);
        eventsTable.setRowHeight(40);
        eventsTable.setGridColor(new Color(220,231,238));
        eventsTable.setSelectionBackground(new Color(222,241,247));
        eventsTable.setSelectionForeground(NAVY);
        eventsTable.setAutoCreateRowSorter(true);
        eventsTable.setFillsViewportHeight(true);
        eventsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        eventsTable.getTableHeader().setForeground(WHITE);
        eventsTable.getTableHeader().setBackground(NAVY);
        eventsTable.getTableHeader().setPreferredSize(new Dimension(0,42));
        eventsTable.getTableHeader().setReorderingAllowed(false);
        // Keep the ownership value in the table model for permission checks,
        // but hide the OWNER column so the visible table matches the Admin panel.
        int[] widths = {
                55,190,120,180,390,90
        }
                ;
        for (int i=0;i<widths.length;i++) eventsTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        eventsTable.removeColumn(eventsTable.getColumnModel().getColumn(5));
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        eventsTable.getColumnModel().getColumn(0).setCellRenderer(center);
        eventsTable.getColumnModel().getColumn(2).setCellRenderer(center);
        JScrollPane tableScroll = new JScrollPane(eventsTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER));
        tableScroll.setPreferredSize(new Dimension(900,280));
        card.add(tableScroll, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        bottom.setBackground(WHITE);
        JButton clear = createButton("⌫  Clear Selection", GRAY, WHITE, 145, 38);
        JButton delete = createButton("▣  Delete My Event", RED, WHITE, 160, 38);
        bottom.add(clear);
        bottom.add(delete);
        card.add(bottom, BorderLayout.SOUTH);
        refresh.addActionListener(e -> loadEvents(searchField.getText().trim()));
        clear.addActionListener(e -> {
                    eventsTable.clearSelection(); clearForm();
                }
        );
        delete.addActionListener(e -> deleteEvent());
        searchField.getDocument().addDocumentListener(new DocumentListener() {
                                                          private void search() {
                                                              loadEvents(searchField.getText().trim());
                                                          }
                                                          public void insertUpdate(DocumentEvent e) {
                                                              search();
                                                          }
                                                          public void removeUpdate(DocumentEvent e) {
                                                              search();
                                                          }
                                                          public void changedUpdate(DocumentEvent e) {
                                                              search();
                                                          }
                                                      }
        );
        eventsTable.getSelectionModel().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting() && eventsTable.getSelectedRow() >= 0) {
                        int row = eventsTable.convertRowIndexToModel(eventsTable.getSelectedRow());
                        loadSelectedEvent(row);
                    }
                }
        );
        return card;
    }

    private JPanel createAddEventCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LIGHT_BORDER), new EmptyBorder(16,20,16,20)));
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(WHITE);
        JLabel title = new JLabel("Add New Event");
        title.setFont(SECTION_FONT);
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Publish an event for CampusConnect students");
        subtitle.setFont(NORMAL_FONT);
        subtitle.setForeground(MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(3));
        heading.add(subtitle);
        header.add(heading, BorderLayout.WEST);
        JLabel badge = new JLabel("FACULTY EVENT");
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(TEAL);
        header.add(badge, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(12,0,8,0));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,5,6,12);
        g.fill = GridBagConstraints.HORIZONTAL;
        eventNameField = createTextField();
        eventDateField = createTextField();
        venueField = createTextField();
        descriptionArea = new JTextArea(3,20);
        descriptionArea.setFont(NORMAL_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setForeground(TEXT);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LIGHT_BORDER), new EmptyBorder(7,8,7,8)));
        addFormRow(form,g,0,0,"▣  EVENT NAME",eventNameField);
        JPanel datePanel = new JPanel(new BorderLayout(5,0));
        datePanel.setBackground(WHITE);
        datePanel.add(eventDateField,BorderLayout.CENTER);
        JButton today = createButton("▣ Today", TEAL, WHITE, 85, 34);
        datePanel.add(today,BorderLayout.EAST);
        today.addActionListener(e -> setToday());
        addFormRow(form,g,2,0,"▣  DATE",datePanel);
        addFormRow(form,g,0,1,"⌖  VENUE",venueField);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(200,65));
        descScroll.setBorder(BorderFactory.createLineBorder(LIGHT_BORDER));
        addFormRow(form,g,2,1,"▤  DESCRIPTION",descScroll);
        card.add(form,BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        buttons.setBackground(WHITE);
        JButton clear = createButton("⌫  Clear", GRAY, WHITE, 110, 38);
        JButton add = createButton("＋  Add Event", TEAL, WHITE, 145, 38);
        JButton close = createButton("×  Close", NAVY, WHITE, 105, 38);
        buttons.add(clear);
        buttons.add(add);
        buttons.add(close);
        card.add(buttons,BorderLayout.SOUTH);
        clear.addActionListener(e -> clearForm());
        add.addActionListener(e -> addEvent());
        close.addActionListener(e -> closeModule());
        return card;
    }
    private void addFormRow(JPanel form, GridBagConstraints g, int x, int y, String label, Component field) {
        g.gridx=x;
        g.gridy=y;
        g.weightx=0;
        form.add(createFieldLabel(label),g);
        g.gridx=x+1;
        g.gridy=y;
        g.weightx=1;
        form.add(field,g);
    }
    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(LABEL_FONT);
        l.setForeground(new Color(70,95,118));
        return l;
    }
    private JTextField createTextField() {
        JTextField f = new JTextField();
        f.setFont(NORMAL_FONT);
        f.setForeground(TEXT);
        f.setBackground(WHITE);
        f.setPreferredSize(new Dimension(180,34));
        styleField(f);
        return f;
    }
    private void styleField(JTextField f) {
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LIGHT_BORDER), new EmptyBorder(0,8,0,8)));
    }
    private JButton createButton(String text, Color bg, Color fg, int w, int h) {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setForeground(fg);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(w,h));
        return b;
    }
    private void loadEvents(String filter) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        String sql = "SELECT event_id, event_name, event_date, venue, description, faculty_id FROM events "
                + "WHERE LOWER(event_name) LIKE ? OR LOWER(venue) LIKE ? OR LOWER(description) LIKE ? ORDER BY event_date ASC";
        String pattern = "%" + (filter == null ? "" : filter.toLowerCase()) + "%";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,pattern);
            ps.setString(2,pattern);
            ps.setString(3,pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int ownerId = rs.getInt("faculty_id");
                    String owner = rs.wasNull() ? "Admin" : (ownerId == facultyId ? "You" : "Faculty");
                    tableModel.addRow(new Object[] {
                                    rs.getInt("event_id"),rs.getString("event_name"),rs.getDate("event_date"),rs.getString("venue"),rs.getString("description"),owner
                            }
                    );
                }
            }
            updateStatistics();
        }  catch (SQLException ex) {
            showDatabaseError("Error loading events", ex);
        }
    }
    private void updateStatistics() {
        int total=tableModel.getRowCount(), upcoming=0;
        String latest="-";
        long earliest=Long.MAX_VALUE;
        long today = Date.valueOf(java.time.LocalDate.now()).getTime();
        for (int i=0;i<total;i++) {
            Object dateObj=tableModel.getValueAt(i,2);
            if (dateObj instanceof Date) {
                Date d=(Date)dateObj;
                if (d.getTime()>=today) {
                    upcoming++;
                    if (d.getTime()<earliest) {
                        earliest=d.getTime();
                        latest=String.valueOf(tableModel.getValueAt(i,1));
                    }
                }
            }
        }
        totalEventsValue.setText(String.valueOf(total));
        upcomingEventsValue.setText(String.valueOf(upcoming));
        latestEventValue.setText(latest.length()>18 ? latest.substring(0,15)+"..." : latest);
    }
    private void loadSelectedEvent(int row) {
        eventNameField.setText(String.valueOf(tableModel.getValueAt(row,1)));
        Object date=tableModel.getValueAt(row,2);
        eventDateField.setText(date==null?"":date.toString());
        venueField.setText(String.valueOf(tableModel.getValueAt(row,3)));
        Object description=tableModel.getValueAt(row,4);
        descriptionArea.setText(description==null?"":description.toString());
    }
    private void setToday() {
        eventDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    }
    private void addEvent() {
        String name=eventNameField.getText().trim(), dateText=eventDateField.getText().trim();
        String venue=venueField.getText().trim(), description=descriptionArea.getText().trim();
        if (name.isEmpty() || dateText.isEmpty() || venue.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please fill all event fields.","Incomplete Information",JOptionPane.WARNING_MESSAGE);
            return;
        }
        Date date;
        try {
            date=Date.valueOf(dateText);
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,"Use date format YYYY-MM-DD (example: 2026-10-15).","Invalid Date",JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql="INSERT INTO events (event_name,event_date,venue,description,faculty_id) VALUES (?,?,?,?,?)";
        try (Connection con=DatabaseConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1,name);
            ps.setDate(2,date);
            ps.setString(3,venue);
            ps.setString(4,description);
            ps.setInt(5,facultyId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Event added successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadEvents(searchField.getText().trim());
        }  catch (SQLException ex) {
            showDatabaseError("Error adding event",ex);
        }
    }
    private void deleteEvent() {
        int viewRow=eventsTable.getSelectedRow();
        if (viewRow<0) {
            JOptionPane.showMessageDialog(this,"Please select one of your own events to delete.","No Event Selected",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row=eventsTable.convertRowIndexToModel(viewRow);
        int eventId=Integer.parseInt(String.valueOf(tableModel.getValueAt(row,0)));
        String owner=String.valueOf(tableModel.getValueAt(row,5));
        if (!"You".equals(owner)) {
            JOptionPane.showMessageDialog(this,"You can delete only events created by your faculty account. Admin and other faculty events cannot be deleted.","Permission Denied",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String eventName=String.valueOf(tableModel.getValueAt(row,1));
        int confirm=JOptionPane.showConfirmDialog(this,"Delete your event?\n\n"+eventName,"Confirm Delete",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (confirm!=JOptionPane.YES_OPTION) return;
        String sql="DELETE FROM events WHERE event_id=? AND faculty_id=?";
        try (Connection con=DatabaseConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,eventId);
            ps.setInt(2,facultyId);
            int deleted=ps.executeUpdate();
            if (deleted>0) {
                JOptionPane.showMessageDialog(this,"Event deleted successfully!","Deleted",JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadEvents(searchField.getText().trim());
            }
            else JOptionPane.showMessageDialog(this,"Event could not be found or is not owned by your account.","Delete Error",JOptionPane.ERROR_MESSAGE);
        }  catch (SQLException ex) {
            showDatabaseError("Error deleting event",ex);
        }
    }
    private void clearForm() {
        eventNameField.setText("");
        eventDateField.setText("");
        venueField.setText("");
        descriptionArea.setText("");
        if (eventsTable!=null) eventsTable.clearSelection();
    }
    private void showDatabaseError(String message, SQLException ex) {
        JOptionPane.showMessageDialog(this,message+":\n"+ex.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
    }
    // Open this frame from FacultyDashboardFrame using the authenticated faculty details:
    // new FacultyEventsFrame(facultyName, facultyEmail);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FacultyEventsFrame(
                    "Amrendra Yadav",
                    "amrendra@gmail.com"
            );
        });
    }

}
