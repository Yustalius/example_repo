package webshop.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

    private final SelenideElement emailInput = $("#Email"),
            passwordInput = $("#Password"),
            loginButton = $("input.login-button"),
            validationSummary = $(".validation-summary-errors");

    public HomePage login(String email, String password) {
        return setEmail(email)
                .setPassword(password)
                .submit();
    }

    @Step("Ввести email {email}")
    public LoginPage setEmail(String email) {
        emailInput.setValue(email);

        return this;
    }

    @Step("Ввести пароль")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);

        return this;
    }

    @Step("Нажать Log in")
    public HomePage submit() {
        loginButton.click();

        return new HomePage();
    }

    @Step("Нажать Log in с невалидными данными")
    public LoginPage submitWithInvalidData() {
        loginButton.click();

        return this;
    }

    @Step("Проверить ошибку входа: {message}")
    public LoginPage checkLoginError(String message) {
        validationSummary.shouldHave(text(message));

        return this;
    }
}
