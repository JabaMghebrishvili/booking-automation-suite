package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.MobileListingPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MobileListingSteps {
    Page page;
    MobileListingPage mobileListingPage;
    private static final Logger logger = LoggerFactory.getLogger(MobileListingSteps.class);


    public MobileListingSteps(Page page) {
        this.page = page;
        mobileListingPage = new MobileListingPage(page);
    }

    public MobileListingSteps validateHeaderIsStickyOnScroll() {
        page.waitForLoadState(LoadState.LOAD);
        page.mouse().wheel(0, 500);

//        Assert.assertTrue(mobileListingPage.header.isVisible(), "Header is not sticky after scrolling");
        assertThat(mobileListingPage.header).isVisible();
        logger.info("Header remained sticky and visible after scroll.");

        return this;
    }

    public MobileListingSteps collapseCalendar() {
        page.waitForLoadState(LoadState.LOAD);
        mobileListingPage.datesSearchBox.click();
        return this;
    }

    public MobileListingSteps reloadPage(){
        page.reload();
        return this;
    }

    public MobileListingSteps setViewPort(int width, int height) {
        page.setViewportSize(width, height);

        int innerWidth = (int) page.evaluate("() => window.innerWidth");
        int innerHeight = (int) page.evaluate("() => window.innerHeight");
        logger.info("Viewport size: width: {}, height: {}", innerWidth, innerHeight);

        return this;
    }

    public MobileListingSteps validateOffersAreInSingleColumn() {
        int count = mobileListingPage.offers.count();

        for (int i = 0; i < count; i++) {
            BoundingBox boxI = mobileListingPage.offers.nth(i).boundingBox();

            for (int j = i + 1; j < count; j++) {
                BoundingBox boxJ = mobileListingPage.offers.nth(j).boundingBox();

                boolean horizontallySeparated = boxI.x + boxI.width <= boxJ.x || boxJ.x + boxJ.width <= boxI.x;

                Assert.assertFalse(horizontallySeparated,
                        String.format("Offers %d and %d are not stacked vertically (they are side-by-side)", i, j));
            }
        }

        logger.info("Mobile listing: all offers are in a single vertical column.");
        return this;
    }
}
