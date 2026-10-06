package webshop.steps;

import io.qameta.allure.Step;
import webshop.data.User;
import webshop.pages.BasePage;
import webshop.pages.RegistrationPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthStep {

    @Step("Зарегистрировать пользователя {user.email}")
    public BasePage register(User user) {
        return open("/register", RegistrationPage.class)
                .register(user)
                .checkRegistrationCompleted()
                .checkLoggedInAs(user.email());
    }
}
