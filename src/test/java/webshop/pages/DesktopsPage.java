package webshop.pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class DesktopsPage extends BasePage {

    private final ElementsCollection productLinks = $$(".product-grid h2.product-title a");

    @Step("Открыть товар «{name}»")
    public ProductPage openProduct(String name) {
        productLinks.findBy(exactText(name)).click();

        return new ProductPage();
    }
}
