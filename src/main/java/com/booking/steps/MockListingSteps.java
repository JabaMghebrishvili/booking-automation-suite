package com.booking.steps;

import com.booking.pages.MockListingPage;
import com.booking.steps.uiresponsiveness.MobileListingSteps;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockListingSteps {
    Page page;
    MockListingPage mockListingPage;
    private static final Logger logger = LoggerFactory.getLogger(MockListingSteps.class);
    public MockListingSteps(Page page) {
        this.page = page;
        this.mockListingPage = new MockListingPage(page);
    }

    @Step("Validate loader is visible")
    public MockListingSteps validateLoaderIsVisible() {
        assertThat(mockListingPage.loader).isVisible();
        logger.info("Loader is visible");
        return this;
    }

    @Step("Validate error message is visible")
    public MockListingSteps validateErrorMessageIsVisible() {
        assertThat(mockListingPage.errorMessage).isVisible();
        logger.info("Error message is visible");
        return this;
    }

    @Step("Validate error message text is: {text}")
    public MockListingSteps validateErrorMessageText(String text) {
        assertThat(mockListingPage.errorMessage).hasText(text);
        return this;
    }

    @Step("Validate 'no results' message text is: {text}")
    public MockListingSteps validateNoResultsMessage(String text) {
        assertThat(mockListingPage.noResultsMessage).hasText(text);
        logger.info("No results found");
        return this;
    }
}
