package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TabletHomePage {

    public Locator searchInput;
    public Locator searchBar;
    public Locator footerGroups;


    public TabletHomePage(Page page) {
        this.searchInput = page.getByPlaceholder("Where are you going?");
        this.searchBar = page.getByTestId("searchbox-layout-wide");
        this.footerGroups = page.locator("div[data-testid^='footer-group']");
    }

//    public Locator footerGroups() {
//        return page.locator("div[data-testid^=\"footer-group\"]");
//    }

    public Locator footerGroupLinks(int groupIndex) {
        return footerGroups.nth(groupIndex).locator("li");
    }
}
