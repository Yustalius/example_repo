package booking.tests;

import booking.dto.BookingDto;
import booking.extensions.BookingCleaner;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static booking.steps.BookingSteps.createBooking;
import static booking.testdata.BookingTestData.randomBooking;
import static booking.testdata.BookingTestData.validBooking;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Обновление бронирования")
class UpdateBookingTest extends BaseApiTest {

    @Test
    @Tag("positive")
    @DisplayName("PUT заменяет бронирование целиком")
    void updateBookingTest(BookingCleaner cleaner) {
        int bookingId = createBooking(validBooking());
        cleaner.deleteAfterTest(bookingId);

        BookingDto update = randomBooking();

        Response response = bookingApi.updateBooking(bookingId, update);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.as(BookingDto.class)).usingRecursiveComparison().isEqualTo(update);
        assertThat(bookingApi.getBooking(bookingId).as(BookingDto.class))
                .usingRecursiveComparison().isEqualTo(update);
    }

    @Test
    @Tag("positive")
    @DisplayName("PATCH меняет только переданные поля")
    void partialUpdateBookingTest(BookingCleaner cleaner) {
        BookingDto original = validBooking();
        int bookingId = createBooking(original);
        cleaner.deleteAfterTest(bookingId);

        BookingDto patch = BookingDto.builder()
                .firstname("Petr")
                .totalprice(3500)
                .build();
        BookingDto expected = original.toBuilder()
                .firstname(patch.getFirstname())
                .totalprice(patch.getTotalprice())
                .build();

        Response response = bookingApi.partialUpdateBooking(bookingId, patch);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.as(BookingDto.class)).usingRecursiveComparison().isEqualTo(expected);
    }
}
