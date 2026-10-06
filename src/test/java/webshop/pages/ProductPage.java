package webshop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProductPage extends BasePage {

    private final SelenideElement price = $(".product-price span[itemprop='price']"),
            quantityInput = $(".add-to-cart input.qty-input"),
            addToCartButton = $(".add-to-cart input.add-to-cart-button"),
            notificationBar = $("#bar-notification");

    private final ElementsCollection attributeTitles = $$(".attributes dl dt");

    @Step("Выбрать процессор «{processor}»")
    public ProductPage selectProcessor(String processor) {
        attributeOptions("Processor")
                .findBy(text(processor))
                .$("input")
                .click();

        return this;
    }

    @Step("Указать количество: {quantity}")
    public ProductPage setQuantity(String quantity) {
        quantityInput.setValue(quantity);

        return this;
    }

    @Step("Добавить товар в корзину")
    public ProductPage addToCart() {
        addToCartButton.click();

        return this;
    }

    @Step("Проверить уведомление о добавлении в корзину")
    public ProductPage checkAddedToCartNotification() {
        notificationBar.shouldBe(visible)
                .shouldHave(cssClass("success"))
                .shouldHave(text("The product has been added to your shopping cart"));

        return this;
    }

    public String getPrice() {
        return price.getText();
    }

    private ElementsCollection attributeOptions(String groupTitle) {
        return attributeTitles.findBy(text(groupTitle))
                .sibling(0)
                .$$("li");
    }
}
