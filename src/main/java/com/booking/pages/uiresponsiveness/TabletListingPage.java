package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TabletListingPage {
    public Locator offerCards;

    public TabletListingPage(Page page) {
        this.offerCards = page.getByTestId("property-card");
    }
}
