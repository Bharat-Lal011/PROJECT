package src.model.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class representing a bank account.
 */
public class Account {

    private int accountId;
    private String accountNumber;
    private String fullName;
    private String email;
    private BigDecimal balance;
    private Timestamp createdAt;

    public Account() {}

    public Account(int accountId, String accountNumber, String fullName,
                   String email, BigDecimal balance, Timestamp createdAt) {
        this.accountId     = accountId;
        this.accountNumber = accountNumber;
        this.fullName      = fullName;
        this.email         = email;
        this.balance       = balance;
        this.createdAt     = createdAt;
    }

    // Getters and Setters
    public int getAccountId()           { return accountId; }
    public void setAccountId(int id)    { this.accountId = id; }

    public String getAccountNumber()                    { return accountNumber; }
    public void setAccountNumber(String accountNumber)  { this.accountNumber = accountNumber; }

    public String getFullName()             { return fullName; }
    public void setFullName(String name)    { this.fullName = name; }

    public String getEmail()            { return email; }
    public void setEmail(String email)  { this.email = email; }

    public BigDecimal getBalance()              { return balance; }
    public void setBalance(BigDecimal balance)  { this.balance = balance; }

    public Timestamp getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)   { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Account[" + accountNumber + " | " + fullName + " | $" + balance + "]";
    }
}
