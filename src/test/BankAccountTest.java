import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BankAccountTest {
    private BankAccount account;

    @BeforeEach
    public void setup() {
        account = new BankAccount(100.0);
    }

    @Test
    public void testDepositValidAmount() {
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance(), 0.001);
        assertTrue(account.getLastTransaction().contains("Deposited"));
    }

    @Test
    public void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-10));
    }

    @Test
    public void testWithdrawValidAmount() {
        account.withdraw(30.0);
        assertEquals(70.0, account.getBalance(), 0.001);
        assertTrue(account.getLastTransaction().contains("Withdrawn"));
    }

    @Test
    public void testWithdrawMoreThanBalance() {
        Exception ex = assertThrows(IllegalStateException.class, () -> account.withdraw(200.0));
        assertTrue(ex.getMessage().contains("Insufficient funds"));
    }

    @Test
    public void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-10));
    }

    @Test
    public void testTransactionHistory() {
        account.deposit(20.0);
        account.withdraw(10.0);
        List<String> history = account.getTransactionHistory();
        assertTrue(history.size() >= 3);
        assertTrue(history.get(history.size() - 1).contains("Withdrawn"));
    }

    @Test
    public void testInitialBalanceNegativeShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-100.0));
    }

    @Test
    public void testGetLastTransactionWhenEmpty() {
        BankAccount emptyAccount = new BankAccount(0.0);
        assertEquals("Account created with initial balance: 0.0", emptyAccount.getLastTransaction());
    }
}

