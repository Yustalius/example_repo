package booking.steps;

import booking.api.BookingApiClient;
import booking.dto.BookingDto;
import booking.dto.CreateBookingResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public final class BookingSteps {

    private static final BookingApiClient BOOKING_API = new BookingApiClient();

    private BookingSteps() {
    }

    @Step("Предусловие: бронирование существует")
    public static int createBooking(BookingDto booking) {
        Response response = BOOKING_API.createBooking(booking);
        assertThat(response.statusCode()).as("Создание брони для предусловия").isEqualTo(200);

        return response.as(CreateBookingResponse.class).getBookingId();
    }
}
