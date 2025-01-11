
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    // Synchronized methods for thread-safe operations
    public synchronized void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount + " | Current Balance: " + balance);
    }

    public synchronized void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + " | Current Balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public synchronized double getBalance() {
        return balance;
    }
}
