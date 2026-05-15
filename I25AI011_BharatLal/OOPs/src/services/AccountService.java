package src.model.services;

import src.model.DBConnection;
import src.model.model.Account;
import src.model.model.Transaction;
import src.model.model.Transaction.Type;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all banking operations: deposit, withdrawal, transfer, history, balance.
 */
public class AccountService {

    /**
     * Deposits an amount into the account.
     * @return true if successful.
     */
    public boolean deposit(Account account, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        Connection conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement update = conn.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE account_id = ?");
            update.setBigDecimal(1, amount);
            update.setInt(2, account.getAccountId());
            update.executeUpdate();

            logTransaction(conn, account.getAccountId(), Type.DEPOSIT, amount, "Deposit");

            conn.commit();
            account.setBalance(account.getBalance().add(amount));
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Withdraws an amount from the account.
     * @return true if successful, false if insufficient funds.
     */
    public boolean withdraw(Account account, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (account.getBalance().compareTo(amount) < 0) return false;

        Connection conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement update = conn.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE account_id = ?");
            update.setBigDecimal(1, amount);
            update.setInt(2, account.getAccountId());
            update.executeUpdate();

            logTransaction(conn, account.getAccountId(), Type.WITHDRAWAL, amount, "Withdrawal");

            conn.commit();
            account.setBalance(account.getBalance().subtract(amount));
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Transfers funds from the logged-in account to another account by account number.
     * @return "SUCCESS", "INSUFFICIENT_FUNDS", or "RECIPIENT_NOT_FOUND".
     */
    public String transfer(Account sender, String recipientAccountNumber, BigDecimal amount)
            throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return "INVALID_AMOUNT";
        if (sender.getBalance().compareTo(amount) < 0) return "INSUFFICIENT_FUNDS";

        Connection conn = DBConnection.getConnection();

        // Find recipient
        PreparedStatement find = conn.prepareStatement(
            "SELECT account_id, full_name FROM accounts WHERE account_number = ?");
        find.setString(1, recipientAccountNumber);
        ResultSet rs = find.executeQuery();
        if (!rs.next()) return "RECIPIENT_NOT_FOUND";

        int recipientId = rs.getInt("account_id");
        String recipientName = rs.getString("full_name");

        if (recipientId == sender.getAccountId()) return "SELF_TRANSFER";

        conn.setAutoCommit(false);
        try {
            // Debit sender
            PreparedStatement debit = conn.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE account_id = ?");
            debit.setBigDecimal(1, amount);
            debit.setInt(2, sender.getAccountId());
            debit.executeUpdate();

            // Credit recipient
            PreparedStatement credit = conn.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE account_id = ?");
            credit.setBigDecimal(1, amount);
            credit.setInt(2, recipientId);
            credit.executeUpdate();

            // Log for sender
            logTransaction(conn, sender.getAccountId(), Type.TRANSFER_OUT, amount,
                "Transfer to " + recipientName + " (" + recipientAccountNumber + ")");

            // Log for recipient
            logTransaction(conn, recipientId, Type.TRANSFER_IN, amount,
                "Transfer from " + sender.getFullName() + " (" + sender.getAccountNumber() + ")");

            conn.commit();
            sender.setBalance(sender.getBalance().subtract(amount));
            return "SUCCESS";
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Returns the latest balance of the account from the database.
     */
    public BigDecimal getBalance(int accountId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT balance FROM accounts WHERE account_id = ?");
        ps.setInt(1, accountId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getBigDecimal("balance");
        return BigDecimal.ZERO;
    }

    /**
     * Returns the transaction history for an account (most recent first).
     */
    public List<Transaction> getHistory(int accountId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC LIMIT 50");
        ps.setInt(1, accountId);
        ResultSet rs = ps.executeQuery();

        List<Transaction> list = new ArrayList<>();
        while (rs.next()) {
            Transaction t = new Transaction();
            t.setTransactionId(rs.getInt("transaction_id"));
            t.setAccountId(rs.getInt("account_id"));
            t.setType(Type.valueOf(rs.getString("type")));
            t.setAmount(rs.getBigDecimal("amount"));
            t.setDescription(rs.getString("description"));
            t.setTransactionDate(rs.getTimestamp("transaction_date"));
            list.add(t);
        }
        return list;
    }

    /**
     * Helper: inserts a transaction record.
     */
    private void logTransaction(Connection conn, int accountId, Type type,
                                 BigDecimal amount, String description) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO transactions (account_id, type, amount, description) VALUES (?,?,?,?)");
        ps.setInt(1, accountId);
        ps.setString(2, type.name());
        ps.setBigDecimal(3, amount);
        ps.setString(4, description);
        ps.executeUpdate();
    }
}
