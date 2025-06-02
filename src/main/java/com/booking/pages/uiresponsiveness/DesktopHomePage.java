package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DesktopHomePage {
    Page page;
    public Locator headerSignUpButton;
    public Locator headerSignInButton;
    public Locator navigationLinks;
    public Locator hamburgerMenu;
    public Locator destinationSearchInput;
    public Locator searchButton;
    public Locator autocompleteResults;
    public Locator firstOption;
    public Locator datesSearchBox;
    public Locator datePickerCalendar;

    public DesktopHomePage(Page page) {
        this.page = page;
        this.headerSignUpButton = page.getByTestId("header-sign-up-button");
        this.headerSignInButton = page.getByTestId("header-sign-in-button");
        this.navigationLinks = page.getByTestId("header-xpb").locator("ul a");
        this.hamburgerMenu = page.getByTestId("header-mobile-menu-button");
        this.destinationSearchInput = page.getByPlaceholder("Where are you going?");
        this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        this.autocompleteResults = page.locator("#autocomplete-results");
        this.firstOption = autocompleteResults.getByRole(AriaRole.OPTION).first();
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
