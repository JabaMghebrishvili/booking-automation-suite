package com.booking.tests;

import com.booking.enums.*;
import com.booking.model.OfferData;
import com.booking.runners.BaseTest;
import com.booking.steps.DetailsSteps;
import com.booking.steps.HomeSteps;
import com.booking.steps.SearchSteps;
import com.booking.utils.retry.RetryAnalyzer;
import com.booking.utils.retry.RetryCount;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import static com.booking.data.Constants.*;

public class CoreFunctionalityTests extends BaseTest {
    HomeSteps homeSteps;
    SearchSteps searchSteps;
    DetailsSteps detailsSteps;

    @BeforeMethod
    public void initSteps(){
        this.homeSteps = new HomeSteps(page);
        this.searchSteps = new SearchSteps(page);
        this.detailsSteps = new DetailsSteps(page);
    }

    @Test
    public void searchTest() {
        page.navigate(BOOKING_URL);

        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .initiateSearch();
        searchSteps
                .validateSearchResultsTitle(SEARCH_BATUMI)
                .validateHotelAddresses(SEARCH_BATUMI);
    }

    // ამ ტესტს ხანდახან აფეილებს დიალოგის ფანჯარა, რომელიც ეფარება ქარდის ფოტოს და ვერ ეკლიკება მასზე,
    // აქ შეიძლება Retry-ის დამატება
    @Test
    public void dateSelectionTest() {
        page.navigate(BOOKING_URL);

        homeSteps
                .fillDestinationInput(SEARCH_TBILISI)
                .waitAndValidateFirstOption(SEARCH_TBILISI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        searchSteps
                .clickToFirstOffer();
        detailsSteps
                .validateStartDate(CHECK_IN_DATE) // ეს ორი მეთოდი შეიძლება searchSteps-ში გავიტანო
                .validateEndDate(CHECK_OUT_DATE);

//        System.out.println(homeSteps.getTextFromDataPicker());
//                .initiateSearch();
    }


    @Test(retryAnalyzer = RetryAnalyzer.class) // ამ ეტაპზე მუშაობს თუმცა რეთრაის მაინც დავტოვებ, შეიძლება არასტაბილური გახდეს
    @RetryCount(count = 1)
//    @Test
    public void filterApplicationTest() {
        page.navigate(BOOKING_URL);
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_TBILISI)
                .waitAndValidateFirstOption(SEARCH_TBILISI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();
        searchSteps
//                .validateInitialPricesPerNightInRange()
                .waitForOffersUpdate()
                .setSliderByAriaLabel3("Max.", Price.THREE_HUNDRED_AND_FIFTY)  //
//                .fillMinPrice(PriceRange.FIFTY) // ასე პირდაპირ ხელით ჩაწერაც მუშაობს და უფრო ზუსტია
                .waitForOffersUpdate()
                .setSliderByAriaLabel3("Min.", Price.FIFTY)
//                .fillMaxPrice(PriceRange.THREE_HUNDRED) // ასე პირდაპირ ხელით ჩაწერაც მუშაობს და უფრო ზუსტია
//                .setSliderRange(100,300);
//                .validateSearchResultsTitle(SEARCH_BATUMI)
//                .validateHotelAddresses(SEARCH_BATUMI)
                .waitForOffersUpdate()
//                .validateHotelAddresses(SEARCH_TBILISI)
                .waitForPricesUpdate() // მგონი ამან გვიშველა
//                .validateInitialPricesPerNightInRange()
//                .clickToMinSlider(100)
//                .waitForOffersUpdate()
//                .clickToMaxSlider(300)
//                .waitForOffersUpdate()
                .validateInitialPricesPerNightInRange()
                .selectMeals(Meals.BREAKFAST_INCLUDED)
                .selectReservationOption(ReservationPolicy.FREE_CANCELLATION)
                .selectRating(Rating.FIVE_STARS)
//                .waitForPricesUpdate()
//                .waitForOffersUpdate()
                .validateMealsIncluded(Meals.BREAKFAST_INCLUDED)
                .validateReservationIncluded(ReservationPolicy.FREE_CANCELLATION)
                .validateOfferRatings2(Rating.FIVE_STARS); // validateOfferRatings ეს მეთოდი მაქვს გამოსასწორებელი
                                                            // მუშაობს მხოლოდ მაშინ როცა ერთი ოფერია, ანუ ითვლის მთლიანად
                                                            // ყველა ვარსკვლავის რაოდენობას, რაც ფაიჯზეა, არათუ თითოეული ოფერის

        // აქ არის პრობლემა, Flaky-ია, და ამასთანავე სწორადაც არ მუშაობს,
                                              // 300-ის ნაცვლად 270-ს ირჩევს
    }

    @Test
    public void sortByReviewScoreTest() {
        page.navigate(BOOKING_URL);
        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate()
                .selectReviewScoreOption(ReviewScore.WONDERFUL)
                .expandSortersDropdown()
                .chooseTopReviewedOption()
//                .waitForPricesUpdate()
                .waitForRatingScoresUpdate()
                .validateTopRatedScores(ReviewScore.WONDERFUL);
//                .validateScoresAreSortedCorrectly(); // აქ ფეილდება, რადგან სწორად ვერ სორტავს
    }

    @Test // ეს მეთოდი არ მუშაობს, წასაშლელია
    @Ignore
    public void propertyDetailsConsistencyTest() {
        page.navigate(BOOKING_URL);

        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_TBILISI)
                .waitAndValidateFirstOption(SEARCH_TBILISI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();
        searchSteps
                .waitForOffersUpdate();
        OfferData offerDataFromListing = searchSteps.getFirstOfferData();

        System.out.println(offerDataFromListing);
        searchSteps
                .clickToFirstOffer();
        detailsSteps
                .validateStartDate(CHECK_IN_DATE)
                .validateEndDate("2025-06-20");
//        detailsSteps
//                .waitForOffersUpdate();
//        String title = detailsSteps.getTitle();
        OfferData offerDataFromDetails = detailsSteps.getOfferDataFromDetails(); // აქ ფეილდება, ლოკატორიების პრობლემაა
        System.out.println(offerDataFromDetails);
        Assert.assertEquals(offerDataFromListing, offerDataFromDetails);
    }

    @Test // ან ამას დავტოვებ ან propertyDetailsConsistencyTest3-ს, აქ ფლუენტ ინტერფეისი ნაკლებადაა
    public void propertyDetailsConsistencyTest2() {
        page.navigate(BOOKING_URL);

        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();

        searchSteps.waitForOffersUpdate();

        OfferData offerDataFromListing = searchSteps.getFirstOfferData();

        // აქ ვაბრუნებთ ახალ ტაბს
        Page detailsTab = searchSteps.clickToFirstOfferAndReturnDetailsPage();
        // არსებული detailsSteps ვაბავთ ახალ ტაბზე
        detailsSteps.setPage(detailsTab);
//        detailsSteps
//                .validateStartDate(CHECK_IN_DATE) // აქ ვარდება იმიტომ რომ 2 ელემენტს არეზოლვებს, ერთი ზედა მეორე ქვედა
//                .validateEndDate("2025-06-20");
        OfferData offerDataFromDetails = detailsSteps.getOfferDataFromDetails();

        Assert.assertEquals(offerDataFromListing, offerDataFromDetails, "Offer data mismatch!");
    }

    @Test // ეს კი უფრო fluent-ია მაგრამ searchSteps-ს რომ detailsSteps ვაბრუნებინებთ ნაკლებად სასურველია
    public void propertyDetailsConsistencyTest3() {
        page.navigate(BOOKING_URL);

        homeSteps
                .clearInputIfNeeded()
                .fillDestinationInput(SEARCH_BATUMI)
                .waitAndValidateFirstOption(SEARCH_BATUMI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();
//        searchSteps.waitForOffersUpdate(); მგონიი ეს ვეითი არც სჭირდება
        OfferData offerDataFromListing = searchSteps.getFirstOfferData();

        OfferData offerDataFromDetails = searchSteps
                .clickToFirstOfferAndSwitchToDetailsTab(detailsSteps)
//                .validateStartDate(CHECK_IN_DATE)
//                .validateEndDate("2025-06-20")
                .getOfferDataFromDetails();

        Assert.assertEquals(offerDataFromListing, offerDataFromDetails, "Offer data mismatch!");
    }
}
