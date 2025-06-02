package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MobileHomePage {
    Page page;
    public Locator hamburgerMenu;
    public Locator searchInput;
    public Locator datesSearchBox;
    public Locator datePickerCalendar;

    public MobileHomePage(Page page) {
        this.page = page;
        this.hamburgerMenu = page.getByTestId("header-mobile-menu-button");
        this.searchInput = page.getByPlaceholder("Where are you going?");
        this.datesSearchBox = page.getByTestId("searchbox-dates-container");
        this.datePickerCalendar = page.getByTestId("searchbox-datepicker-calendar");
    }

    public Locator checkInDatePicker(String date) {
        return page.locator("[data-date='" + date + "']");
    }

    public Locator checkOutDatePicker(String date) {
        return page.locator("[data-date='" + date + "']");
    }
}
