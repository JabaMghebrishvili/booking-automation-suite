package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.MobileHomePage;
import com.microsoft.playwright.Page;
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

    public MobileHomeSteps setViewPort(int width, int height) {
        page.setViewportSize(width, height);

        int innerWidth = (int) page.evaluate("() => window.innerWidth");
        int innerHeight = (int) page.evaluate("() => window.innerHeight");
        logger.info("Viewport size: width: {}, height: {}", innerWidth, innerHeight);

        return this;
    }

    public MobileHomeSteps validateHamburgerMenuIsVisible() {
        assertThat(mobileHomePage.hamburgerMenu).isVisible();

        return this;
    }

    public MobileHomeSteps fillSearchInput(String search) {
        mobileHomePage.searchInput.fill(search);
        return this;
    }
    public MobileHomeSteps initiateSearch() {
        mobileHomePage.searchInput.press("Enter");

        return this;
    }

    public MobileHomeSteps reloadPage(){
        page.reload();
        return this;
    }

    public MobileHomeSteps clickDatesSearchBox() {
        mobileHomePage.datesSearchBox.click();
        return this;
    }

    public MobileHomeSteps selectCheckInDate(String date) {
        mobileHomePage.checkInDatePicker(date).click();
        return this;
    }

    public MobileHomeSteps selectCheckOutDate(String date) {
        mobileHomePage.checkOutDatePicker(date).click();
        return this;
    }
}
