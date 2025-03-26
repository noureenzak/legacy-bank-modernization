//added
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe bank account implementation with transaction history
 */
public class BankAccount {
    private double balance;
   //added initializations
   // Using ReentrantLock instead of synchronized for better control
    private final ReentrantLock lock = new ReentrantLock();
    // Stores all transaction records as strings
    private final List<String> transactionHistory = new ArrayList<>();

     /**
     * Creates a new bank account with specified initial balance
     * @param initialBalance Starting balance (must be >= 0)
     * @throws IllegalArgumentException if initialBalance is negative
     */
    public BankAccount(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.balance = initialBalance;
        transactionHistory.add("Account created with initial balance: " + initialBalance);
    }

    /**
     * Deposits specified amount into account
     * @param amount Positive amount to deposit
     * @throws IllegalArgumentException if amount is <= 0
     */
    public void deposit(double amount) {
        // Validate input
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        lock.lock();
        try {
            // Perform deposit
            balance += amount;
            String transaction = String.format("Deposited: +%.2f | Balance: %.2f", amount, balance);
            transactionHistory.add(transaction);
            System.out.println(transaction);
        } finally {
            // Ensure lock is released even if exception occurs
            lock.unlock();
        }
    }

    /**
     * Withdraws specified amount from account
     * @param amount Positive amount to withdraw
     * @throws IllegalArgumentException if amount is <= 0
     * @throws IllegalStateException if insufficient funds
     */
    public void withdraw(double amount) {
        // Validate input
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        lock.lock();
        try {
            // Check sufficient funds
            if (balance >= amount) {
                balance -= amount;
                String transaction = String.format("Withdrawn: -%.2f | Balance: %.2f", amount, balance);
                transactionHistory.add(transaction);
                System.out.println(transaction);
            } else {
                String message = String.format("Withdrawal failed: Insufficient funds for %.2f", amount);
                transactionHistory.add(message);
                System.out.println(message);
                throw new IllegalStateException(message);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return Current account balance
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
     * @return Copy of all transaction history (thread-safe)
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
     * @return Last transaction or "No transactions yet"
     */
    public String getLastTransaction() {
        lock.lock();
        try {
            return transactionHistory.isEmpty() ? "No transactions yet" 
                   : transactionHistory.get(transactionHistory.size() - 1);
        } finally {
            lock.unlock();
        }
    }
}
