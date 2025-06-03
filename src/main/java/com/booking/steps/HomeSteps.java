package com.booking.steps;

import com.booking.pages.HomePage;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomeSteps {
    Page page;
    HomePage homePage;

    public HomeSteps(Page page) {
        this.page = page;
        homePage = new HomePage(page);
    }
    private static final Logger logger = LoggerFactory.getLogger(HomeSteps.class);

    @Step("Fill destination input with '{destination}'")
    public HomeSteps fillDestinationInput(String destination) {
        homePage.destinationSearchInput.fill(destination);
        return this;
    }

    @Step("Initiate search")
    public HomeSteps initiateSearch() {
        homePage.searchButton.click();
        return this;
    }

    @Step("Wait and validate first option contains '{destination}'")
    public HomeSteps waitAndValidateFirstOption(String destination) {
        assertThat(homePage.firstOption).containsText(destination);
        return this;
    }

    @Step("Click dates search box")
    public HomeSteps clickDatesSearchBox() {
        homePage.datesSearchBox.click();
        return this;
    }

    @Step("Select check-in date: {date}")
    public HomeSteps selectCheckInDate(String date) {
        homePage.checkInDatePicker(date).click();
        return this;
    }

    @Step("Select check-out date: {date}")
    public HomeSteps selectCheckOutDate(String date) {
        homePage.checkOutDatePicker(date).click();
        return this;
    }

    @Step("Clear destination input if needed")
    public HomeSteps clearInputIfNeeded(){
        homePage.destinationSearchInput.click();
        String currentValue = homePage.destinationSearchInput.inputValue();

        if (currentValue != null && !currentValue.isEmpty()) {
            homePage.destinationSearchInput.press("Control+A");
            homePage.destinationSearchInput.press("Delete");
        }
        return this;
    }

    @Step("Expand guests section")
    public HomeSteps expandGuestsSection() {
        homePage.guestsCountButton.click();

        return this;
    }

    @Step("Set number of guests to {desiredGuests}")
    public HomeSteps setGuestsNumber(int desiredGuests) {
        assertThat(homePage.occupancyPopup).isVisible();

        int currentGuests = Integer.parseInt(homePage.adultsGroupInput.getAttribute("value"));
        int diff = desiredGuests - currentGuests;

        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                homePage.plusButton.click();
            }
        } else if (diff < 0) {
            for (int i = 0; i < Math.abs(diff); i++) {
                homePage.minusButton.click();
            }
        }

        assertThat(homePage.adultsGroupInput).hasValue(String.valueOf(desiredGuests));
        logger.info("Guests number for default: {}, Guests number after modified: {}", currentGuests, homePage.adultsGroupInput.getAttribute("value"));

        return this;
    }

    @Step("Validate that Sign Up button is visible")
    public HomeSteps validateSignUpButtonIsVisible() {
        assertThat(homePage.headerSignUpButton).isVisible();
        return this;
    }

    @Step("Validate that Sign In button is visible")
    public HomeSteps validateSignInButtonIsVisible() {
        assertThat(homePage.headerSignInButton).isVisible();
        return this;
    }

    @Step("Validate that hamburger menu is NOT visible")
    public HomeSteps validateHamburgerMenuIsNotVisible() {
        assertThat(homePage.hamburgerMenu).not().isVisible();
        return this;
    }

    @Step("Validate that all navigation menu links are fully visible")
    public HomeSteps validateNavigationMenuIsFullyVisible() {
        for(int i=0; i <homePage.navigationLinks.count(); i++){
            assertThat(homePage.navigationLinks.nth(i)).isVisible();
        }
        return this;
    }
}
