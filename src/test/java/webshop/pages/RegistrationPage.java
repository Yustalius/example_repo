package webshop.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import webshop.data.User;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BasePage {

    private final SelenideElement maleGenderRadio = $("#gender-male"),
            firstNameInput = $("#FirstName"),
            lastNameInput = $("#LastName"),
            emailInput = $("#Email"),
            passwordInput = $("#Password"),
            confirmPasswordInput = $("#ConfirmPassword"),
            registerButton = $("#register-button"),
            emailError = $("span[data-valmsg-for='Email']"),
            validationSummary = $(".validation-summary-errors");

    public RegistrationResultPage register(User user) {
        return fillForm(user).submit();
    }

    @Step("Заполнить форму регистрации для {user.email}")
    public RegistrationPage fillForm(User user) {
        return selectMaleGender()
                .setFirstName(user.firstName())
                .setLastName(user.lastName())
                .setEmail(user.email())
                .setPassword(user.password())
                .setConfirmPassword(user.password());
    }

    @Step("Выбрать пол Male")
    public RegistrationPage selectMaleGender() {
        maleGenderRadio.click();

        return this;
    }

    @Step("Ввести имя {firstName}")
    public RegistrationPage setFirstName(String firstName) {
        firstNameInput.setValue(firstName);

        return this;
    }

    @Step("Ввести фамилию {lastName}")
    public RegistrationPage setLastName(String lastName) {
        lastNameInput.setValue(lastName);

        return this;
    }

    @Step("Ввести email {email}")
    public RegistrationPage setEmail(String email) {
        emailInput.setValue(email);

        return this;
    }

    @Step("Ввести пароль")
    public RegistrationPage setPassword(String password) {
        passwordInput.setValue(password);

        return this;
    }

    @Step("Повторить пароль")
    public RegistrationPage setConfirmPassword(String password) {
        confirmPasswordInput.setValue(password);

        return this;
    }

    @Step("Отправить форму регистрации")
    public RegistrationResultPage submit() {
        registerButton.click();

        return new RegistrationResultPage();
    }

    @Step("Отправить форму с невалидными данными")
    public RegistrationPage submitWithInvalidData() {
        registerButton.click();

        return this;
    }

    @Step("Проверить ошибку поля Email: {message}")
    public RegistrationPage checkEmailError(String message) {
        emailError.shouldHave(exactText(message));

        return this;
    }

    @Step("Проверить ошибку формы: {message}")
    public RegistrationPage checkFormError(String message) {
        validationSummary.shouldHave(text(message));

        return this;
    }
}
