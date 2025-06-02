package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.DesktopListingPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DesktopListingSteps {
    Page page;
    DesktopListingPage desktopListingPage;
    private static final Logger logger = LoggerFactory.getLogger(DesktopListingSteps.class);

    public DesktopListingSteps(Page page) {
        this.page = page;
        desktopListingPage = new DesktopListingPage(page);
    }

    public DesktopListingSteps changeLayoutToGrid(){
        page.waitForLoadState(LoadState.LOAD);
        desktopListingPage.gridLayoutButton.click();
        return this;
    }

    public DesktopListingSteps validateGridLayoutHasThreeOffersPerRow() {
        page.waitForLoadState(LoadState.LOAD);

        int offerCount = desktopListingPage.offerCards.count();
        Assert.assertTrue(offerCount > 0, "No offer cards found!");

        // Map-ის გამოყენებით ვალაგებთ offer-ებს row-ებად y კოორდინატის მიხედვით
        // key = y coordinate, value = list of x coordinates
        Map<Integer, List<Integer>> rowMap = new TreeMap<>(); // y -> list of x positions

        for (int i = 0; i < offerCount; i++) {
            BoundingBox box = desktopListingPage.offerCards.nth(i).boundingBox();

            if (box == null) {
                throw new AssertionError("Bounding box is null for offer at index: " + i);
            }

            int x = (int) box.x;
            int y = (int) box.y;

            rowMap.computeIfAbsent(y, k -> new ArrayList<>()).add(x);
            logger.info("Offer [{}] - x: {}, y: {}", i, x, y);
        }

        // ვამოწმებთ ყველა row-ს
        for (Map.Entry<Integer, List<Integer>> entry : rowMap.entrySet()) {
            int rowY = entry.getKey();
            List<Integer> xPositions = entry.getValue();

            logger.info("Row at y={} has {} offers", rowY, xPositions.size());
            Assert.assertTrue(xPositions.size() <= 3, "More than 3 offers in row at y=" + rowY);
        }

        logger.info("Grid layout validation passed: Max 3 offers per row.");

        return this;
    }


}
