import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BankAccountTest.class,
    BankAccountConcurrencyTest.class,
    GUIControllerTest.class
})
public class BankAccountTestSuite {}
