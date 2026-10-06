package booking.tests;

import booking.dto.BookingDto;
import booking.dto.CreateBookingResponse;
import booking.extensions.BookingCleaner;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static booking.testdata.BookingTestData.bookingWithDates;
import static booking.testdata.BookingTestData.datesOf;
import static booking.testdata.BookingTestData.validBooking;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Создание бронирования")
class CreateBookingTest extends BaseApiTest {

    @Test
    @Tag("positive")
    @DisplayName("Бронирование создаётся и читается с теми же данными")
    void createBookingTest(BookingCleaner cleaner) {
        BookingDto request = validBooking();

        Response createResponse = bookingApi.createBooking(request);
        assertThat(createResponse.statusCode()).isEqualTo(200);

        CreateBookingResponse created = createResponse.as(CreateBookingResponse.class);
        assertThat(created.getBookingId()).isNotNull().isPositive();
        cleaner.deleteAfterTest(created.getBookingId());
        assertThat(created.getBooking()).usingRecursiveComparison().isEqualTo(request);

        Response getResponse = bookingApi.getBooking(created.getBookingId());
        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.as(BookingDto.class)).usingRecursiveComparison().isEqualTo(request);
    }

    static Stream<Arguments> bookingsWithoutRequiredFields() {
        return Stream.of(
                Arguments.of("Без firstname", validBooking().toBuilder().firstname(null).build()),
                Arguments.of("Без lastname", validBooking().toBuilder().lastname(null).build()),
                Arguments.of("Пустое тело", BookingDto.builder().build())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("bookingsWithoutRequiredFields")
    @Tag("negative")
    @DisplayName("Бронирование без обязательных полей не создаётся")
    @Description("Стенд отвечает 500 с текстом вместо 400 с описанием ошибки")
    void createBookingWithoutRequiredFieldsTest(String scenario, BookingDto request) {
        Response response = bookingApi.createBooking(request);

        assertThat(response.statusCode()).as(scenario).isEqualTo(500);
        assertThat(response.asString()).as(scenario).isEqualTo("Internal Server Error");
    }

    static Stream<Arguments> bookingsWithInvalidValues() {
        LocalDate today = LocalDate.now();

        return Stream.of(
                Arguments.of("Отрицательная цена", validBooking().toBuilder().totalprice(-500).build()),
                Arguments.of("Выезд раньше заезда", validBooking().toBuilder()
                        .bookingdates(datesOf(today.plusDays(8), today.plusDays(1)))
                        .build()),
                Arguments.of("Дата не в формате yyyy-MM-dd", bookingWithDates("not-a-date", today.plusDays(8).toString()))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("bookingsWithInvalidValues")
    @Tag("negative")
    @DisplayName("Дефект стенда: бронирование с невалидными данными принимается")
    @Description("Ожидаемое поведение - 400 с описанием ошибки, фактическое - 200 и созданная бронь")
    void createBookingWithInvalidValuesIsAcceptedTest(String scenario, BookingDto request, BookingCleaner cleaner) {
        Response response = bookingApi.createBooking(request);

        assertThat(response.statusCode()).as(scenario).isEqualTo(200);

        Integer bookingId = response.as(CreateBookingResponse.class).getBookingId();
        assertThat(bookingId).as(scenario).isNotNull().isPositive();
        cleaner.deleteAfterTest(bookingId);
    }
}
