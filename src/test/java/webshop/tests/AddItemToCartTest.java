package webshop.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import webshop.data.User;
import webshop.pages.HomePage;
import webshop.pages.ProductPage;
import webshop.pages.ShoppingCartPage;
import webshop.steps.AuthStep;

import java.math.BigDecimal;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Demo Web Shop")
@Feature("Корзина")
class AddItemToCartTest extends BaseUiTest {

    private static final String PRODUCT_NAME = "Build your own cheap computer";

    private final AuthStep authStep = new AuthStep();

    @BeforeEach
    void registerUser() {
        authStep.register(User.random());
    }

    @Test
    @Tag("positive")
    @DisplayName("Товар с выбранным процессором попадает в корзину с верной ценой")
    void addItemToCartTest() {
        String processor = "Fast";
        String quantity = "3";

        ProductPage productPage = open("/", HomePage.class)
                .hoverComputersMenu()
                .openDesktops()
                .openProduct(PRODUCT_NAME);

        BigDecimal expectedUnitPrice = new BigDecimal(productPage.getPrice()).add(processorSurcharge(processor));
        BigDecimal expectedSubtotal = expectedUnitPrice.multiply(new BigDecimal(quantity));

        ShoppingCartPage cartPage = productPage
                .selectProcessor(processor)
                .setQuantity(quantity)
                .addToCart()
                .checkAddedToCartNotification()
                .checkCartQuantity(quantity)
                .openCart();

        assertAll(
                () -> assertEquals(PRODUCT_NAME, cartPage.getProductName()),
                () -> assertEquals(quantity, cartPage.getQuantity()),
                () -> assertEquals(expectedUnitPrice.toPlainString(), cartPage.getUnitPrice()),
                () -> assertEquals(expectedSubtotal.toPlainString(), cartPage.getSubtotal())
        );
    }

    private BigDecimal processorSurcharge(String processor) {
        return switch (processor) {
            case "Slow" -> new BigDecimal("0.00");
            case "Medium" -> new BigDecimal("15.00");
            case "Fast" -> new BigDecimal("100.00");
            default -> throw new IllegalArgumentException("Unknown processor: " + processor);
        };
    }
}
