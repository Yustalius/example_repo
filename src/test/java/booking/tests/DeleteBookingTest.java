package booking.tests;

import booking.extensions.BookingCleaner;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static booking.steps.BookingSteps.createBooking;
import static booking.testdata.BookingTestData.validBooking;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Удаление бронирования")
class DeleteBookingTest extends BaseApiTest {

    @Test
    @Tag("positive")
    @DisplayName("Удалённое бронирование больше не находится")
    void deleteBookingTest(BookingCleaner cleaner) {
        int bookingId = createBooking(validBooking());
        cleaner.deleteAfterTest(bookingId);

        assertThat(bookingApi.deleteBooking(bookingId).statusCode()).isEqualTo(201);
        assertThat(bookingApi.getBooking(bookingId).statusCode()).isEqualTo(404);
    }

    @Test
    @Tag("negative")
    @DisplayName("Без токена бронирование не удаляется")
    void deleteBookingWithoutTokenTest(BookingCleaner cleaner) {
        int bookingId = createBooking(validBooking());
        cleaner.deleteAfterTest(bookingId);

        assertThat(bookingApi.deleteBookingWithoutToken(bookingId).statusCode()).isEqualTo(403);
        assertThat(bookingApi.getBooking(bookingId).statusCode()).isEqualTo(200);
    }
}
