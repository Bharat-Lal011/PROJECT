package src.model.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class representing a bank transaction.
 */
public class Transaction {

    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }

    private int transactionId;
    private int accountId;
    private Type type;
    private BigDecimal amount;
    private String description;
    private Timestamp transactionDate;

    public Transaction() {}

    public Transaction(int transactionId, int accountId, Type type,
                       BigDecimal amount, String description, Timestamp transactionDate) {
        this.transactionId   = transactionId;
        this.accountId       = accountId;
        this.type            = type;
        this.amount          = amount;
        this.description     = description;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getTransactionId()               { return transactionId; }
    public void setTransactionId(int id)        { this.transactionId = id; }

    public int getAccountId()                   { return accountId; }
    public void setAccountId(int accountId)     { this.accountId = accountId; }

    public Type getType()           { return type; }
    public void setType(Type type)  { this.type = type; }

    public BigDecimal getAmount()               { return amount; }
    public void setAmount(BigDecimal amount)    { this.amount = amount; }

    public String getDescription()                  { return description; }
    public void setDescription(String description)  { this.description = description; }

    public Timestamp getTransactionDate()                   { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }

    @Override
    public String toString() {
        return "[" + transactionDate + "] " + type + " $" + amount + " - " + description;
    }
}
