package webshop.util;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static webshop.config.DriverSettings.videoUrl;

public final class Attachments {

    private Attachments() {
    }

    @Attachment(value = "Последний скриншот", type = "image/png")
    public static byte[] screenshot() {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Консоль браузера", type = "text/plain")
    public static String browserConsoleLogs() {
        try {
            return String.join("\n", Selenide.getWebDriverLogs(BROWSER));
        } catch (RuntimeException e) {
            return "Логи консоли недоступны: " + e.getMessage();
        }
    }

    @Attachment(value = "Видео", type = "text/html", fileExtension = ".html")
    public static String video(String sessionId) {
        return "<html><body><video width='100%' height='100%' controls autoplay>"
                + "<source src='" + videoUrl(sessionId) + "' type='video/mp4'>"
                + "</video></body></html>";
    }
}
