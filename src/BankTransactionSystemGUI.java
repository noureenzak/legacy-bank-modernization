import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI for bank transaction system with thread-safe updates
 */
public class BankTransactionSystemGUI {
    private static BankAccount account = new BankAccount(1000);
    private static JTextArea transactionLog;

    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame("Bank Transaction System");
        frame.setSize(600, 400); // Increased size for transaction log
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Transaction panel (left)
        JPanel transactionPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        // Transaction components
        JTextField depositField = new JTextField(10);
        JTextField withdrawField = new JTextField(10);
        JLabel balanceLabel = new JLabel("Balance: " + account.getBalance());
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");

        // Add components to transaction panel
        transactionPanel.add(new JLabel("Deposit Amount:"));
        transactionPanel.add(depositField);
        transactionPanel.add(depositButton);
        transactionPanel.add(new JLabel("Withdraw Amount:"));
        transactionPanel.add(withdrawField);
        transactionPanel.add(withdrawButton);
        transactionPanel.add(new JLabel("Current Balance:"));
        transactionPanel.add(balanceLabel);

        // Transaction log (right)
        transactionLog = new JTextArea();
        transactionLog.setEditable(false);
        transactionLog.setText("Transaction History:\n");
        JScrollPane scrollPane = new JScrollPane(transactionLog);

        // Add panels to main frame
        mainPanel.add(transactionPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(mainPanel);

        // Deposit action with error handling
        depositButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(depositField.getText());
                new Thread(() -> {
                    try {
                        account.deposit(amount);
                        // Update UI on Swing thread
                        SwingUtilities.invokeLater(() -> {
                            balanceLabel.setText(String.format("Balance: %.2f", account.getBalance()));
                            transactionLog.append(account.getLastTransaction() + "\n");
                        });
                    } catch (IllegalArgumentException ex) {
                        SwingUtilities.invokeLater(() -> 
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                    }
                }).start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Withdraw action with error handling
        withdrawButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(withdrawField.getText());
                new Thread(() -> {
                    try {
                        account.withdraw(amount);
                        SwingUtilities.invokeLater(() -> {
                            balanceLabel.setText(String.format("Balance: %.2f", account.getBalance()));
                            transactionLog.append(account.getLastTransaction() + "\n");
                        });
                    } catch (IllegalArgumentException | IllegalStateException ex) {
                        SwingUtilities.invokeLater(() -> 
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                    }
                }).start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    /**
     * Helper method to update transaction log
     */
    private static void updateTransactionLog(String message) {
        transactionLog.append(message + "\n");
    }
}
