package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public class DesktopListingPage {
    public Locator offerCards;
    public Locator gridLayoutButton;

    public DesktopListingPage(Page page) {
        this.gridLayoutButton = page.getByLabel("Grid");
        this.offerCards = page.getByTestId("property-card"); // page.getByTestId("property-card-container");
    }


}
