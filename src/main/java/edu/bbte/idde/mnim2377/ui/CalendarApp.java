package edu.bbte.idde.mnim2377.ui;

import edu.bbte.idde.mnim2377.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.data.model.Calendar;
import edu.bbte.idde.mnim2377.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.ui.exception.UiException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
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

        initializeUI();
        refreshTable();
        setVisible(true);
    }

    private void initializeUI() {
        setTitle("Calendar Management App");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table Model
        String[] columnNames = {"ID", "Address", "Location", "Date", "isOnline"};
        tableModel = new NonEditableTableModel(columnNames, 0);

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
        CalendarDialog dialog = new CalendarDialog(this, "Add New Calendar Entry", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            calendarService.addCalendar(dialog.getCalendar());
            refreshTable();
        }
    }

    private void onUpdateButtonClicked() {
        int selectedRow = calendarTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a calendar entry to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Calendar calendarToUpdate = calendarService.getCalendarById(id);

            if (calendarToUpdate == null) {
                throw new UiException("Selected item no longer exists.");
            }

            CalendarDialog dialog = new CalendarDialog(this, "Update Calendar Entry", calendarToUpdate);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                calendarService.updateCalendar(dialog.getCalendar());
                refreshTable();
            }

        } catch (ServiceException ex) {
            showError("Error updating item: " + ex.getMessage(), "Error");
            refreshTable(); // Refresh in case the item was deleted by another user
        }
    }

    private void onDeleteButtonClicked() {
        int selectedRow = calendarTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a calendar entry to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
                showError("Error: Could not delete the item. " + ex.getMessage(), "Error");
            }
        }
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private static class NonEditableTableModel extends DefaultTableModel {

        public NonEditableTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            // Always return false to make the table read-only
            return false;
        }
    }
}