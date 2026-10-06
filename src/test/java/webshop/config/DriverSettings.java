package webshop.config;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URI;
import java.util.Map;

public final class DriverSettings {

    private static final WebDriverConfig CONFIG = ConfigCache.getOrCreate(WebDriverConfig.class);

    private DriverSettings() {
    }

    public static void configure() {
        Configuration.baseUrl = CONFIG.baseUrl();
        Configuration.browser = CONFIG.browser();
        Configuration.browserVersion = CONFIG.browserVersion();
        Configuration.browserSize = CONFIG.browserSize();
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 30_000;

        if (isRemote()) {
            Configuration.remote = withCredentials(CONFIG.remoteUrl(), CONFIG.remoteLogin(), CONFIG.remotePassword());
            Configuration.browserCapabilities = selenoidCapabilities();
        }
    }

    public static boolean isRemote() {
        return CONFIG.remoteUrl() != null;
    }

    public static String videoUrl(String sessionId) {
        return CONFIG.videoStorageUrl() + sessionId + ".mp4";
    }

    private static DesiredCapabilities selenoidCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", true
        ));

        return capabilities;
    }

    private static String withCredentials(String url, String login, String password) {
        if (login == null || password == null) {
            return url;
        }

        URI uri = URI.create(url);

        return uri.getScheme() + "://" + login + ":" + password + "@" + uri.getAuthority() + uri.getPath();
    }
}
