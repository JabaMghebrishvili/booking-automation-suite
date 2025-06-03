package com.booking.steps.uiresponsiveness;

import com.booking.data.Constants;
import com.booking.pages.uiresponsiveness.TabletListingPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class TabletListingSteps {
    Page page;
    TabletListingPage tabletListingPage;
    private static final Logger logger = LoggerFactory.getLogger(TabletListingSteps.class);

    public TabletListingSteps(Page page) {
        this.page = page;
        this.tabletListingPage = new TabletListingPage(page);
    }

    @Step("Validate that all offer cards are vertically stacked")
    public TabletListingSteps validateOffersAreVerticallyStacked() {
        page.waitForLoadState(LoadState.LOAD);

        int offerCount = tabletListingPage.offerCards.count();
        Assert.assertTrue(offerCount > 0, Constants.NO_OFFERS_FOUND);

        BoundingBox firstBox = tabletListingPage.offerCards.nth(0).boundingBox();
        Assert.assertNotNull(firstBox);
        int expectedX = (int) firstBox.x;

        for (int i = 0; i < offerCount; i++) {
            BoundingBox box = tabletListingPage.offerCards.nth(i).boundingBox();
            int currentX = (int) box.x;

            logger.info("Offer [{}] - x: {}, y: {}", i, currentX, (int) box.y);

            Assert.assertEquals(currentX, expectedX, Constants.NOT_VERTICALLY_ALIGNED);
        }
        logger.info("All offer cards are vertically stacked (Tablet layout validated).");

        return this;
    }
}
