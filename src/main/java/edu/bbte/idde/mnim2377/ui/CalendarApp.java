package edu.bbte.idde.mnim2377.ui;

import edu.bbte.idde.mnim2377.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.data.dao.DaoFactory;
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

public class CalendarApp extends JFrame {

    private final transient CalendarServiceImplementation calendarService;
    private JTable calendarTable;
    private DefaultTableModel tableModel;

    public CalendarApp() {
        super();
        DaoFactory daoFactory = DaoFactory.getInstance();
        CalendarDao calendarDao = daoFactory.getCalendarDao();
        calendarService = new CalendarServiceImplementation(calendarDao);

        // --- UI Components ---
        setTitle("Calendar Management App");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        initializeUI();
        refreshTable();

        setVisible(true);
    }

    private void initializeUI() {
        setTitle("Calendar Management App");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Address", "Location", "Date", "isOnline"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        calendarTable = new JTable(tableModel);
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        hideIdColumn();

        JPanel buttonPanel = createButtonPanel();
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void hideIdColumn() {
        calendarTable.getColumnModel().getColumn(0).setMinWidth(0);
        calendarTable.getColumnModel().getColumn(0).setMaxWidth(0);
        calendarTable.getColumnModel().getColumn(0).setWidth(0);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> onAddButtonClicked());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> onUpdateButtonClicked());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> onDeleteButtonClicked());
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private void showCalendarDialog(Calendar calendar) {
        // --- Dialog Components ---
        JTextField addressField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField dateField = new JTextField(10);
        JCheckBox onlineCheckBox = new JCheckBox("Online event");

        String dialogTitle = populateDialogFields(calendar, addressField, locationField, dateField, onlineCheckBox);

        JPanel panel = createDialogPanel(addressField, locationField, dateField, onlineCheckBox);
        // --- Dialog Layout ---
        int result = JOptionPane.showConfirmDialog(this, panel, dialogTitle,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            processDialogResult(calendar, addressField, locationField, dateField, onlineCheckBox);
        }
    }

    private String populateDialogFields(Calendar calendar, JTextField addressField, JTextField locationField,
                                        JTextField dateField, JCheckBox onlineCheckBox) {
        if (calendar != null) {
            addressField.setText(calendar.getAddress());
            locationField.setText(calendar.getLocation());
            dateField.setText(calendar.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            onlineCheckBox.setSelected(calendar.isOnline());
            return "Update Calendar Entry";
        } else {
            dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            return "Add New Calendar Entry";
        }
    }

    private JPanel createDialogPanel(JTextField addressField, JTextField locationField,
                                     JTextField dateField, JCheckBox onlineCheckBox) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        JPanel panel = new JPanel(new GridBagLayout());

        addDialogField(panel, "Address:", addressField, 0, gbc);
        addDialogField(panel, "Location:", locationField, 1, gbc);
        addDialogField(panel, "Date (YYYY-MM-DD):", dateField, 2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(onlineCheckBox, gbc);

        return panel;
    }

    private void addDialogField(JPanel panel, String label, JTextField field,
                                int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void processDialogResult(Calendar calendar, JTextField addressField,
                                     JTextField locationField, JTextField dateField,
                                     JCheckBox onlineCheckBox) {
        try {
            String address = addressField.getText();
            String location = locationField.getText();

            if (!isValidInput(address, location)) {
                return;
            }

            LocalDate date = parseDate(dateField.getText());
            boolean isOnline = onlineCheckBox.isSelected();

            saveOrUpdateCalendar(calendar, address, location, date, isOnline);
            refreshTable();
        } catch (DateTimeParseException e) {
            showError("Invalid date format. Please use YYYY-MM-DD.", "Input Error");
        } catch (ServiceException e) {
            showError("Service error: " + e.getMessage(), "Error");
        }
    }

    private boolean isValidInput(String address, String location) {
        if (address.isBlank() || location.isBlank()) {
            showError("Address and Location cannot be empty.", "Input Error");
            return false;
        }
        return true;
    }

    private LocalDate parseDate(String dateText) throws DateTimeParseException {
        return LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private void saveOrUpdateCalendar(Calendar calendar, String address, String location,
                                      LocalDate date, boolean isOnline) throws ServiceException {
        if (calendar == null) {
            calendarService.addCalendar(new Calendar(address, location, date, isOnline));
        } else {
            calendar.setAddress(address);
            calendar.setLocation(location);
            calendar.setDate(date);
            calendar.setOnline(isOnline);
            calendarService.updateCalendar(calendar);
        }
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
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
                    calendar.isOnline() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }

    private void onAddButtonClicked() {
        showCalendarDialog(null);
    }

    private void onUpdateButtonClicked() {
        int selectedRow = calendarTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                Calendar calendarToUpdate = calendarService.getCalendarById(id);
                showCalendarDialog(calendarToUpdate);
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: The selected item could not be found.",
                        "Not Found", JOptionPane.ERROR_MESSAGE);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a calendar entry to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDeleteButtonClicked() {
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
            JOptionPane.showMessageDialog(this,
                    "Please select a calendar entry to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

}