package webshop.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ShoppingCartPage extends BasePage {

    private final SelenideElement productName = $("table.cart .product-name"),
            unitPrice = $("table.cart .product-unit-price"),
            quantityInput = $("table.cart input.qty-input"),
            subtotal = $("table.cart .product-subtotal");

    public String getProductName() {
        return productName.getText();
    }

    public String getUnitPrice() {
        return unitPrice.getText();
    }

    public String getQuantity() {
        return quantityInput.getValue();
    }

    public String getSubtotal() {
        return subtotal.getText();
    }
}
