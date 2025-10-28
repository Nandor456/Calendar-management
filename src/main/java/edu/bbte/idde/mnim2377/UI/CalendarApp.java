package edu.bbte.idde.mnim2377.UI;

import edu.bbte.idde.mnim2377.data.dao.InMemoryCalendarDao;
import edu.bbte.idde.mnim2377.data.model.Calendar;
import edu.bbte.idde.mnim2377.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * The main application class with the Swing user interface.
 * This class is responsible for displaying the calendar data and
 * handling user interactions like adding, updating, and deleting entries.
 */
public class CalendarApp extends JFrame {

    private final CalendarServiceImplementation calendarService;
    private final JTable calendarTable;
    private final DefaultTableModel tableModel;

    public CalendarApp() {
        // --- Initialization ---
        calendarService = new CalendarServiceImplementation(new InMemoryCalendarDao());

        // --- UI Components ---
        setTitle("Calendar Management App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Table Model
        String[] columnNames = {"ID", "Address", "Location", "Date", "isOnline"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        calendarTable = new JTable(tableModel);
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide the ID column
        calendarTable.getColumnModel().getColumn(0).setMinWidth(0);
        calendarTable.getColumnModel().getColumn(0).setMaxWidth(0);
        calendarTable.getColumnModel().getColumn(0).setWidth(0);

        // Buttons
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        // Layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners for Buttons ---
        addButton.addActionListener(e -> showCalendarDialog(null));

        updateButton.addActionListener(e -> {
            int selectedRow = calendarTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                try {
                    Calendar calendarToUpdate = calendarService.getCalendarById(id);
                    // Pass the existing object to the dialog to populate it for editing
                    showCalendarDialog(calendarToUpdate);
                } catch (ServiceException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: The selected item could not be found.",
                            "Not Found", JOptionPane.ERROR_MESSAGE);
                    refreshTable(); // Refresh in case it was deleted by another process
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a calendar entry to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = calendarTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                String address = (String) tableModel.getValueAt(selectedRow, 1);

                int choice = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the entry for '" + address + "'?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        calendarService.deleteCalendar(id);
                        refreshTable();
                    } catch (ServiceException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error: Could not delete the item.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a calendar entry to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- Initial Data Load ---
        addSampleData();
        refreshTable();

        setVisible(true);
    }

    /**
     * Shows a dialog for adding a new entry or updating an existing one.
     * @param calendar The calendar to edit. If null, the dialog is configured for adding a new entry.
     */
    private void showCalendarDialog(Calendar calendar) {
        // --- Dialog Components ---
        JTextField addressField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField dateField = new JTextField(10);
        JCheckBox isOnlineCheckBox = new JCheckBox("Online event");

        final String dialogTitle;
        // If a calendar object is passed, pre-fill the fields for updating
        if (calendar != null) {
            dialogTitle = "Update Calendar Entry";
            addressField.setText(calendar.getAddress());
            locationField.setText(calendar.getLocation());
            dateField.setText(calendar.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            isOnlineCheckBox.setSelected(calendar.getOnline());
        } else {
            dialogTitle = "Add New Calendar Entry";
            dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        // --- Dialog Layout ---
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(addressField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(locationField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(dateField, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(isOnlineCheckBox, gbc);

        // --- Show Dialog and Process Result ---
        int result = JOptionPane.showConfirmDialog(this, panel, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String address = addressField.getText();
                String location = locationField.getText();
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
                boolean isOnline = isOnlineCheckBox.isSelected();

                if (address.trim().isEmpty() || location.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Address and Location cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (calendar == null) { // This is a new entry
                    calendarService.addCalendar(new Calendar(address, location, date, isOnline));
                } else { // This is an existing entry to update
                    calendar.setAddress(address);
                    calendar.setLocation(location);
                    calendar.setDate(date);
                    calendar.setOnline(isOnline);
                    calendarService.updateCalendar(calendar);
                }
                refreshTable(); // Update the table to show the new/updated data
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Calendar> calendars = calendarService.getAllCalendars();
        for (Calendar calendar : calendars) {
            Object[] row = {
                    calendar.getId(),
                    calendar.getAddress(),
                    calendar.getLocation(),
                    calendar.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    calendar.getOnline() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }

    private void addSampleData() {
        calendarService.addCalendar(new Calendar("123 Main St", "Conference Room A", LocalDate.parse("2025-10-20"), true));
        calendarService.addCalendar(new Calendar("456 Oak Ave", "Client Office", LocalDate.parse("2025-11-15"), false));
    }
}