package webshop.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import webshop.data.User;
import webshop.pages.LoginPage;
import webshop.steps.AuthStep;

import static com.codeborne.selenide.Selenide.open;

@Epic("Demo Web Shop")
@Feature("Авторизация")
class LoginTest extends BaseUiTest {

    private final AuthStep authStep = new AuthStep();
    private final User user = User.random();

    @BeforeEach
    void registerUser() {
        authStep.register(user).logout();
    }

    @Test
    @Tag("positive")
    @DisplayName("Зарегистрированный пользователь входит по email и паролю")
    void loginTest() {
        open("/login", LoginPage.class)
                .login(user.email(), user.password())
                .checkLoggedInAs(user.email());
    }

    @Test
    @Tag("negative")
    @DisplayName("Вход с неверным паролем отклоняется")
    void loginWithWrongPasswordTest() {
        open("/login", LoginPage.class)
                .setEmail(user.email())
                .setPassword(user.password() + "-wrong")
                .submitWithInvalidData()
                .checkLoginError("The credentials provided are incorrect");
    }
}
