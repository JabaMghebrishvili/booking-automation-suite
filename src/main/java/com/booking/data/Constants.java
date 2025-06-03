package com.booking.data;

import com.github.javafaker.Faker;

public class Constants {
    private static final Faker faker = new Faker();

    public static final String BOOKING_URL = "https://booking.com/",
    SEARCH_RANDOM_CITY = faker.address().city(),
    SEARCH_BATUMI = "Batumi",
    SEARCH_TBILISI = "Tbilisi",
    SEARCH_LONDON = "London",
    CHECK_IN_DATE = "2025-06-18",
    CHECK_OUT_DATE = "2025-06-28",
    CURRENCY_GEL = "GEL",
    MAX_PRICE_SLIDER = "Max.",
    MIN_PRICE_SLIDER = "Min.",
    OFFER_DATA_MISMATCH = "Offer data mismatch!",
    WRONG_ADDRESS = "Address does not contain destination: ",
    DOES_NOT_INCLUDE_MEAL = "Recommended unit does not include expected meal: ",
    STARS_COUNT = "Stars count: ",
    SCORES_NOT_SORTED_CORRECTLY= "Scores not sorted correctly",
    MORE_THAN_THREE_ROW = "More than 3 offers in row at y=",
    BOUNDING_BOX_IS_NULL = "Bounding box is null for offer at index: ",
    OFFERS_ARE_NOT_STACKED = "Offers %d and %d are not stacked vertically ",
    STACK = "stack",
    NO_OFFERS_FOUND = "No offer cards found!",
    MYBATIS_INIT_ERROR = "Mybatis Initializing Error!",
    FAILED_TO_LOAD_MOCK_HTML = "Failed to load mock HTML: ",
    SEARCH_RESULTS_URL = "https://www.booking.com/searchresults.html*",
    SERVER_ERROR_TEXT = "Something went wrong on our end. Please try again later.",
    NO_RESULTS_MESSAGE = "Sorry, no results found for your search.",
    UNSUPPORTED_DEVICE = "Unsupported browser type: ",
    UNSUPPORTED_BROWSER = "Unsupported browser type: ",
    NOT_VERTICALLY_ALIGNED = "Offer is not vertically aligned.",
    CHROMIUM = "chromium",
    WEBKIT = "webkit",
    MOBILE = "mobile",
    TABLET = "tablet",
    DESKTOP = "desktop";

    public static final int DELAY = 4000,
    DESKTOP_WIDTH = 1920,
    DESKTOP_HEIGHT = 1080,
    TABLET_WIDTH = 768,
    TABLET_HEIGHT = 1024,
    MOBILE_WIDTH = 375,
    MOBILE_HEIGHT = 667;
}
