package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.MobileHomePage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MobileHomeSteps {
    Page page;
    MobileHomePage mobileHomePage;
    private static final Logger logger = LoggerFactory.getLogger(TabletHomeSteps.class);

    public MobileHomeSteps(Page page) {
        this.page = page;
        mobileHomePage = new MobileHomePage(page);
    }

    @Step("Validate hamburger menu is visible")
    public MobileHomeSteps validateHamburgerMenuIsVisible() {
        assertThat(mobileHomePage.hamburgerMenu).isVisible();

        return this;
    }

    @Step("Fill search input with: {search}")
    public MobileHomeSteps fillSearchInput(String search) {
        mobileHomePage.searchInput.fill(search);
        return this;
    }

    @Step("Initiate search (press Enter on search input)")
    public MobileHomeSteps initiateSearch() {
        mobileHomePage.searchInput.press("Enter");
        return this;
    }

    @Step("Reload the page")
    public MobileHomeSteps reloadPage(){
        page.reload();
        return this;
    }

}
