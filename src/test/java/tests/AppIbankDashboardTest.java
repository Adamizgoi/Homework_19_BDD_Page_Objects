package tests;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pages.DashboardPage.Dashboard.*;
import static pages.LoginPage.Login.loginSuccessful;
import static pages.VerificationPage.Verification.verifyUser;

public class AppIbankDashboardTest {

    DataHelper.AuthInfo user = new DataHelper.AuthInfo();
    DataHelper.DashboardInfo dashboardInfo = new DataHelper.DashboardInfo();
    int originBalance = parseInt(dashboardInfo.getOriginBalance());

    @BeforeEach
    void setUp() {
        //Configuration.headless=true;
        open("http://localhost:9999/");
    }

    @AfterEach
    void teardown() {
        returnBalancesToOriginState();
        closeWebDriver();
    }

    @Test
    void shouldTransferMoneyToFirstCard() {
        loginSuccessful(user);
        verifyUser();
        transferMoneyToFirstCard(valueOf(originBalance - 100));
        String[] expected = {valueOf(originBalance + (originBalance - 100)), valueOf(originBalance - (originBalance - 100))};
        String[] actual = {getBalanceOfFirstCard(), getBalanceOfSecondCard()};
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldTransferMoneyToSecondCard() {
        loginSuccessful(user);
        verifyUser();
        transferMoneyToSecondCard(valueOf(originBalance - 100));
        String[] expected = {valueOf(originBalance - (originBalance - 100)), valueOf(originBalance + (originBalance - 100))};
        String[] actual = {getBalanceOfFirstCard(), getBalanceOfSecondCard()};
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldTransferFullMoneyToFirstCard() {
        loginSuccessful(user);
        verifyUser();
        transferMoneyToFirstCard(valueOf(originBalance));
        String[] expected = {valueOf(originBalance + originBalance), valueOf(0)};
        String[] actual = {getBalanceOfFirstCard(), getBalanceOfSecondCard()};
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldTransferFullMoneyToSecondCard() {
        loginSuccessful(user);
        verifyUser();
        transferMoneyToSecondCard(valueOf(originBalance));
        String[] expected = {valueOf(0), valueOf(originBalance + originBalance)};
        String[] actual = {getBalanceOfFirstCard(), getBalanceOfSecondCard()};
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldTransferOneRubleToFirstCard() {
        loginSuccessful(user);
        verifyUser();
        transferMoneyToFirstCard(valueOf(originBalance - 1));
        String[] expected = {valueOf(originBalance + (originBalance - 1)), valueOf(originBalance - (originBalance - 1))};
        String[] actual = {getBalanceOfFirstCard(), getBalanceOfSecondCard()};
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldTransferOneRubleToSecondCard() {
        loginSuccessful(user);
        verifyUser();
        transferMoneyToSecondCard(valueOf(originBalance - 1));
        String[] expected = {valueOf(originBalance - (originBalance - 1)), valueOf(originBalance + (originBalance - 1))};
        String[] actual = {getBalanceOfFirstCard(), getBalanceOfSecondCard()};
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldNotTransferMoreThanBalanceMoneyToFirstCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferMoneyToFirstCard(valueOf(originBalance + 1));
        });
    }

    @Test
    void shouldNotTransferMoreThanBalanceMoneyToSecondCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferMoneyToSecondCard(valueOf(originBalance + 1));
        });
    }

    @Test
    void shouldNotTransferWithEmptySumFieldToFirstCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {transferErrorEmptySumToFirstCard();
        });
    }

    @Test
    void shouldNotTransferWithEmptySumFieldToSecondCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferErrorEmptySumToSecondCard();
        });
    }

    @Test
    void shouldNotTransferWithEmptyCardFieldToFirstCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferErrorEmptyCardFieldToFirstCard(valueOf(originBalance - 100));
        });
    }

    @Test
    void shouldNotTransferWithEmptyCardFieldToSecondCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferErrorEmptyCardFieldToSecondCard(valueOf(originBalance - 100));
        });
    }

    @Test
    void shouldNotSendZeroMoneyToFirstCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferMoneyToFirstCard(valueOf(0));
        });
    }

    @Test
    void shouldNotSendZeroMoneyToSecondCard() throws RuntimeException {
        loginSuccessful(user);
        verifyUser();
        assertThrows(RuntimeException.class, () -> {
            transferMoneyToSecondCard(valueOf(0));
        });
    }
}
