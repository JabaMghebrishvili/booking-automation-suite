package com.booking.enums;

public enum ReservationPolicy {
    FREE_CANCELLATION("Free cancellation"),
    BOOK_WITHOUT_CREDIT_CARD("Book without credit card"),
    NO_PREPAYMENT("No prepayment"),
    ;

    private final String value;

    ReservationPolicy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
