package com.booking.enums;

public enum Rating {
    TWO_STARS("2 stars"),
    THREE_STARS("3 stars"),
    FOUR_STARS("4 stars"),
    FIVE_STARS("5 stars"),;

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
