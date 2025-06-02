package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.DesktopHomePage;
import com.booking.steps.HomeSteps;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DesktopHomeSteps {
    Page page;
    DesktopHomePage desktopHomePage;
    private static final Logger logger = LoggerFactory.getLogger(DesktopHomeSteps.class);

    public DesktopHomeSteps(Page Page){
        this.page = Page;
        this.desktopHomePage = new DesktopHomePage(page);
    }

    public DesktopHomeSteps setViewPort(int width, int height) {
        page.setViewportSize(width, height);

        int innerWidth = (int) page.evaluate("() => window.innerWidth");
        int innerHeight = (int) page.evaluate("() => window.innerHeight");
        logger.info("Viewport size: width: {}, height: {}", innerWidth, innerHeight);

        return this;
    }

    public DesktopHomeSteps validateSignUpButtonIsVisible() {
        assertThat(desktopHomePage.headerSignUpButton).isVisible();
        return this;
    }

    public DesktopHomeSteps validateSignInButtonIsVisible() {
        assertThat(desktopHomePage.headerSignInButton).isVisible();
        return this;
    }

    public DesktopHomeSteps validateHamburgerMenuIsNotVisible() {
        assertThat(desktopHomePage.hamburgerMenu).not().isVisible();

        return this;
    }

    public DesktopHomeSteps validateNavigationMenuIsFullyVisible() {
        for(int i=0; i <desktopHomePage.navigationLinks.count(); i++){
            assertThat(desktopHomePage.navigationLinks.nth(i)).isVisible();
        }
        return this;
    }

    public DesktopHomeSteps fillDestinationInput(String destination) {
        desktopHomePage.destinationSearchInput.fill(destination);
        return this;
    }

    public DesktopHomeSteps waitAndValidateFirstOption(String destination) {
//        assertThat(homePage.firstOption).isVisible();
        assertThat(desktopHomePage.firstOption).containsText(destination);

        return this;
    }

    public DesktopHomeSteps initiateSearch() {
        desktopHomePage.searchButton.click();
        return this;
    }

    public DesktopHomeSteps clickDatesSearchBox() {
        desktopHomePage.datesSearchBox.click();
        return this;
    }

    public DesktopHomeSteps selectCheckInDate(String date) {
        desktopHomePage.checkInDatePicker(date).click();
        return this;
    }

    public DesktopHomeSteps selectCheckOutDate(String date) {
        desktopHomePage.checkOutDatePicker(date).click();
        return this;
    }
}
