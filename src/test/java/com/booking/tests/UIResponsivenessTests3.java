package com.booking.tests;

import com.booking.runners.BaseTestResponsiveness;
import com.booking.steps.HomeSteps;
import com.booking.steps.SearchSteps;
import com.booking.steps.uiresponsiveness.*;
import io.qameta.allure.*;
import org.testng.annotations.*;

import static com.booking.data.Constants.*;

@Epic("Responsive UI Tests")
@Feature("Layout validation on different device viewports")
public class UIResponsivenessTests3 extends BaseTestResponsiveness {

    BaseSteps baseSteps;
    HomeSteps homeSteps;
    SearchSteps searchSteps;
    TabletHomeSteps tabletHomeSteps;
    TabletListingSteps tabletListingSteps;
    MobileHomeSteps mobileHomeSteps;
    MobileListingSteps mobileListingSteps;

    public UIResponsivenessTests3(String browserType, String deviceType) {
        super(browserType, deviceType);
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

    @Test(description = "[SQDTBC-T3001] Validate UI and search results on Desktop viewport")
    @Story("User uses desktop device to perform search and verifies UI elements and grid layout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates visibility of sign up/sign in buttons, navigation menu and that the search results " +
            "grid layout displays 3 offers per row on desktop viewport")
    @Link(name = "Jira Ticket", url = "https://fake-jira-link.com/SQDTBC-T3001")
    public void desktopViewPortTest() {
        if (!deviceType.equals("desktop")) return;
        homeSteps
                .validateSignUpButtonIsVisible()
                .validateSignInButtonIsVisible()
                .validateHamburgerMenuIsNotVisible()
                .validateNavigationMenuIsFullyVisible()
                .fillDestinationInput(SEARCH_LONDON)
                .waitAndValidateFirstOption(SEARCH_LONDON)
                .clickDatesSearchBox()
                .selectCheckInDate(CHECK_IN_DATE)
                .selectCheckOutDate(CHECK_OUT_DATE)
                .initiateSearch();
        searchSteps
                .changeLayoutToGrid()
                .validateGridLayoutHasThreeOffersPerRow();
    }

    @Test(description = "[SQDTBC-T3002] Validate UI and search results on Tablet viewport")
    @Story("User uses tablet device to perform search and verifies UI elements and vertical stacking of offers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates search bar visibility, footer links stacking and that offers are vertically stacked on tablet viewport")
    @Link(name = "Jira Ticket", url = "https://fake-jira-link.com/SQDTBC-T3002")
    public void tabletViewPortTest() {
        if (!deviceType.equals("tablet")) return;
        tabletHomeSteps
                .validateSearchBarIsVisible()
                .validateFooterLinksAreVerticallyStacked()
                .fillSearchInput(SEARCH_BATUMI)
                .initiateSearch();
        tabletListingSteps
                .validateOffersAreVerticallyStacked();
    }

    @Test(description = "[SQDTBC-T3003] Validate UI and search results on Mobile viewport")
    @Story("User uses mobile device to perform search and verifies hamburger menu, offer layout, and sticky header")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates hamburger menu visibility, single column offer layout, collapsed calendar and sticky header " +
            "on scroll in mobile viewport")
    @Link(name = "Jira Ticket", url = "https://your-jira-link.com/SQDTBC-T3003")
    public void mobileViewPortTest() {
        if (!deviceType.equals("mobile")) return;
        mobileHomeSteps
                .validateHamburgerMenuIsVisible()
                .fillSearchInput(SEARCH_BATUMI)
                .initiateSearch();
        mobileListingSteps
                .collapseCalendar()
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
