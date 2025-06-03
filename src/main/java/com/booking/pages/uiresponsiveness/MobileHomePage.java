package com.booking.pages.uiresponsiveness;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MobileHomePage {
    Page page;
    public Locator hamburgerMenu;
    public Locator searchInput;

    public MobileHomePage(Page page) {
        this.page = page;
        this.hamburgerMenu = page.getByTestId("header-mobile-menu-button");
        this.searchInput = page.getByPlaceholder("Where are you going?");
    }
}
