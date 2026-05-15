package src.model.ui;

import src.model.model.Account;
import src.model.model.Transaction;
import src.model.services.AccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel displaying the last 50 transactions in a table.
 */
public class HistoryPanel extends JPanel {

    private final AccountService accountService = new AccountService();
    private final JFrame parent;
    private final Account account;

    public HistoryPanel(JFrame parent, Account account) {
        this.parent  = parent;
        this.account = account;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(25, 35, 55));
        setBorder(new EmptyBorder(20, 24, 20, 24));

        add(buildHeader(),         BorderLayout.NORTH);
        add(buildTransactionTable(), BorderLayout.CENTER);
        add(buildBack(),           BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("Transaction History", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(100, 180, 255));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel sub = new JLabel("Account: " + account.getAccountNumber() + "  |  " + account.getFullName(),
            SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(130, 155, 185));

        p.add(title, BorderLayout.CENTER);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    private JScrollPane buildTransactionTable() {
        String[] cols = {"Date & Time", "Type", "Amount (₹)", "Description"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            List<Transaction> transactions = accountService.getHistory(account.getAccountId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            for (Transaction t : transactions) {
                String sign   = (t.getType() == Transaction.Type.DEPOSIT ||
                                 t.getType() == Transaction.Type.TRANSFER_IN) ? "+" : "-";
                String amount = sign + String.format("%,.2f", t.getAmount());
                model.addRow(new Object[]{
                    sdf.format(t.getTransactionDate()),
                    t.getType().toString().replace("_", " "),
                    amount,
                    t.getDescription()
                });
            }
        } catch (SQLException ex) {
            model.addRow(new Object[]{"Error", "Could not load", ex.getMessage(), ""});
        }

        JTable table = new JTable(model);
        table.setBackground(new Color(35, 50, 75));
        table.setForeground(new Color(200, 215, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(new Color(50, 70, 110));
        table.getTableHeader().setForeground(new Color(160, 200, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(60, 90, 140));

        // Color-code amount column
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.RIGHT);
                String v = value.toString();
                if (v.startsWith("+")) setForeground(new Color(80, 200, 120));
                else                   setForeground(new Color(230, 100, 80));
                if (sel) setBackground(new Color(60, 90, 140));
                else     setBackground(new Color(35, 50, 75));
                return this;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(155);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(280);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(new Color(35, 50, 75));
        scroll.getViewport().setBackground(new Color(35, 50, 75));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 85, 130)));
        return scroll;
    }

    private JPanel buildBack() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JButton back = new JButton("← Back to Dashboard");
        back.setFont(new Font("Segoe UI", Font.BOLD, 12));
        back.setBackground(new Color(80, 80, 100));
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setBorder(new EmptyBorder(8, 14, 8, 14));
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new DashboardPanel(parent, account));
            parent.revalidate();
            parent.repaint();
        });
        p.add(back);
        return p;
    }
}
