package com.booking.tests;

import com.booking.enums.*;
import com.booking.model.OfferData;
import com.booking.runners.BaseTest;
import com.booking.steps.DetailsSteps;
import com.booking.steps.HomeSteps;
import com.booking.steps.SearchSteps;
import com.booking.utils.retry.RetryAnalyzer;
import com.booking.utils.retry.RetryCount;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.booking.data.Constants.*;

@Epic("Core Functionality Tests")
//@Feature("Hotels Search")
public class CoreFunctionalityTests extends BaseTest {
    HomeSteps homeSteps;
    SearchSteps searchSteps;
    DetailsSteps detailsSteps;

    @BeforeMethod
    public void initSteps(){
        this.homeSteps = new HomeSteps(page);
        this.searchSteps = new SearchSteps(page);
        this.detailsSteps = new DetailsSteps(page);

        page.navigate(BOOKING_URL);
    }

    @Test(description = "[SQDTBC-T1446] Search hotels by destination and validate results")
    @Feature("Search")
    @Story("User searches for hotels in desired location")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that searching page shows relevant hotel offers and addresses")
    @Link(name = "Jira Ticket", url = "https://fake-jira.com/testcase/SQDTBC-T1446")
    public void searchTest()  {
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate()
                .validateSearchResultsTitle(SEARCH_BATUMI)
                .validateHotelAddresses(SEARCH_BATUMI);
    }

    @Test(description = "[SQDTBC-T1447] Select check-in and check-out dates and verify date details")
    @Feature("Date Selection")
    @Story("User selects dates and validates check-in, check-out and nights count")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the selected check-in and check-out dates are correctly displayed and the nights count is accurate")
    @Link(name = "Jira Ticket", url = "https://fake-jira.com/testcase/SQDTBC-T1447")
    public void dateSelectionTest() {
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_TBILISI)
                .waitAndValidateFirstOption(SEARCH_TBILISI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        searchSteps
                .validateStartDate(CHECK_IN_DATE)
                .validateEndDate(CHECK_OUT_DATE)
                .validateNightsCount(CHECK_IN_DATE, CHECK_OUT_DATE);
    }

    @Test(description = "[SQDTBC-T1448] Apply filters on hotel search results and validate filtered offers")
    @Feature("Filter")
    @Story("User applies price, meal, reservation, and rating filters and verifies results")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that applying filters such as price range, meal options, reservation policy, and ratings " +
            "correctly updates and filters hotel offers")
    @Link(name = "Jira Ticket", url = "https://fake-jira.com/testcase/SQDTBC-T1448")
    public void filterApplicationTest() {
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_TBILISI)
                .waitAndValidateFirstOption(SEARCH_TBILISI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate()
                .setSliderByAriaLabel(MAX_PRICE_SLIDER, Price.THREE_HUNDRED_AND_FIFTY)
                .waitForOffersUpdate()
                .setSliderByAriaLabel(MIN_PRICE_SLIDER, Price.FIFTY)
                .waitForOffersUpdate()
                .waitForPricesUpdate()
                .validateInitialPricesPerNightInRange()
                .selectMeals(Meals.BREAKFAST_INCLUDED)
                .selectReservationOption(ReservationPolicy.FREE_CANCELLATION)
                .selectRating(Rating.FIVE_STARS)
                .waitForOffersUpdate()
                .validateMealsIncluded(Meals.BREAKFAST_INCLUDED)
                .validateReservationIncluded(ReservationPolicy.FREE_CANCELLATION)
                .validateOfferRatings(Rating.FIVE_STARS);
    }

    @Test(description = "[SQDTBC-T1449] Sort hotel search results by review score and validate sorting",
            retryAnalyzer = RetryAnalyzer.class)
    @RetryCount(count = 2)
    @Feature("Sorting")
    @Story("User sorts hotels by top review scores and validates the sorting and filters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that sorting hotel offers by review score works correctly together with applied filters " +
            "like meals, reservation options, and ratings")
    @Link(name = "Jira Ticket", url = "https://fake-jira.com/testcase/SQDTBC-T1449")
    @Flaky
    public void sortByReviewScoreTest() {
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate()
                .selectReviewScoreOption(ReviewScore.WONDERFUL)
                .selectMeals(Meals.BREAKFAST_INCLUDED)
                .selectReservationOption(ReservationPolicy.NO_PREPAYMENT)
                .selectRating(Rating.FIVE_STARS)
                .expandSortersDropdown()
                .chooseTopReviewedOption()
                .waitForPricesUpdate()
                .waitForOffersUpdate()
                .validateTopRatedScores(ReviewScore.WONDERFUL)
                .validateScoresAreSortedCorrectly();
    }

    @Test(description = "[SQDTBC-T1450] Verify consistency of property details between listing and details page")
    @Feature("Details consistency")
    @Story("User verifies that hotel offer details match between the listing and the details page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that the information displayed for a hotel offer in the search results listing matches " +
            "exactly with the details shown on the offer's detail page")
    @Link(name = "Jira Ticket", url = "https://fake-jira.com/testcase/SQDTBC-T1450")
    public void propertyDetailsConsistencyTest() {
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_LONDON)
                .waitAndValidateFirstOption(SEARCH_LONDON)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        OfferData offerDataFromListing = searchSteps
                .waitForOffersUpdate()
                .getFirstOfferData();
        searchSteps.clickToFirstOffer();

        OfferData offerDataFromDetails = detailsSteps
                .waitForDetailsLoaded()
                .getOfferDataFromDetails();
        Assert.assertEquals(offerDataFromListing, offerDataFromDetails, OFFER_DATA_MISMATCH);
    }
}
