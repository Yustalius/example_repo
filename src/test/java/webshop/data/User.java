package webshop.data;

import net.datafaker.Faker;

import java.util.UUID;

public record User(String firstName, String lastName, String email, String password) {

    private static final Faker FAKER = new Faker();

    public static User random() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);

        return new User(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                "qa." + uniqueSuffix + "@example.com",
                FAKER.credentials().password(8, 16)
        );
    }
}
