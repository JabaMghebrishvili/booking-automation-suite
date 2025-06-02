package com.booking.tests;

import com.booking.data.DataSupplier;
import com.booking.model.BookingCase;
import com.booking.runners.BaseTest;
import com.booking.steps.HomeSteps;
import com.booking.steps.SearchSteps;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.booking.data.Constants.*;

public class DataDrivenTests extends BaseTest {
    HomeSteps homeSteps;
    SearchSteps searchSteps;

    @BeforeMethod
    public void initSteps() {
        this.homeSteps = new HomeSteps(page);
        this.searchSteps = new SearchSteps(page);
    }

    @Test
    public void searchingFormTest() {
        page.navigate(BOOKING_URL);

        homeSteps
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();
    }

    @Test(dataProvider = "bookingCasesProvider", dataProviderClass = DataSupplier.class)
    public void searchFormTestWithDatabaseData(BookingCase bookingCase) {
        page.navigate(BOOKING_URL);

        homeSteps
                .fillDestinationInput(bookingCase.getDestination())
                .waitAndValidateFirstOption(bookingCase.getDestination())
                .clickDatesSearchBox()
                .selectCheckInDate(bookingCase.getCheckInAsString())
                .selectCheckOutDate(bookingCase.getCheckOutAsString())
                .expandGuestsSection()
                .setGuestsNumber(bookingCase.getGuests())
//                .fillGuestsInput("5")
//                .fillGuests(bookingCase.getGuests()) // ეს უნდა დაამატო შენს Steps კლასში
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate()
//                .validateSearchResultsTitle(bookingCase.getDestination()) // თარიღების ვალიდაცია თუ გადმოვიტანე
                                                                            // search steps-ში იმასაც ჩავსვავ აქ
                .validateHotelAddresses(bookingCase.getDestination())
                .validateNightCount(bookingCase.getCheckInAsString(), bookingCase.getCheckOutAsString())
                .validateGuestsCount(bookingCase.getGuests());
    }
}
