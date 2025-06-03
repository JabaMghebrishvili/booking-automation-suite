package com.booking.tests;

import com.booking.steps.HomeSteps;
import com.booking.steps.SearchSteps;
import com.booking.steps.uiresponsiveness.*;
import com.microsoft.playwright.*;
import org.testng.annotations.*;
import static com.booking.data.Constants.*;


public class UIResponsivenessTests2 {

    private final Page page;
    private final Browser browser;
    private final BrowserContext context;
    private final String deviceType;

    BaseSteps baseSteps;
    HomeSteps homeSteps;
    SearchSteps searchSteps;
    TabletHomeSteps tabletHomeSteps;
    TabletListingSteps tabletListingSteps;
    MobileHomeSteps mobileHomeSteps;
    MobileListingSteps mobileListingSteps;

    public UIResponsivenessTests2(Page page, Browser browser, BrowserContext context, String deviceType) {
        this.page = page;
        this.browser = browser;
        this.context = context;
        this.deviceType = deviceType;
    }

    @BeforeMethod
    public void initSteps() {
        this.baseSteps = new BaseSteps(page);
        this.homeSteps = new HomeSteps(page);
        this.searchSteps = new SearchSteps(page);
        this.tabletHomeSteps = new TabletHomeSteps(page);
        this.tabletListingSteps = new TabletListingSteps(page);
        this.mobileHomeSteps = new MobileHomeSteps(page);
        this.mobileListingSteps = new MobileListingSteps(page);
        page.navigate(BOOKING_URL);
    }

    @Test(priority = 2)
    public void desktopViewPortTest() {
        if (!deviceType.equals("desktop")) return;
//        baseSteps.setViewPort(DESKTOP_WIDTH, DESKTOP_HEIGHT);
        homeSteps.validateSignUpButtonIsVisible()
                .validateSignInButtonIsVisible()
                .validateHamburgerMenuIsNotVisible()
                .validateNavigationMenuIsFullyVisible()
                .fillDestinationInput(SEARCH_LONDON)
                .waitAndValidateFirstOption(SEARCH_LONDON)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        searchSteps.changeLayoutToGrid()
                .validateGridLayoutHasThreeOffersPerRow();
    }

    @Test(priority = 1)
    public void tabletViewPortTest() {
        if (!deviceType.equals("tablet")) return;
//        baseSteps.setViewPort(TABLET_WIDTH, TABLET_HEIGHT);
        tabletHomeSteps.validateSearchBarIsVisible()
                .validateFooterLinksAreVerticallyStacked()
                .fillSearchInput(SEARCH_BATUMI)
                .initiateSearch();
        tabletListingSteps.validateOffersAreVerticallyStacked();
    }

    @Test
    public void mobileViewPortTest() {
        if (!deviceType.equals("mobile")) return;
//        baseSteps.setViewPort(MOBILE_WIDTH, MOBILE_HEIGHT);
        mobileHomeSteps.validateHamburgerMenuIsVisible()
                .fillSearchInput(SEARCH_BATUMI)
                .initiateSearch();
        mobileListingSteps.collapseCalendar()
                .validateOffersAreInSingleColumn()
                .validateHeaderIsStickyOnScroll();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
    }
}
