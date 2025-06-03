package com.booking.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DetailsPage {
    public Page page;
    public Locator searchBoxLayout;
    public Locator startDate;
    public Locator endDate;
    public Locator title;
    public Locator reviewScore;
    public Locator price;
    public Locator address;

    public DetailsPage(Page page) {
        this.page = page;
        this.searchBoxLayout = page.getByTestId("searchbox-layout-wide");
        this.startDate = searchBoxLayout.getByTestId("date-display-field-start");
        this.endDate = searchBoxLayout.getByTestId("date-display-field-end");
        this.title = page.locator("#hp_hotel_name h2");
        this.reviewScore = page.getByTestId("review-score-right-component").locator("div.bc946a29db").first();
        this.price = page.locator("span.prco-valign-middle-helper").first();
        this.address = page.locator("div.cb4b7a25d9").first();
    }
}
