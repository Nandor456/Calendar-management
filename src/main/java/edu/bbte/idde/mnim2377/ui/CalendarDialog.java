package edu.bbte.idde.mnim2377.ui;

import edu.bbte.idde.mnim2377.data.model.Calendar;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class CalendarDialog extends JDialog {

    private final JTextField addressField = new JTextField(20);
    private final JTextField locationField = new JTextField(20);
    private final JTextField dateField = new JTextField(10);
    private final JCheckBox onlineCheckBox = new JCheckBox("Online event");

    private transient Calendar calendar;
    private boolean confirmed;

    public CalendarDialog(Frame owner, String title, Calendar calendar) {
        super(owner, title, true);
        this.calendar = calendar;
        this.confirmed = false;

        initComponents(owner);
    }

    private void initComponents(Frame owner) {
        setLayout(new BorderLayout(10, 10));
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        populateFields();

        pack(); // Sizes the dialog to fit its components
        setLocationRelativeTo(owner); // Centers the dialog relative to the main app
    }


    public boolean isConfirmed() {
        return confirmed;
    }

    public Calendar getCalendar() {
        return calendar;
    }


    private void populateFields() {
        if (calendar != null) {
            addressField.setText(calendar.getAddress());
            locationField.setText(calendar.getLocation());
            dateField.setText(calendar.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            onlineCheckBox.setSelected(calendar.isOnline());
        } else {
            dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    private JPanel createFormPanel() {
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

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());

        panel.add(okButton);
        panel.add(cancelButton);
        return panel;
    }

    private void onOk() {
        try {
            String address = addressField.getText();
            String location = locationField.getText();
            if (address.isBlank() || location.isBlank()) {
                showError("Address and Location cannot be empty.");
                return;
            }

            LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            boolean isOnline = onlineCheckBox.isSelected();

            if (this.calendar == null) {
                this.calendar = new Calendar(address, location, date, isOnline);
            } else {
                this.calendar.setAddress(address);
                this.calendar.setLocation(location);
                this.calendar.setDate(date);
                this.calendar.setOnline(isOnline);
            }

            this.confirmed = true;
            setVisible(false);
            dispose();

        } catch (DateTimeParseException e) {
            showError("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private void onCancel() {
        this.confirmed = false;
        setVisible(false);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}