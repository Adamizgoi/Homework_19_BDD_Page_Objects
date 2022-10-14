package pages;

import data.DataHelper;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Класс для заполнения страницы с логином и паролем. Может заполнять валидными или не валидными значениями.
 * В классе также хранятся тексты возможных ошибок.
 */

@UtilityClass
public class LoginPage {

    @UtilityClass
    public class Login {
        private final String name = "[data-test-id = 'login'] input";
        private final String password = "[data-test-id = 'password'] input";
        private final String button = "[data-test-id = 'action-login']";
        private final String errorNotification = "[data-test-id = 'error-notification'] .notification__content";
        private final String errorEmptyLogin = "[data-test-id = 'login'] .input__sub";
        private final String errorEmptyPassword = "[data-test-id = 'password'] .input__sub";

        private final String textErrorEmptyField = "Поле обязательно для заполнения";
        private final String textErrorFieldValue = "Неверно указан логин или пароль";
        private final String textError = "Ошибка";

        public static VerificationPage loginSuccessful(DataHelper.AuthInfo userInfo) {
            $(By.cssSelector(name)).setValue(userInfo.getLogin());
            $(By.cssSelector(password)).setValue(userInfo.getPassword());
            $(By.cssSelector(button)).click();
            return new VerificationPage();
        }
    }
}
