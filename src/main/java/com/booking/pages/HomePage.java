package com.booking.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage {
    Page page;
    public Locator destinationSearchInput;
    public Locator searchButton;
    public Locator autocompleteResults;
    public Locator firstOption;
    public Locator datesSearchBox;
    public Locator discountOfferDialog;
    public Locator dialogCloseButton;
    public Locator datePickerCalendar;
    public Locator guestsCountButton;
    public Locator adultsGroupInput;
    public Locator occupancyPopup;
    public Locator minusButton;
    public Locator plusButton;
    public Locator headerSignUpButton;
    public Locator headerSignInButton;
    public Locator navigationLinks;
    public Locator hamburgerMenu;

    public HomePage(Page page) {
        this.page = page;
        this.destinationSearchInput = page.getByPlaceholder("Where are you going?");
        this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        this.autocompleteResults = page.locator("#autocomplete-results");
        this.firstOption = autocompleteResults.getByRole(AriaRole.OPTION).first();
        this.datesSearchBox = page.getByTestId("searchbox-dates-container");
        this.discountOfferDialog = page.getByRole(AriaRole.DIALOG);
        this.dialogCloseButton = discountOfferDialog.locator("button");
        this.datePickerCalendar = page.getByTestId("searchbox-datepicker-calendar");
        this.guestsCountButton = page.getByTestId("occupancy-config");
        this.adultsGroupInput = page.locator("input#group_adults");
        this.occupancyPopup = page.getByTestId("occupancy-popup");
        this.minusButton = page.locator("//label[text()='Adults']//ancestor::div[@class='e484bb5b7a']//button[contains(@class,'c857f39cb2')]");
        this.plusButton = page.locator("//label[text()='Adults']//ancestor::div[@class='e484bb5b7a']//button[contains(@class,'dc8366caa6')]");
        this.headerSignUpButton = page.getByTestId("header-sign-up-button");
        this.headerSignInButton = page.getByTestId("header-sign-in-button");
        this.navigationLinks = page.getByTestId("header-xpb").locator("ul a");
        this.hamburgerMenu = page.getByTestId("header-mobile-menu-button");
    }

    public Locator checkInDatePicker(String date) {
        return page.locator("[data-date='" + date + "']");
    }

    public Locator checkOutDatePicker(String date) {
        return page.locator("[data-date='" + date + "']");
    }
}
