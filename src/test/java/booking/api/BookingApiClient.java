package booking.api;

import booking.config.BookingConfig;
import booking.dto.AuthRequest;
import booking.dto.AuthResponse;
import booking.dto.BookingDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigCache;

import static booking.api.BookingSpec.BOOKING_SPEC;
import static io.restassured.RestAssured.given;

public class BookingApiClient {

    private static final BookingConfig CONFIG = ConfigCache.getOrCreate(BookingConfig.class);

    private String token;

    @Step("Авторизоваться под пользователем {body.username}")
    public Response auth(AuthRequest body) {
        return given(BOOKING_SPEC)
                .body(body)
                .post("/auth");
    }

    @Step("Создать бронирование")
    public Response createBooking(BookingDto booking) {
        return given(BOOKING_SPEC)
                .body(booking)
                .post("/booking");
    }

    @Step("Получить бронирование id={id}")
    public Response getBooking(int id) {
        return given(BOOKING_SPEC)
                .pathParam("id", id)
                .get("/booking/{id}");
    }

    @Step("Полностью обновить бронирование id={id}")
    public Response updateBooking(int id, BookingDto booking) {
        return given(BOOKING_SPEC)
                .cookie("token", token())
                .pathParam("id", id)
                .body(booking)
                .put("/booking/{id}");
    }

    @Step("Частично обновить бронирование id={id}")
    public Response partialUpdateBooking(int id, BookingDto booking) {
        return given(BOOKING_SPEC)
                .cookie("token", token())
                .pathParam("id", id)
                .body(booking)
                .patch("/booking/{id}");
    }

    @Step("Удалить бронирование id={id}")
    public Response deleteBooking(int id) {
        return given(BOOKING_SPEC)
                .cookie("token", token())
                .pathParam("id", id)
                .delete("/booking/{id}");
    }

    @Step("Удалить бронирование id={id} без токена")
    public Response deleteBookingWithoutToken(int id) {
        return given(BOOKING_SPEC)
                .pathParam("id", id)
                .delete("/booking/{id}");
    }

    private String token() {
        if (token == null) {
            AuthRequest credentials = new AuthRequest(CONFIG.bookingUsername(), CONFIG.bookingPassword());
            token = auth(credentials).as(AuthResponse.class).getToken();
        }

        return token;
    }
}
