package booking.testdata;

import booking.dto.BookingDto;
import net.datafaker.Faker;

import java.time.LocalDate;

public final class BookingTestData {

    private static final Faker FAKER = new Faker();

    private BookingTestData() {
    }

    public static BookingDto validBooking() {
        return BookingDto.builder()
                .firstname("Ivan")
                .lastname("Petrov")
                .totalprice(2000)
                .depositpaid(true)
                .bookingdates(datesOf(LocalDate.now().plusDays(1), LocalDate.now().plusDays(8)))
                .additionalneeds("Breakfast")
                .build();
    }

    public static BookingDto randomBooking() {
        return validBooking().toBuilder()
                .firstname(FAKER.name().firstName())
                .lastname(FAKER.name().lastName())
                .totalprice(FAKER.number().numberBetween(100, 10_000))
                .depositpaid(FAKER.bool().bool())
                .build();
    }

    public static BookingDto bookingWithDates(String checkin, String checkout) {
        return validBooking().toBuilder()
                .bookingdates(new BookingDto.BookingDates(checkin, checkout))
                .build();
    }

    public static BookingDto.BookingDates datesOf(LocalDate checkin, LocalDate checkout) {
        return new BookingDto.BookingDates(checkin.toString(), checkout.toString());
    }
}
