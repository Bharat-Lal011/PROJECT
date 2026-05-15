-- ============================================
-- Banking System Database Schema
-- Run this in MySQL before starting the app
-- ============================================

CREATE DATABASE IF NOT EXISTS banking_system;
USE banking_system;

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_id    INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(12) UNIQUE NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    balance       DECIMAL(15, 2) DEFAULT 0.00,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    account_id       INT NOT NULL,
    type             ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount           DECIMAL(15, 2) NOT NULL,
    description      VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Sample account (password: password123)
INSERT INTO accounts (account_number, full_name, email, password_hash, balance)
VALUES ('100000000001', 'Demo User', 'demo@bank.com',
        SHA2('password123', 256), 10000.00);
