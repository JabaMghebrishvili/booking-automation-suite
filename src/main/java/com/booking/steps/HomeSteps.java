package com.booking.steps;

import com.booking.pages.HomePage;
import com.microsoft.playwright.Page;
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
    public HomeSteps fillDestinationInput(String destination) {
        homePage.destinationSearchInput.fill(destination);
        return this;
    }

    public HomeSteps initiateSearch() {
        homePage.searchButton.click();
        return this;
    }

    public HomeSteps waitAndValidateFirstOption(String destination) {
//        assertThat(homePage.firstOption).isVisible();
        assertThat(homePage.firstOption).containsText(destination);

        return this;
    }

    public HomeSteps clickDatesSearchBox() {
        homePage.datesSearchBox.click();
        return this;
    }

    public HomeSteps selectCheckInDate(String date) {
        homePage.checkInDatePicker(date).click();
        return this;
    }

    public HomeSteps selectCheckOutDate(String date) {
        homePage.checkOutDatePicker(date).click();
        return this;
    }

    public String getTextFromDataPicker() {

        return homePage.datesSearchBox.innerText();
    }

    public HomeSteps clearInputIfNeeded(){
        homePage.destinationSearchInput.click();
        String currentValue = homePage.destinationSearchInput.inputValue();

        if (currentValue != null && !currentValue.isEmpty()) {
            homePage.destinationSearchInput.press("Control+A");  // Windows/Linux
            homePage.destinationSearchInput.press("Delete");
        }

        return this;
    }

    public HomeSteps expandGuestsSection() {
        homePage.guestsCountButton.click();

        return this;
    }

    public HomeSteps fillGuestsInput(String guests) {
        homePage.adultsGroupInput.fill(guests);
        return this;
    }

    public HomeSteps setGuestsNumber(int desiredGuests) {
//        Locator guestsInput = page.locator("input#group_adults");
//        Locator increaseButton = page.locator("button.increase-button-selector"); // ჩასვი სწორი სელექტორი
//        Locator decreaseButton = page.locator("button.decrease-button-selector"); // ჩასვი სწორი სელექტორი
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
}
