package com.booking.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MockListingPage {
    public Locator loader;
    public Locator errorMessage;
    public Locator noResultsMessage;

    public MockListingPage(Page page) {
        this.loader = page.locator(".loader-spinner");
        this.errorMessage = page.locator("p.error-message");
        this.noResultsMessage = page.locator(".no-results");
    }
}
