package com.booking.tests;

import com.booking.runners.BaseTest;
import com.booking.steps.HomeSteps;
import com.booking.steps.MockListingSteps;
import com.booking.utils.MockHtmlReader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.booking.data.Constants.*;

import java.util.regex.Pattern;

@Epic("Booking Mock Tests")
@Feature("Search Results Mock Testing")
public class BookingMockTests extends BaseTest {

    HomeSteps homeSteps;
    MockListingSteps mockListingSteps;

    @BeforeMethod
    public void initSteps() {
        this.homeSteps = new HomeSteps(page);
        this.mockListingSteps = new MockListingSteps(page);
    }

    @Test(description = "[SQDTBC-T2001] Validate loader visibility on slow search results response")
    @Story("User performs a search and loader is shown when search results respond slowly")
    @Severity(SeverityLevel.NORMAL)
    @Description("Tests that when the search results page responds slowly, the loader animation is visible to the user")
    @Link(name = "Jira Ticket", url = "https://fake-jira-link.com/SQDTBC-T2001")
    void testSlowResponse() {
        page.route(Pattern.compile(SEARCH_RESULTS_URL), route -> {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String mockHtmlResponse = MockHtmlReader.load("slow-response.html");

            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setContentType("text/html")
                    .setBody(mockHtmlResponse));
        });
        page.navigate(BOOKING_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        homeSteps
                .fillDestinationInput(SEARCH_LONDON)
                .initiateSearch();
        mockListingSteps
                .validateLoaderIsVisible();
    }

    @Test(description = "[SQDTBC-T2002] Verify no results message for empty hotel list response")
    @Story("User searches a location with no hotel results and sees appropriate no results message")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that when the search response contains an empty hotel list, the no results message is displayed correctly")
    @Link(name = "Jira Ticket", url = "https://fake-jira-link.com/SQDTBC-T2002")
    void testEmptyHotelList() {

        page.route(Pattern.compile(SEARCH_RESULTS_URL), route -> {
            String emptyHotelListHtml = MockHtmlReader.load("empty-results.html");
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setContentType("text/html")
                    .setBody(emptyHotelListHtml));
        });
        page.navigate(BOOKING_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        homeSteps
                .fillDestinationInput(SEARCH_BATUMI)
                .initiateSearch();
        mockListingSteps
                .validateNoResultsMessage(NO_RESULTS_MESSAGE);
    }

    @Test(description = "[SQDTBC-T2003] Validate error message display for server 500 error response")
    @Story("User performs a search but server returns 500 error and an error message is shown")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Tests that when the server returns a 500 Internal Server Error for search results, a proper error " +
            "message is shown to the user")
    @Link(name = "Jira Ticket", url = "https://fake-jira-link.com/SQDTBC-T2003")
    void test500Error() {
        page.route(Pattern.compile(SEARCH_RESULTS_URL), route -> {
            String mockHtmlResponse = MockHtmlReader.load("server-error.html");
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(500)
                    .setBody(mockHtmlResponse));
        });
        page.navigate(BOOKING_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        homeSteps
                .fillDestinationInput(SEARCH_TBILISI)
                .initiateSearch();
        mockListingSteps
                .validateErrorMessageIsVisible()
                .validateErrorMessageText(SERVER_ERROR_TEXT);
    }
}
