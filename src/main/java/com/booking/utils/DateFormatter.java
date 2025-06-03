package com.booking.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
    public static String formatDateForDisplay(String date) {
        LocalDate localDate = LocalDate.parse(date); // parse "2025-06-10"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd", Locale.US); //
        return localDate.format(formatter); //  "Tue, Jun 10"
    }
}
