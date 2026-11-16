package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import logic.ReservationManager;
import model.ClassReservationSlot;
import model.IndividualStudentSlot;
import model.InvalidSlotException;
import model.Lab;
import model.Reservation;
import model.TimeSlot;

public class BookingPanel extends JPanel {
    private final ReservationManager manager;
    private int nextId;

    private final JTextField txtReservedBy = new JTextField(15);
    private final JComboBox<String> cboLab = new JComboBox<>();
    private final JComboBox<String> cboType =
        new JComboBox<>(new String[]{"Class Reservation", "Individual Reservation"});
    private final JLabel lblDetail = new JLabel("Course code:");
    private final JTextField txtDetail = new JTextField(10);   // course code or student ID
    private final JTextField txtStart = new JTextField(8);
    private final JTextField txtEnd = new JTextField(8);
    private final JButton btnReserve = new JButton("Reserve");

    private final DefaultListModel<Reservation> listModel = new DefaultListModel<>();
    private final JList<Reservation> lstReservations = new JList<>(listModel);
    private final JButton btnCancel = new JButton("Cancel Selected");
    private final JLabel lblStatus = new JLabel("Ready.");

    public BookingPanel(ReservationManager manager) {
        this.manager = manager;
        this.nextId = manager.getAllReservations().size() + 1;

        setLayout(new BorderLayout(8, 8));
        setBorder(UIConstants.PADDING);
        setBackground(UIConstants.BACKGROUND);

        for (Lab lab : manager.getAllLabs()) {
            cboLab.addItem(lab.getLabId());
        }

        // Form
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setOpaque(false);
        addRow(form, new JLabel("Reserved by:"), txtReservedBy);
        addRow(form, new JLabel("Computer Lab:"), cboLab);
        addRow(form, new JLabel("Type:"), cboType);
        addRow(form, lblDetail, txtDetail);
        addRow(form, new JLabel("Start (HH:mm):"), txtStart);
        addRow(form, new JLabel("End (HH:mm):"), txtEnd);
        form.add(new JLabel());
        form.add(btnReserve);

        // Style
        UIConstants.styleButton(btnReserve);
        UIConstants.styleButton(btnCancel);
        lblStatus.setFont(UIConstants.BODY_FONT);
        lstReservations.setFont(UIConstants.BODY_FONT);

        // Show each reservation as text (works even without Reservation.toString())
        lstReservations.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Reservation) {
                    setText(describe((Reservation) value));
                }
                return this;
            }
        });

        // Buttons stay disabled until they can work
        btnReserve.setEnabled(false);
        btnCancel.setEnabled(false);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { updateButton(); }
            public void removeUpdate(DocumentEvent e)  { updateButton(); }
            public void changedUpdate(DocumentEvent e) { updateButton(); }
        };
        txtReservedBy.getDocument().addDocumentListener(dl);
        txtDetail.getDocument().addDocumentListener(dl);
        txtStart.getDocument().addDocumentListener(dl);
        txtEnd.getDocument().addDocumentListener(dl);

        cboType.addActionListener(e -> lblDetail.setText(
            cboType.getSelectedIndex() == 0 ? "Course code:" : "Student ID:"));

        lstReservations.addListSelectionListener(e ->
            btnCancel.setEnabled(lstReservations.getSelectedValue() != null));

        btnReserve.addActionListener(e -> onReserve());
        btnCancel.addActionListener(e -> onCancel());

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(btnCancel, BorderLayout.NORTH);
        bottom.add(lblStatus, BorderLayout.SOUTH);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(lstReservations), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        refreshList();
    }

    private void addRow(JPanel form, JLabel label, Component field) {
        label.setFont(UIConstants.BODY_FONT);
        form.add(label);
        form.add(field);
    }

    private void updateButton() {
        btnReserve.setEnabled(
            !txtReservedBy.getText().trim().isEmpty()
            && !txtDetail.getText().trim().isEmpty()
            && !txtStart.getText().trim().isEmpty()
            && !txtEnd.getText().trim().isEmpty()
            && cboLab.getItemCount() > 0);
    }

    private void onReserve() {
        try {
            String labId = (String) cboLab.getSelectedItem();
            Lab lab = manager.findLabById(labId);

            String start = txtStart.getText().trim();
            String end = txtEnd.getText().trim();
            String detail = txtDetail.getText().trim();

            // Validate the time format and order
            try {
                if (!LocalTime.parse(end).isAfter(LocalTime.parse(start))) {
                    throw new InvalidSlotException("End time must be after start time.");
                }
            } catch (DateTimeParseException ex) {
                throw new InvalidSlotException("Use 24-hour HH:mm format, e.g. 09:00, 13:00 = 1:00PM.");
            }

            // Slot id includes lab and times, because createReservation compares slot ids
            String slotId = labId + "_" + start + "-" + end;

            // Polymorphism: both subclasses are stored as the parent type TimeSlot
            TimeSlot slot;
            if (cboType.getSelectedIndex() == 0) {
                slot = new ClassReservationSlot(slotId, start, end, detail);
            } else {
                slot = new IndividualStudentSlot(slotId, start, end, detail);
            }

            String id = "R" + nextId;
            manager.createReservation(id, lab, slot, txtReservedBy.getText().trim());
            nextId++;

            refreshList();
            txtReservedBy.setText("");
            txtDetail.setText("");
            txtStart.setText("");
            txtEnd.setText("");
            setStatus("Reservation " + id + " saved.", UIConstants.SUCCESS);
        } catch (InvalidSlotException ex) {
            setStatus(ex.getMessage(), UIConstants.ERROR);
        }
    }

    private void onCancel() {
        Reservation selected = lstReservations.getSelectedValue();
        if (selected == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
            "Cancel reservation " + selected.getReservationId() + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (manager.cancelReservation(selected.getReservationId())) {
                refreshList();
                setStatus("Reservation cancelled.", UIConstants.SUCCESS);
            } else {
                setStatus("Reservation not found.", UIConstants.ERROR);
            }
        }
    }

    // Call this after loading data from a file
    public void refreshList() {
        listModel.clear();
        for (Reservation r : manager.getAllReservations()) {
            listModel.addElement(r);
        }
        nextId = manager.getAllReservations().size() + 1;
    }

    private String describe(Reservation r) {
        TimeSlot s = r.getTimeSlot();
        return r.getReservationId() + " | " + r.getLab().getLabId()
            + " | " + s.getStartTime() + "-" + s.getEndTime()
            + " | " + s.getReservationType()   // overridden method: differs per subclass
            + " | " + r.getReservedBy();
    }

    private void setStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }
}