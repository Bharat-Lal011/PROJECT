package src.model.services;

import src.model.DBConnection;
import src.model.model.Account;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Handles user authentication and account registration.
 */
public class AuthService {

    /**
     * Hashes a plain-text password using SHA-256.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Generates a unique 12-digit account number.
     */
    private String generateAccountNumber() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String number;
        do {
            number = "10" + String.format("%010d", (long)(Math.random() * 1_000_000_0000L));
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM accounts WHERE account_number = ?");
            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) break;
        } while (true);
        return number;
    }

    /**
     * Registers a new bank account.
     * @return the created Account, or null if email already exists.
     */
    public Account register(String fullName, String email, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();

        // Check for existing email
        PreparedStatement check = conn.prepareStatement(
            "SELECT COUNT(*) FROM accounts WHERE email = ?");
        check.setString(1, email);
        ResultSet rs = check.executeQuery();
        rs.next();
        if (rs.getInt(1) > 0) return null;

        String accountNumber = generateAccountNumber();
        String hash = hashPassword(password);

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO accounts (account_number, full_name, email, password_hash, balance) VALUES (?,?,?,?,?)",
            Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, accountNumber);
        ps.setString(2, fullName);
        ps.setString(3, email);
        ps.setString(4, hash);
        ps.setBigDecimal(5, BigDecimal.ZERO);
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        int id = keys.getInt(1);

        Account account = new Account();
        account.setAccountId(id);
        account.setAccountNumber(accountNumber);
        account.setFullName(fullName);
        account.setEmail(email);
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    /**
     * Logs in an existing user.
     * @return the Account if credentials match, else null.
     */
    public Account login(String email, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String hash = hashPassword(password);

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM accounts WHERE email = ? AND password_hash = ?");
        ps.setString(1, email);
        ps.setString(2, hash);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Account acc = new Account();
            acc.setAccountId(rs.getInt("account_id"));
            acc.setAccountNumber(rs.getString("account_number"));
            acc.setFullName(rs.getString("full_name"));
            acc.setEmail(rs.getString("email"));
            acc.setBalance(rs.getBigDecimal("balance"));
            acc.setCreatedAt(rs.getTimestamp("created_at"));
            return acc;
        }
        return null;
    }
}
