package src.model.ui;

import src.model.model.Account;
import src.model.services.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {

    private final AuthService authService = new AuthService();
    private final JFrame parent;

    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;

    private JTextField regNameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;

    public LoginPanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 30, 50));
        setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("  SecureBank", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(255, 215, 0));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.setBackground(new Color(40, 55, 80));
        tabs.setForeground(new Color(0, 0, 0));
        UIManager.put("TabbedPane.selected", new Color(60, 100, 200));
        tabs.addTab("  Login  ", buildLoginTab());
        tabs.addTab("  Register  ", buildRegisterTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildLoginTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 45, 70));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 6, 10, 6);

        loginEmailField    = styledField(20);
        loginPasswordField = styledPasswordField();

        c.gridx = 0; c.gridy = 0; panel.add(label("Email :"), c);
        c.gridx = 1;               panel.add(loginEmailField, c);
        c.gridx = 0; c.gridy = 1; panel.add(label("Password :"), c);
        c.gridx = 1;               panel.add(loginPasswordField, c);

        JButton btn = styledButton("Login", new Color(0, 150, 100));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> doLogin());
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        c.insets = new Insets(20, 6, 8, 6);
        panel.add(btn, c);
        return panel;
    }

    private void doLogin() {
        String email    = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) { showError("Please fill in all fields."); return; }
        try {
            Account account = authService.login(email, password);
            if (account == null) showError("Invalid email or password.");
            else switchToDashboard(account);
        } catch (SQLException ex) { showError("Database error: " + ex.getMessage()); }
    }

    private JPanel buildRegisterTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 45, 70));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 6, 10, 6);

        regNameField     = styledField(20);
        regEmailField    = styledField(20);
        regPasswordField = styledPasswordField();

        c.gridx = 0; c.gridy = 0; panel.add(label("Full Name :"), c);
        c.gridx = 1;               panel.add(regNameField, c);
        c.gridx = 0; c.gridy = 1; panel.add(label("Email :"), c);
        c.gridx = 1;               panel.add(regEmailField, c);
        c.gridx = 0; c.gridy = 2; panel.add(label("Password :"), c);
        c.gridx = 1;               panel.add(regPasswordField, c);

        JButton btn = styledButton("Create Account", new Color(0, 120, 200));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> doRegister());
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.insets = new Insets(20, 6, 8, 6);
        panel.add(btn, c);
        return panel;
    }

    private void doRegister() {
        String name     = regNameField.getText().trim();
        String email    = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword());
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) { showError("Please fill in all fields."); return; }
        if (password.length() < 6) { showError("Password must be at least 6 characters."); return; }
        try {
            Account account = authService.register(name, email, password);
            if (account == null) showError("An account with this email already exists.");
            else {
                JOptionPane.showMessageDialog(this,
                    "Account created!\nYour account number: " + account.getAccountNumber(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                switchToDashboard(account);
            }
        } catch (SQLException ex) { showError("Database error: " + ex.getMessage()); }
    }

    private void switchToDashboard(Account account) {
        parent.getContentPane().removeAll();
        parent.getContentPane().add(new DashboardPanel(parent, account));
        parent.revalidate();
        parent.repaint();
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(255, 230, 100));
        l.setFont(new Font("Arial", Font.BOLD, 13));
        return l;
    }

    private JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        f.setCaretColor(Color.BLACK);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 200), 2),
            new EmptyBorder(6, 8, 6, 8)));
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        return f;
    }

    private JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField(20);
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        f.setCaretColor(Color.BLACK);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 200), 2),
            new EmptyBorder(6, 8, 6, 8)));
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        return f;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
