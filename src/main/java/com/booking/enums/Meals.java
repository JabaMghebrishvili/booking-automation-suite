package com.booking.enums;

public enum Meals {
    BREAKFAST_INCLUDED("Breakfast included"),
    KITCHEN_FACILITIES("Kitchen facilities"),
    ;

    private final String value;

    Meals(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
