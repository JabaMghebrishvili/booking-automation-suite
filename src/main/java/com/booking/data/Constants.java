package com.booking.data;

import com.github.javafaker.Faker;

public class Constants {
    private static final Faker faker = new Faker();

    public static final String BOOKING_URL = "https://booking.com/",
    SEARCH_RANDOM_CITY = faker.address().city(),
    SEARCH_BATUMI = "Batumi",
    SEARCH_TBILISI = "Tbilisi",
    CHECK_IN_DATE = "2025-06-10",
    CHECK_OUT_DATE = "2025-06-17";
}
