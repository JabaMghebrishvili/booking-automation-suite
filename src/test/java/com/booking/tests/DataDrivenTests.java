package com.booking.tests;

import com.booking.data.DataSupplier;
import com.booking.model.BookingCase;
import com.booking.runners.BaseTest;
import com.booking.steps.HomeSteps;
import com.booking.steps.SearchSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.booking.data.Constants.*;

@Epic("Data Driven Tests")
@Feature("Search Functionality with Data-Driven Testing")
public class DataDrivenTests extends BaseTest {
    HomeSteps homeSteps;
    SearchSteps searchSteps;

    @BeforeMethod
    public void initSteps() {
        this.homeSteps = new HomeSteps(page);
        this.searchSteps = new SearchSteps(page);

        page.navigate(BOOKING_URL);
    }

    @Test(description = "[SQDTBC-T1460] Data-driven test: verifies search functionality using multiple booking cases from the database",
            dataProvider = "bookingCasesDataProvider", dataProviderClass = DataSupplier.class)
    @Story("User searches with various data sets and validates search results on listing page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Performs searches with data from a database and verifies that the search results listing matches the " +
            "search criteria (destination, dates, guests)")
    @Link(name = "Test Case Management", url = "https://fake-jira.com/testcase/SQDTBC-T1460")
    public void searchFormTestWithDatabaseData(BookingCase bookingCase) {
        homeSteps
                .fillDestinationInput(bookingCase.getDestination())
                .waitAndValidateFirstOption(bookingCase.getDestination())
                .clickDatesSearchBox()
                .selectCheckInDate(bookingCase.getCheckInAsString())
                .selectCheckOutDate(bookingCase.getCheckOutAsString())
                .expandGuestsSection()
                .setGuestsNumber(bookingCase.getGuests())
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate()
                .validateHotelAddresses(bookingCase.getDestination())
                .validateNightsCount(bookingCase.getCheckInAsString(), bookingCase.getCheckOutAsString())
                .validateGuestsCount(bookingCase.getGuests());
    }
}
