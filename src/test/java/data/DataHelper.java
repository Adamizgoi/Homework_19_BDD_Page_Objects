package data;

import com.github.javafaker.Faker;
import lombok.*;

public class DataHelper {

    /**
     * Класс-генератор данных для страницы ввода логина и пароля
     */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthInfo {
        Faker faker = new Faker();

        private String login;
        private String password;

        // генерация валидного логина
        public String getLogin() {
            generateLogin();
            return login;
        }

        private void generateLogin() {
            login = "vasya";
        }

        // генерация валидного пароля
        public String getPassword() {
            generatePassword();
            return password;
        }

        private void generatePassword() {
            password = "qwerty123";
        }

        // генерация невалидного логина
        public String getErrorLogin() {
            generateErrorLogin();
            return login;
        }

        private void generateErrorLogin() {
            login = faker.name().username();
        }

        // генерация невалидного пароля
        public String getErrorPassword() {
            generateErrorLogin();
            return password;
        }

        private void generateErrorPassword() {
            password = faker.internet().password();
        }
    }

    /**
     * Класс-генератор смс-кода / push для страницы верификации (VerificationPage)
     */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationInfo {
        private String code;

        // генерирует валидный код
        public String getCode() {
            generateCode();
            return code;
        }

        private void generateCode() {
            code = "12345";
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardInfo {
        private String cardNumber1;
        private String cardNumber2;
        private String originBalance;

        public String getFirstCardNumber() {
            generateFirstCardNumber();
            return cardNumber1;
        }

        private void generateFirstCardNumber() {
            cardNumber1 = "5559 0000 0000 0001";
        }

        public String getSecondCardNumber() {
            generateSecondCardNumber();
            return cardNumber2;
        }

        private void generateSecondCardNumber() {
            cardNumber2 = "5559 0000 0000 0002";
        }

        public String getOriginBalance() {
            setOriginBalance();
            return originBalance;
        }

        private void setOriginBalance() {
            originBalance = "10000";
        }
    }
}
