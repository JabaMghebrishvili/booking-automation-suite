package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.TabletListingPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
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

    public TabletListingSteps validateOffersAreVerticallyStacked() {
        page.waitForLoadState(LoadState.LOAD);

        int offerCount = tabletListingPage.offerCards.count();
        Assert.assertTrue(offerCount > 0, "No offer cards found!");

        BoundingBox firstBox = tabletListingPage.offerCards.nth(0).boundingBox();
        Assert.assertNotNull(firstBox, "Bounding box is null for first offer card");
        int expectedX = (int) firstBox.x;

        for (int i = 0; i < offerCount; i++) {
            BoundingBox box = tabletListingPage.offerCards.nth(i).boundingBox();
//            Assert.assertNotNull(box, "Bounding box is null for offer card " + i);
            int currentX = (int) box.x;

            logger.info("Offer [{}] - x: {}, y: {}", i, currentX, (int) box.y);

            Assert.assertEquals(currentX, expectedX,
                    "Offer " + i + " is not vertically aligned. X: " + currentX + ", expected: " + expectedX
            );
        }
        logger.info("All offer cards are vertically stacked (Tablet layout validated).");

        return this;
    }



}
