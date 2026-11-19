package ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logic.ReservationManager;

public class LabReportPanel extends JPanel {
    private final ReservationManager manager;
    private final JTextArea txtReport = new JTextArea();
    private final JButton btnRefresh = new JButton("Refresh Report");

    public LabReportPanel(ReservationManager manager) {
        this.manager = manager;

        setLayout(new BorderLayout(8, 8));
        setBorder(UIConstants.PADDING);
        setBackground(UIConstants.BACKGROUND);

        txtReport.setEditable(false);
        txtReport.setFont(UIConstants.REPORT_FONT);   // monospaced keeps columns aligned
        txtReport.setBorder(UIConstants.PADDING);

        UIConstants.styleButton(btnRefresh);
        btnRefresh.addActionListener(e -> refresh());

        add(new JScrollPane(txtReport), BorderLayout.CENTER);
        add(btnRefresh, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        txtReport.setText(manager.generateDailyReport());
        txtReport.setCaretPosition(0);
    }
}