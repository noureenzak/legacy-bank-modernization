import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BankTransactionSystemGUI {
    private static final String TITLE = "Bank Transaction System";
    private static final String DEPOSIT_LABEL = "Deposit Amount:";
    private static final String WITHDRAW_LABEL = "Withdraw Amount:";
    private static final String BALANCE_LABEL = "Current Balance:";
    private static final String DEPOSIT_BUTTON = "Deposit";
    private static final String WITHDRAW_BUTTON = "Withdraw";
    private static final String TRANSACTION_HISTORY_HEADER = "Transaction History:\n";
    private static final String ERROR_TITLE = "Error";
    private static final String INVALID_NUMBER_MESSAGE = "Please enter a valid number";

    private static BankAccount account = new BankAccount(1000);
    private static JTextArea transactionLog;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame(TITLE);
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields and buttons
        JTextField depositField = new JTextField(10);
        JTextField withdrawField = new JTextField(10);
        JLabel balanceLabel = new JLabel(BALANCE_LABEL + " " + account.getBalance());
        JButton depositButton = new JButton(DEPOSIT_BUTTON);
        JButton withdrawButton = new JButton(WITHDRAW_BUTTON);

        // Deposit Row
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel(DEPOSIT_LABEL), gbc);
        gbc.gridx = 1;
        panel.add(depositField, gbc);
        gbc.gridx = 2;
        panel.add(depositButton, gbc);

        // Withdraw Row
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel(WITHDRAW_LABEL), gbc);
        gbc.gridx = 1;
        panel.add(withdrawField, gbc);
        gbc.gridx = 2;
        panel.add(withdrawButton, gbc);

        // Balance Row
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel.add(balanceLabel, gbc);

        // Transaction Log Area
        transactionLog = new JTextArea(10, 30);
        transactionLog.setEditable(false);
        transactionLog.setText(TRANSACTION_HISTORY_HEADER);
        JScrollPane scrollPane = new JScrollPane(transactionLog);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        // Action Listeners
        depositButton.addActionListener(e -> handleTransaction(depositField, true, balanceLabel));
        withdrawButton.addActionListener(e -> handleTransaction(withdrawField, false, balanceLabel));

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void handleTransaction(JTextField field, boolean isDeposit, JLabel balanceLabel) {
        try {
            double amount = Double.parseDouble(field.getText());
            new Thread(() -> {
                try {
                    if (isDeposit) {
                        account.deposit(amount);
                    } else {
                        account.withdraw(amount);
                    }
                    SwingUtilities.invokeLater(() -> {
                        balanceLabel.setText(BALANCE_LABEL + " " + String.format("%.2f", account.getBalance()));
                        transactionLog.append(account.getLastTransaction() + "\n");
                        field.setText("");
                    });
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null, ex.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, INVALID_NUMBER_MESSAGE, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
