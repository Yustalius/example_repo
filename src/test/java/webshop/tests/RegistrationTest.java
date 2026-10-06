package webshop.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import webshop.data.User;
import webshop.pages.HomePage;
import webshop.pages.RegistrationPage;
import webshop.steps.AuthStep;

import static com.codeborne.selenide.Selenide.open;

@Epic("Demo Web Shop")
@Feature("Регистрация")
class RegistrationTest extends BaseUiTest {

    private final AuthStep authStep = new AuthStep();

    @Test
    @Tag("positive")
    @DisplayName("Новый пользователь регистрируется и сразу авторизован")
    void registrationTest() {
        User user = User.random();

        open("/", HomePage.class)
                .openRegistrationPage()
                .register(user)
                .checkRegistrationCompleted()
                .checkLoggedInAs(user.email());
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/invalid-emails.csv", numLinesToSkip = 1)
    @Tag("negative")
    @DisplayName("Регистрация с невалидным email отклоняется")
    void registrationWithInvalidEmailTest(String email) {
        open("/register", RegistrationPage.class)
                .setEmail(email)
                .submitWithInvalidData()
                .checkEmailError("Wrong email");
    }

    @Test
    @Tag("negative")
    @DisplayName("Повторная регистрация на тот же email отклоняется")
    void registrationWithExistingEmailTest() {
        User user = User.random();
        authStep.register(user).logout();

        open("/register", RegistrationPage.class)
                .fillForm(user)
                .submitWithInvalidData()
                .checkFormError("The specified email already exists");
    }
}
