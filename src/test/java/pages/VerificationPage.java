package pages;

import data.DataHelper;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Класс для заполнения страницы верификации юзера. Может заполнять валидным или невалидным кодом.
 * В классе также хранятся тексты возможных ошибок.
 */

public class VerificationPage {

    @UtilityClass
    public static class Verification {
        DataHelper.VerificationInfo verifier = new DataHelper.VerificationInfo();

        private final String code = "[data-test-id = 'code'] input";
        private final String button = "[data-test-id = 'action-verify']";

        public static DashboardPage verifyUser() {
            $(By.cssSelector(code)).setValue(verifier.getCode());
            $(By.cssSelector(button)).click();
            return new DashboardPage();
        }
    }
}
