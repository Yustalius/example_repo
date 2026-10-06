package webshop.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage {

    private final SelenideElement registerLink = $(".header-links a.ico-register"),
            loginLink = $(".header-links a.ico-login"),
            logoutLink = $(".header-links a.ico-logout"),
            accountLink = $(".header-links a.account"),
            cartLink = $("#topcartlink a.ico-cart"),
            cartQuantityBadge = $("#topcartlink span.cart-qty"),
            computersMenu = $("ul.top-menu a[href='/computers']"),
            desktopsMenuItem = $("ul.top-menu a[href='/desktops']");

    @Step("Открыть страницу регистрации")
    public RegistrationPage openRegistrationPage() {
        registerLink.click();

        return new RegistrationPage();
    }

    @Step("Открыть страницу входа")
    public LoginPage openLoginPage() {
        loginLink.click();

        return new LoginPage();
    }

    @Step("Выйти из аккаунта")
    public HomePage logout() {
        logoutLink.click();

        return new HomePage();
    }

    @Step("Навести курсор на меню Computers")
    public BasePage hoverComputersMenu() {
        computersMenu.hover();

        return this;
    }

    @Step("Открыть каталог Desktops")
    public DesktopsPage openDesktops() {
        desktopsMenuItem.click();

        return new DesktopsPage();
    }

    @Step("Открыть корзину")
    public ShoppingCartPage openCart() {
        cartLink.click();

        return new ShoppingCartPage();
    }

    @Step("Проверить, что вход выполнен под {email}")
    public BasePage checkLoggedInAs(String email) {
        accountLink.shouldHave(exactText(email));

        return this;
    }

    @Step("Проверить счётчик корзины: {quantity}")
    public BasePage checkCartQuantity(String quantity) {
        cartQuantityBadge.shouldHave(exactText("(" + quantity + ")"));

        return this;
    }
}
