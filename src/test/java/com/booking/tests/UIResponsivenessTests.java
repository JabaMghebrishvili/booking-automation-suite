package com.booking.tests;

import com.booking.runners.BaseTest;
import com.booking.steps.uiresponsiveness.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.booking.data.Constants.*;

public class UIResponsivenessTests extends BaseTest {
    DesktopHomeSteps desktopHomeSteps;
    DesktopListingSteps desktopListingSteps;
    TabletHomeSteps tabletHomeSteps;
    TabletListingSteps tabletListingSteps;
    MobileHomeSteps mobileHomeSteps;
    MobileListingSteps mobileListingSteps;

    @BeforeMethod
    public void initSteps() {
        this.desktopHomeSteps = new DesktopHomeSteps(page);
        this.desktopListingSteps = new DesktopListingSteps(page);
        this.tabletHomeSteps = new TabletHomeSteps(page);
        this.tabletListingSteps = new TabletListingSteps(page);
        this.mobileHomeSteps = new MobileHomeSteps(page);
        this.mobileListingSteps = new MobileListingSteps(page);
    }

    @Test(priority = 2)
    public void desktopViewPortTest() {
        page.navigate(BOOKING_URL);

        desktopHomeSteps
                .setViewPort(1920, 1080)
                .validateSignUpButtonIsVisible()
                .validateSignInButtonIsVisible()
                .validateHamburgerMenuIsNotVisible()
                .validateNavigationMenuIsFullyVisible()
                .fillDestinationInput(SEARCH_TBILISI)
                .waitAndValidateFirstOption(SEARCH_TBILISI)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate("2025-06-20")
                .initiateSearch();
        desktopListingSteps
                .changeLayoutToGrid()
                .validateGridLayoutHasThreeOffersPerRow();
    }

    @Test(priority = 1)
    public void tabletViewPortTest() {
        page.navigate(BOOKING_URL);

        tabletHomeSteps
                .setViewPort(768, 1024)
                .validateSearchBarIsVisible()
                .validateFooterLinksAreVerticallyStackedByX2()
                .fillSearchInput(SEARCH_BATUMI)
                .initiateSearch();
        tabletListingSteps
                .validateOffersAreVerticallyStacked();
    }

    @Test
    public void mobileViewPortTest() {
//        BrowserContext mobileContext = browser.newContext(new Browser.NewContextOptions()
//                .setViewportSize(375, 667)
//                .setDeviceScaleFactor(3)
//                .setIsMobile(true)
//                .setHasTouch(true)
//        );
//        Page mobilePage = mobileContext.newPage();
//        mobilePage.navigate(BOOKING_URL);
//
//        MobileHomeSteps mobileHomeSteps = new MobileHomeSteps(mobilePage);
//        MobileListingSteps mobileListingSteps = new MobileListingSteps(mobilePage);

        page.navigate(BOOKING_URL);

        mobileHomeSteps
                .setViewPort(375, 668)
                .reloadPage()
//                .reloadPage() // რელოადს აზრი არ აქვს
                .validateHamburgerMenuIsVisible()
//                .clickDatesSearchBox()
//                .selectCheckInDate(CHECK_IN_DATE) // ამათაც აზრი არ აქვს ალბათ წავშლი
//                .selectCheckOutDate("2025-06-20")
                .fillSearchInput(SEARCH_BATUMI)
                .initiateSearch();
        mobileListingSteps
//                .setViewPort(375, 667)
//                .reloadPage()
                .collapseCalendar()
                .validateOffersAreInSingleColumn()
                .validateHeaderIsStickyOnScroll();
    }
}
