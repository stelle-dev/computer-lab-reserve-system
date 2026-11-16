package ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class UIConstants {
    // colors
    public static final Color BACKGROUND = Color.decode("#FFF2D0");
    public static final Color PRIMARY    = Color.decode("#3E668F");

    public static final Color TEXT       = new Color(35, 45, 70);
    public static final Color HINT       = new Color(90, 105, 140);
    public static final Color SUCCESS    = new Color(30, 120, 70);
    public static final Color ERROR      = new Color(170, 40, 40);

    // fonts
    public static final Font TITLE_FONT  = new Font(Font.MONOSPACED, Font.BOLD, 20);
    public static final Font BODY_FONT   = new Font(Font.MONOSPACED, Font.BOLD, 13);
    public static final Font REPORT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 13);

    // padding
    public static final Border PADDING = BorderFactory.createEmptyBorder(12, 12, 12, 12);

    private UIConstants() {}

    public static void styleButton(JButton button) {
        button.setFont(BODY_FONT);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    // Applies the body font to components we don't style by hand
    // (menus, tabs, dialogs, tooltips, fields, dropdowns, lists)
    public static void applyGlobalFont() {
        String[] keys = {
            "Label.font", "Button.font", "TextField.font", "TextArea.font",
            "ComboBox.font", "List.font", "TabbedPane.font",
            "Menu.font", "MenuItem.font", "MenuBar.font",
            "ToolTip.font", "OptionPane.messageFont", "OptionPane.buttonFont"
        };
        for (String key : keys) {
            UIManager.put(key, BODY_FONT);
        }
    }
}