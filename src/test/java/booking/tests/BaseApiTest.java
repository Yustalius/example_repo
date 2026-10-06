package booking.tests;

import booking.api.BookingApiClient;
import booking.extensions.BookingCleanupExtension;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("API")
@ExtendWith(BookingCleanupExtension.class)
public abstract class BaseApiTest {

    protected final BookingApiClient bookingApi = new BookingApiClient();

    @BeforeAll
    static void setUpRestAssured() {
        RestAssured.replaceFiltersWith(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured());
    }
}
