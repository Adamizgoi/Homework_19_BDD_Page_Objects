package cucumber;

import data.DataHelper;
import errors.MoneyTransferException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.*;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static pages.DashboardPage.Dashboard.*;
import static pages.LoginPage.Login.loginSuccessful;
import static pages.VerificationPage.Verification.verifyUser;

public class CucumberRunnerTest {

    DataHelper.AuthInfo user = new DataHelper.AuthInfo();
    String cardNumber1 = "5559 0000 0000 0001"; // номер первой тестовой карты
    String cardNumber2 = "5559 0000 0000 0002"; // номер второй тестовой карты

    @Before(order = 1)
    public void userOpenSite() {
        open("http://localhost:9999/");
    }

    @After(order = 1)
    public void returnBalanceToOriginState() {
        returnBalancesToOriginState();
    }

    @After(order = 2)
    public void teardown() {
        closeWebDriver();
    }

    @И("пользователь успешно залогинился")
    public void userLogInSuccessful() {
        loginSuccessful(user);
    }

    @И("пользователь ввел верный код на странице верификации")
    public void userVerifySuccessful() {
        verifyUser();
    }

    @Когда("пользователь переводит {string} рублей со своей карты {string} на свою карту {string} с главной страницы")
    public void transferMoney(String sum, String cardFrom, String cardTo) {
        if (cardTo.equals(cardNumber1) && cardFrom.equals(cardNumber2)) {
            transferMoneyToFirstCard(sum);
        }
        if (cardTo.equals(cardNumber2) && cardFrom.equals(cardNumber1)) {
            transferMoneyToSecondCard(sum);
        }
    }

    @Тогда("баланс его карты {string} из списка на главной странице должен стать {string} рублей")
    public void checkFinalBalance(String card, String balance) {
        if (card.equals(cardNumber1)) {
            if (!getBalanceOfFirstCard().equals(balance)) {
                throw new MoneyTransferException(
                        "Баланс карты " + cardNumber1 + " после перевода на нее денег не равен " + balance + "."
                );
            }
        }
        if (card.equals(cardNumber2)) {
            if (!getBalanceOfSecondCard().equals(balance)) {
                throw new MoneyTransferException(
                        "Баланс карты " + cardNumber2 + " после перевода на нее денег не равен " + balance + "."
                );
            }
        }
    }
}