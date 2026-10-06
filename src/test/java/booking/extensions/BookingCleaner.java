package booking.extensions;

import booking.api.BookingApiClient;

import java.util.ArrayList;
import java.util.List;

public class BookingCleaner {

    private final BookingApiClient bookingApi = new BookingApiClient();
    private final List<Integer> bookingIds = new ArrayList<>();

    public void deleteAfterTest(int bookingId) {
        bookingIds.add(bookingId);
    }

    void deleteAll() {
        bookingIds.forEach(bookingApi::deleteBooking);
        bookingIds.clear();
    }
}
