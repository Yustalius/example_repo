package webshop.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationResultPage extends BasePage {

    private final SelenideElement result = $(".page-body .result");

    @Step("Проверить сообщение об успешной регистрации")
    public RegistrationResultPage checkRegistrationCompleted() {
        result.shouldHave(exactText("Your registration completed"));

        return this;
    }
}
