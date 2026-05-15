package src.model.ui;

import src.model.model.Account;
import src.model.services.AccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {

    private final AccountService accountService = new AccountService();
    private final JFrame parent;
    private final Account account;

    private JLabel balanceLabel;
    private JTextField amountField;

    public DashboardPanel(JFrame parent, Account account) {
        this.parent  = parent;
        this.account = account;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(25, 35, 55));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildNavBar(),    BorderLayout.SOUTH);
    }


    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(30, 45, 75));
        p.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel welcome = new JLabel("Welcome, " + account.getFullName());
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcome.setForeground(new Color(140, 180, 255));

        JLabel accNum = new JLabel("Acc: " + account.getAccountNumber());
        accNum.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accNum.setForeground(new Color(130, 150, 180));

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logout.setBackground(new Color(180, 50, 50));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.setOpaque(true);
        logout.setBorderPainted(false);
        logout.setBorder(new EmptyBorder(6, 14, 6, 14));
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> doLogout());

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(welcome);
        left.add(accNum);

        p.add(left, BorderLayout.WEST);
        p.add(logout, BorderLayout.EAST);
        return p;
    }

    // ─── Balance Card + Quick Actions ───────────────────────────────────────

    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setOpaque(false);

        // Balance card
        JPanel card = new JPanel(new GridLayout(3, 1, 4, 4));
        card.setBackground(new Color(40, 60, 100));
        card.setBorder(new EmptyBorder(24, 30, 24, 30));

        JLabel balTitle = new JLabel("Current Balance", SwingConstants.CENTER);
        balTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        balTitle.setForeground(new Color(150, 180, 220));

        balanceLabel = new JLabel(formatBalance(), SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        balanceLabel.setForeground(new Color(80, 200, 120));

        JButton refreshBtn = new JButton("Refresh Balance");
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshBtn.setBackground(new Color(50, 80, 130));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setBorder(new EmptyBorder(6, 14, 6, 14));
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshBalance());

        card.add(balTitle);
        card.add(balanceLabel);
        card.add(refreshBtn);
        p.add(card, BorderLayout.NORTH);

        // Quick actions
        p.add(buildQuickActions(), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildQuickActions() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(35, 50, 75));
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 6, 8, 6);

        JLabel amtLabel = new JLabel("Amount (₹):");
        amtLabel.setForeground(new Color(170, 195, 230));
        amtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        amountField = new JTextField(15);
        amountField.setBackground(new Color(55, 70, 95));
        amountField.setForeground(Color.WHITE);
        amountField.setCaretColor(Color.WHITE);
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 110, 160)),
            new EmptyBorder(6, 8, 6, 8)));
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton depositBtn  = styledButton("Deposit",  new Color(40, 160, 90));
        JButton withdrawBtn = styledButton("Withdraw", new Color(200, 96, 40));

        depositBtn.addActionListener(e  -> doDeposit());
        withdrawBtn.addActionListener(e -> doWithdraw());

        c.gridx = 0; c.gridy = 0; p.add(amtLabel, c);
        c.gridx = 1;               p.add(amountField, c);
        c.gridx = 0; c.gridy = 1; p.add(depositBtn, c);
        c.gridx = 1;               p.add(withdrawBtn, c);

        return p;
    }


    private JPanel buildNavBar() {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 0));
        p.setOpaque(false);

        JButton transferBtn = styledButton("Fund Transfer", new Color(60, 100, 200));
        JButton historyBtn  = styledButton("Transaction History", new Color(120, 60, 200));

        transferBtn.addActionListener(e -> switchTo(new TransferPanel(parent, account)));
        historyBtn.addActionListener(e  -> switchTo(new HistoryPanel(parent, account)));

        p.add(transferBtn);
        p.add(historyBtn);
        return p;
    }


    private void doDeposit() {
        BigDecimal amount = parseAmount();
        if (amount == null) return;
        try {
            boolean ok = accountService.deposit(account, amount);
            if (ok) {
                refreshBalance();
                JOptionPane.showMessageDialog(this, "Deposit successful! +" + formatMoney(amount));
            } else {
                showError("Invalid amount.");
            }
        } catch (SQLException ex) {
            showError("DB error: " + ex.getMessage());
        }
        amountField.setText("");
    }

    private void doWithdraw() {
        BigDecimal amount = parseAmount();
        if (amount == null) return;
        try {
            boolean ok = accountService.withdraw(account, amount);
            if (ok) {
                refreshBalance();
                JOptionPane.showMessageDialog(this, "Withdrawal successful! -" + formatMoney(amount));
            } else {
                showError("Insufficient balance.");
            }
        } catch (SQLException ex) {
            showError("DB error: " + ex.getMessage());
        }
        amountField.setText("");
    }

    private void refreshBalance() {
        try {
            BigDecimal bal = accountService.getBalance(account.getAccountId());
            account.setBalance(bal);
            balanceLabel.setText(formatBalance());
        } catch (SQLException ex) {
            showError("Could not refresh balance.");
        }
    }

    private void doLogout() {
        parent.getContentPane().removeAll();
        parent.getContentPane().add(new LoginPanel(parent));
        parent.revalidate();
        parent.repaint();
    }

    private void switchTo(JPanel panel) {
        parent.getContentPane().removeAll();
        parent.getContentPane().add(panel);
        parent.revalidate();
        parent.repaint();
    }


    private BigDecimal parseAmount() {
        try {
            BigDecimal amt = new BigDecimal(amountField.getText().trim());
            if (amt.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
            return amt;
        } catch (NumberFormatException e) {
            showError("Enter a valid positive number.");
            return null;
        }
    }

    private String formatBalance() {
        return "₹ " + String.format("%,.2f", account.getBalance());
    }

    private String formatMoney(BigDecimal val) {
        return "₹ " + String.format("%,.2f", val);
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
