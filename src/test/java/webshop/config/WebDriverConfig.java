package webshop.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${run}.properties",
        "classpath:config/local.properties"
})
public interface WebDriverConfig extends Config {

    @DefaultValue("https://demowebshop.tricentis.com")
    String baseUrl();

    @DefaultValue("chrome")
    String browser();

    String browserVersion();

    @DefaultValue("1920x1080")
    String browserSize();

    String remoteUrl();

    String remoteLogin();

    String remotePassword();

    String videoStorageUrl();
}
