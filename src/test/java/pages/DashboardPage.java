package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import errors.MoneyTransferException;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.*;
import static pages.LoginPage.Login.loginSuccessful;
import static pages.VerificationPage.Verification.verifyUser;

/**
 * Класс для работы со страницей личного кабинета. В ней содержатся операции перевода с карты на карту, обновления страницы,
 * а также коды ошибок и методы для получения ошибок
 */

public class DashboardPage {

    @UtilityClass
    public static class Dashboard {
        DataHelper.DashboardInfo cardInfo = new DataHelper.DashboardInfo();

        private final ElementsCollection cards = $$(By.cssSelector("[data-test-id = 'action-deposit']"));
        private final SelenideElement card1 = cards.get(0); // выбирает кнопку "пополнить" первой карты
        private final SelenideElement card2 = cards.get(1); // выбирает кнопку "пополнить" второй карты
        private final String refresh = "[data-test-id = 'action-reload']"; // обновить страницу

        private final String amountOfMoney = "[data-test-id = 'amount'] input"; // сумма перевода
        private final String fromCard = "[data-test-id = 'from'] input"; // с какой карты
        private final String transferMoney = "[data-test-id = 'action-transfer']"; // подтвердить перевод
        private final String cancel = "[data-test-id = 'action-cancel']"; // отменить перевод

        // баланс карт
        private final String balanceCard1 = "[data-test-id = '92df3f1c-a033-48e6-8390-206f6b1f56c0']";
        private final String balanceCard2 = "[data-test-id = '0f3f5c2a-249e-4c3d-8287-09f7a039391d']";
        private String balance1; // тут в строках много текста помимо баланса, так как фронтенд не содержит нужных меток
        private String balance2; // тут в строках много текста помимо баланса, так как фронтенд не содержит нужных меток

        private String sumOfLastTransfer; // поле для хранения суммы последнего перевода

        // ошибки
        private final String errorNotification = "[data-test-id = 'error-notification'] .notification__content";
        private final String errorText = "Произошла ошибка";

        // метод для перевода денег с первой карты на вторую: в метод нужно передать сумму в формате String
        public static void transferMoneyToFirstCard(String sum) {
            card1.click();
            $(By.cssSelector(amountOfMoney)).setValue(sum);
            $(By.cssSelector(fromCard)).setValue(cardInfo.getSecondCardNumber());
            $(By.cssSelector(transferMoney)).click();
            errorTransferCheck();
            $(By.cssSelector(refresh)).click();
            sumOfLastTransfer = sum;
            saveCurrentBalance();
        }

        // попытка отправить деньги с незаполненной суммой
        public static void transferErrorEmptySumToFirstCard() {
            card1.click();
            $(By.cssSelector(fromCard)).setValue(cardInfo.getSecondCardNumber());
            $(By.cssSelector(transferMoney)).click();
            errorTransferCheck();
        }

        // попытка отправить деньги с незаполненным номером карты
        public static void transferErrorEmptyCardFieldToFirstCard(String sum) {
            card1.click();
            $(By.cssSelector(amountOfMoney)).setValue(sum);
            $(By.cssSelector(transferMoney)).click();
            errorTransferCheck();
        }

        // метод для перевода денег со второй карты на первую: в метод нужно передать сумму в формате String
        public static void transferMoneyToSecondCard(String sum) {
            card2.click();
            $(By.cssSelector(amountOfMoney)).setValue(sum);
            $(By.cssSelector(fromCard)).setValue(cardInfo.getFirstCardNumber());
            $(By.cssSelector(transferMoney)).click();
            errorTransferCheck();
            $(By.cssSelector(refresh)).click();
            sumOfLastTransfer = sum;
            saveCurrentBalance();
        }

        // попытка отправить деньги с незаполненной суммой (вторая карта)
        public static void transferErrorEmptySumToSecondCard() {
            card2.click();
            $(By.cssSelector(fromCard)).setValue(cardInfo.getFirstCardNumber());
            $(By.cssSelector(transferMoney)).click();
            errorTransferCheck();
        }

        // попытка отправить деньги с незаполненным номером карты (вторая карта)
        public static void transferErrorEmptyCardFieldToSecondCard(String sum) {
            card2.click();
            $(By.cssSelector(amountOfMoney)).setValue(sum);
            $(By.cssSelector(transferMoney)).click();
            errorTransferCheck();
        }

        // метод для перехватывания ошибок, произошедших при попытке перевести деньги
        private void errorTransferCheck() {
            if ($(By.cssSelector(errorNotification)).isDisplayed()) {
                throw new MoneyTransferException(
                        "Произошла ошибка при попытке перевести деньги"
                );
            }
        }

        // метод для возвращения баланса к исходному состоянию (рассчитан только на две карты!)
        public static void returnBalancesToOriginState() {
            DataHelper.AuthInfo user = new DataHelper.AuthInfo();
            open("http://localhost:9999/");
            loginSuccessful(user);
            verifyUser();
            saveCurrentBalance();
            int firstCard = parseInt(balance1);
            int originBalance = parseInt(cardInfo.getOriginBalance());

            if (firstCard != originBalance) {
                if (firstCard > originBalance) {
                    int sumToTransfer = firstCard - originBalance;
                    transferMoneyToSecondCard(String.valueOf(sumToTransfer));
                }
                if (firstCard < originBalance) {
                    int sumToTransfer = originBalance - firstCard;
                    transferMoneyToFirstCard(String.valueOf(sumToTransfer));
                }
            }
        }

        // метод для сохранения в массив balance текущего баланса обеих карт
        private static void saveCurrentBalance() {
            String textOfBalance1 = $(By.cssSelector(balanceCard1)).getText();
            textOfBalance1 = textOfBalance1.replace("**** **** **** 0001, баланс: ", "");
            textOfBalance1 = textOfBalance1.replace(" р.\n" + "Пополнить", "");
            balance1 = textOfBalance1;
            String textOfBalance2 = $(By.cssSelector(balanceCard2)).getText();
            textOfBalance2 = textOfBalance2.replace("**** **** **** 0002, баланс: ", "");
            textOfBalance2 = textOfBalance2.replace(" р.\n" + "Пополнить", "");
            balance2 = textOfBalance2;
        }

        public static String getBalanceOfFirstCard() {
            return balance1;
        }

        public static String getBalanceOfSecondCard() {
            return balance2;
        }
    }
}
