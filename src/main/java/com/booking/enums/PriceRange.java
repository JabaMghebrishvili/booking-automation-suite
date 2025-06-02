package com.booking.enums;

public enum PriceRange {
    FIFTY("50"),
    ONE_HUNDRED("100"),
    ONE_HUNDRED_AND_FIFTY("150"),
    TWO_HUNDRED("200"),
    TWO_HUNDRED_AND_FIFTY("250"),
    THREE_HUNDRED("300"),
    THREE_HUNDRED_AND_FIFTY("350"),
    FOUR_HUNDRED("400"),
    FOUR_HUNDRED_AND_FIFTY("450"),
    FIVE_HUNDRED("500"),;

    private final String value;

    PriceRange(String value) {
        this.value = value;
    }

    public String  getValue() {
        return value;
    }
}
