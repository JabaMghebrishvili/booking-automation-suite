package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MobileListingPage {

    public Locator header;
    public Locator datesSearchBox;
    public Locator offers;

    public MobileListingPage(Page page) {
        this.header = page.getByTestId("sorters-filters-map-triggers");
        this.datesSearchBox = page.getByTestId("searchbox-dates-container");
        this.offers = page.getByTestId("property-card");
    }
}
