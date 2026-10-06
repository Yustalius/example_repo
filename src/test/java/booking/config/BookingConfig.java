package booking.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/booking.properties"
})
public interface BookingConfig extends Config {

    @DefaultValue("https://restful-booker.herokuapp.com")
    String bookingUrl();

    String bookingUsername();

    String bookingPassword();
}
