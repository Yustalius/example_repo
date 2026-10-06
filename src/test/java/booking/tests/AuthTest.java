package booking.tests;

import booking.config.BookingConfig;
import booking.dto.AuthRequest;
import booking.dto.ErrorResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Авторизация")
class AuthTest extends BaseApiTest {

    private static final BookingConfig CONFIG = ConfigCache.getOrCreate(BookingConfig.class);

    @Test
    @Tag("positive")
    @DisplayName("Валидная учётка получает токен")
    void authWithValidCredentialsTest() {
        Response response = bookingApi.auth(new AuthRequest(CONFIG.bookingUsername(), CONFIG.bookingPassword()));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    static Stream<Arguments> invalidCredentials() {
        return Stream.of(
                Arguments.of("Неверный пароль", new AuthRequest(CONFIG.bookingUsername(), "wrong-password")),
                Arguments.of("Несуществующий пользователь", new AuthRequest("unknown-user", CONFIG.bookingPassword())),
                Arguments.of("Пустой пароль", new AuthRequest(CONFIG.bookingUsername(), "")),
                Arguments.of("Пустой логин", new AuthRequest("", CONFIG.bookingPassword())),
                Arguments.of("Пустое тело", new AuthRequest(null, null))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCredentials")
    @Tag("negative")
    @DisplayName("Невалидная учётка не получает токен")
    @Description("Стенд отвечает на отказ кодом 200, а не 401, поэтому отказ сверяется по тексту reason")
    void authWithInvalidCredentialsTest(String scenario, AuthRequest credentials) {
        Response response = bookingApi.auth(credentials);

        assertThat(response.statusCode()).as(scenario).isEqualTo(200);
        assertThat(response.jsonPath().getString("token")).as(scenario).isNull();
        assertThat(response.as(ErrorResponse.class).getReason()).as(scenario).isEqualTo("Bad credentials");
    }
}
