import javax.swing.*;
import org.junit.jupiter.api.*;
import java.lang.reflect.*;

public class GUIControllerTest {
    private JTextField dummyField;
    private JLabel balanceLabel;

    @BeforeEach
    void setup() throws Exception{
        dummyField = new JTextField();
        balanceLabel = new JLabel();
        resetAccount(); 
    }

    private void resetAccount() throws Exception {
        Field accountField = BankTransactionSystemGUI.class.getDeclaredField("account");
        accountField.setAccessible(true);
        accountField.set(null, new BankAccount(100));
    }

    private void invokeHandleTransaction(String amount, boolean isDeposit) throws Exception {
        dummyField.setText(amount);
        Method method = BankTransactionSystemGUI.class.getDeclaredMethod(
            "handleTransaction", 
            JTextField.class, 
            boolean.class, 
            JLabel.class
        );
        method.setAccessible(true);
        method.invoke(null, dummyField, isDeposit, balanceLabel);
        Thread.sleep(100); 
    }

    @Test
    void testValidDeposit() throws Exception {
        BankTransactionSystemGUI.main(null);
        invokeHandleTransaction("50", true);
        Field accountField = BankTransactionSystemGUI.class.getDeclaredField("account");
        accountField.setAccessible(true);
        BankAccount account = (BankAccount) accountField.get(null);
        assertEquals(150.0, account.getBalance(), 0.001);
    }
}
