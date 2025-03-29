import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe bank account implementation with transaction history
 */
public class BankAccount {
    private double balance;

    // Lock for thread safety
    private final ReentrantLock lock = new ReentrantLock();

    // List to store transaction history
    private final List<String> transactionHistory = new ArrayList<>();

    /**
     * Constructor for BankAccount
     * @param initialBalance Must be >= 0
     * @throws IllegalArgumentException if initial balance is negative
     */
    public BankAccount(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.balance = initialBalance;
        transactionHistory.add("Account created with initial balance: " + initialBalance);
    }

    /**
     * Deposits a positive amount into the account
     * @param amount Amount to deposit
     * @throws IllegalArgumentException if amount is non-positive
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        lock.lock();
        try {
            balance += amount;
            String transaction = String.format("Deposited: +%.2f | Balance: %.2f", amount, balance);
            transactionHistory.add(transaction);
            // System.out.println(transaction); // Optional: for debugging
        } finally {
            lock.unlock();
        }
    }

    /**
     * Withdraws a positive amount from the account
     * @param amount Amount to withdraw
     * @throws IllegalArgumentException if amount is non-positive
     * @throws IllegalStateException if balance is insufficient
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                String transaction = String.format("Withdrawn: -%.2f | Balance: %.2f", amount, balance);
                transactionHistory.add(transaction);
                // System.out.println(transaction); // Optional: for debugging
            } else {
                String message = String.format("Withdrawal failed: Insufficient funds for %.2f", amount);
                transactionHistory.add(message);
                // System.out.println(message); // Optional
                throw new IllegalStateException(message);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return Current balance (thread-safe)
     */
    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return A copy of all transaction history
     */
    public List<String> getTransactionHistory() {
        lock.lock();
        try {
            return new ArrayList<>(transactionHistory);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return Last transaction string or a default message
     */
    public String getLastTransaction() {
        lock.lock();
        try {
            return transactionHistory.isEmpty()
                ? "No transactions yet"
                : transactionHistory.get(transactionHistory.size() - 1);
        } finally {
            lock.unlock();
        }
    }
}
