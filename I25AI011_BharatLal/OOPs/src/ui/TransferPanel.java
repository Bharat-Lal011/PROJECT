package src.model.ui;

import src.model.model.Account;
import src.model.services.AccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Panel for transferring funds to another account.
 */
public class TransferPanel extends JPanel {

    private final AccountService accountService = new AccountService();
    private final JFrame parent;
    private final Account account;

    private JTextField recipientField;
    private JTextField amountField;

    public TransferPanel(JFrame parent, Account account) {
        this.parent  = parent;
        this.account = account;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(25, 35, 55));
        setBorder(new EmptyBorder(24, 40, 24, 40));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(),   BorderLayout.CENTER);
        add(buildBack(),   BorderLayout.SOUTH);
    }

    private JLabel pageTitle(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(new Color(100, 180, 255));
        l.setBorder(new EmptyBorder(0, 0, 16, 0));
        return l;
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel info = new JLabel("Your balance: ₹ " + String.format("%,.2f", account.getBalance()));
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(new Color(120, 200, 130));

        p.add(pageTitle("Fund Transfer"), BorderLayout.CENTER);
        p.add(info, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(35, 50, 75));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 6, 10, 6);
        c.weightx = 1;

        recipientField = styledField(20);
        amountField    = styledField(20);

        c.gridx = 0; c.gridy = 0; c.weightx = 0; p.add(label("Recipient Account No.:"), c);
        c.gridx = 1;               c.weightx = 1; p.add(recipientField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0; p.add(label("Amount (₹):"), c);
        c.gridx = 1;               c.weightx = 1; p.add(amountField, c);

        JButton btn = styledButton("Transfer Now", new Color(60, 120, 200));
        btn.addActionListener(e -> doTransfer());
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 1;
        c.insets = new Insets(24, 6, 10, 6);
        p.add(btn, c);

        return p;
    }

    private JPanel buildBack() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JButton back = styledButton("← Back to Dashboard", new Color(80, 80, 100));
        back.addActionListener(e -> switchToDashboard());
        p.add(back);
        return p;
    }

    private void doTransfer() {
        String recipient = recipientField.getText().trim();
        if (recipient.isEmpty()) {
            showError("Enter a recipient account number.");
            return;
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountField.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Enter a valid positive amount.");
            return;
        }

        try {
            String result = accountService.transfer(account, recipient, amount);
            switch (result) {
                case "SUCCESS":
                    JOptionPane.showMessageDialog(this,
                        "Transfer of ₹ " + String.format("%,.2f", amount) + " successful!");
                    recipientField.setText("");
                    amountField.setText("");
                    break;
                case "INSUFFICIENT_FUNDS":
                    showError("Insufficient balance for this transfer.");
                    break;
                case "RECIPIENT_NOT_FOUND":
                    showError("Recipient account number not found.");
                    break;
                case "SELF_TRANSFER":
                    showError("You cannot transfer to your own account.");
                    break;
                default:
                    showError("Transfer failed: " + result);
            }
        } catch (SQLException ex) {
            showError("DB error: " + ex.getMessage());
        }
    }

    private void switchToDashboard() {
        parent.getContentPane().removeAll();
        parent.getContentPane().add(new DashboardPanel(parent, account));
        parent.revalidate();
        parent.repaint();
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(170, 195, 230));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(new Color(55, 70, 95));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 110, 160)),
            new EmptyBorder(6, 8, 6, 8)));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
