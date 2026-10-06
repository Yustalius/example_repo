package booking.api;

import booking.config.BookingConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigCache;

public final class BookingSpec {

    private static final BookingConfig CONFIG = ConfigCache.getOrCreate(BookingConfig.class);

    public static final RequestSpecification BOOKING_SPEC = new RequestSpecBuilder()
            .setBaseUri(CONFIG.bookingUrl())
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .build();

    private BookingSpec() {
    }
}
