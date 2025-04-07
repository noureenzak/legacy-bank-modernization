import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountConcurrencyTest {
    private static final int THREADS = 100;
    private final BankAccount account = new BankAccount(1000);

    @Test
    void concurrentDepositsAndWithdrawals() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < THREADS/2; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    account.deposit(10);
                } catch (InterruptedException ignored) {}
            });
            
            executor.submit(() -> {
                try {
                    latch.await();
                    account.withdraw(10);
                } catch (InterruptedException ignored) {}
            });
        }

        latch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));

        assertEquals(1000.0, account.getBalance(), 0.001,
            "Balance should remain consistent under concurrent access");
    }
}
