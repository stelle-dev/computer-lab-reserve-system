package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import logic.FileHandler;
import logic.ReservationManager;
import model.Lab;

public class MainFrame extends JFrame {
    private final ReservationManager manager = new ReservationManager();
    private final BookingPanel booking;
    private final LabReportPanel report;

    public MainFrame() {
        setTitle("Computer Lab Reservation System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(650, 550);
        setLocationRelativeTo(null);

        // Load saved data, or fall back to sample labs on first run
        try {
            if (!FileHandler.loadData(manager)) {
                manager.addLab(new Lab("LAB1", "Room 101", 30));
                manager.addLab(new Lab("LAB2", "Room 102", 40));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Could not load saved data: " + ex.getMessage(),
                "Load Error", JOptionPane.WARNING_MESSAGE);
            manager.addLab(new Lab("LAB1", "Room 101", 30));
            manager.addLab(new Lab("LAB2", "Room 102", 40));
        }

        // both panels receive the same manager
        booking = new BookingPanel(manager);
        report = new LabReportPanel(manager);
        booking.refreshList();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIConstants.BODY_FONT);
        tabs.addTab("Book a Lab", booking);
        tabs.addTab("Daily Report", report);
        tabs.addChangeListener(e -> report.refresh());   // report updates when the tab opens
        add(tabs);

        setJMenuBar(buildMenuBar());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { confirmExit(); }
        });
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> confirmExit());
        file.add(exit);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Computer Lab Reservation System\nJava and Swing final project",
            "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file);
        bar.add(help);
        return bar;
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Exit the program?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                FileHandler.saveData(manager);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error saving data: " + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}