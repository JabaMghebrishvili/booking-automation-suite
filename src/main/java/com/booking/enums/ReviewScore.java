package com.booking.enums;

public enum ReviewScore {
    WONDERFUL("Wonderful: 9+"),
    VERY_GOOD("Very Good: 8+"),
    GOOD("Good: 7+"),
    PLEASANT("Pleasant: 6+"),
    ;

    private final String value;

    ReviewScore(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
