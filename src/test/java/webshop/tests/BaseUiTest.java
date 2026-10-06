package webshop.tests;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import webshop.config.DriverSettings;
import webshop.util.Attachments;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sessionId;

@Tag("UI")
public abstract class BaseUiTest {

    @BeforeAll
    static void setUpBrowser() {
        DriverSettings.configure();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void tearDown() {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }

        String sessionId = sessionId().toString();
        Attachments.screenshot();
        Attachments.browserConsoleLogs();
        closeWebDriver();

        if (DriverSettings.isRemote()) {
            Attachments.video(sessionId);
        }
    }
}
