package com.booking.steps.uiresponsiveness;

import com.booking.pages.uiresponsiveness.TabletHomePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TabletHomeSteps {
    Page page;
    TabletHomePage tabletHomePage;
    private static final Logger logger = LoggerFactory.getLogger(TabletHomeSteps.class);

    public TabletHomeSteps(Page page){
        this.page = page;
        tabletHomePage = new TabletHomePage(page);
    }

    public TabletHomeSteps setViewPort(int width, int height) {
        page.setViewportSize(width, height);
        page.evaluate("() => window.dispatchEvent(new Event('resize'))");

        int innerWidth = (int) page.evaluate("() => window.innerWidth");
        int innerHeight = (int) page.evaluate("() => window.innerHeight");
        logger.info("Viewport size: width: {}, height: {}", innerWidth, innerHeight);

        return this;
    }

    public TabletHomeSteps validateSearchBarIsVisible() {
        assertThat(tabletHomePage.searchBar).isVisible();

        return this;
    }

    public TabletHomeSteps fillSearchInput(String text) {
        tabletHomePage.searchInput.fill(text);

        return this;
    }

    public TabletHomeSteps initiateSearch() {
        tabletHomePage.searchInput.press("Enter");
        return this;
    }

    public TabletHomeSteps validateFooterLinksAreVerticallyStacked() {
        int groupCount = tabletHomePage.footerGroups.count();
        Assert.assertTrue(groupCount > 0, "No footer groups found!");

        for (int i = 0; i < groupCount; i++) {
            Locator links = tabletHomePage.footerGroupLinks(i);
            int linkCount = links.count();
            Assert.assertTrue(linkCount > 0, "No links in footer group " + i);

            int previousY = -1;

            for (int j = 0; j < linkCount; j++) {
                BoundingBox box = links.nth(j).boundingBox();
                Assert.assertNotNull(box, "BoundingBox is null for link " + j + " in group " + i);
                int currentY = (int) box.y;

                logger.info("Group [{}] Link [{}] Y: {}", i, j, currentY);

                // თითოეული y მომდევნო უნდა იყოს წინაზე მეტი
                if (previousY != -1) {
                    Assert.assertTrue(currentY > previousY, "Links are not vertically stacked in group " + i);
                }

                previousY = currentY;
            }

            logger.info("Group [{}] is vertically stacked", i);
        }

        return this;
    }

    public TabletHomeSteps validateFooterLinksAreVerticallyStackedByX() {
        int groupCount = tabletHomePage.footerGroups.count();
        Assert.assertTrue(groupCount > 0, "No footer groups found!");

        for (int i = 0; i < groupCount; i++) {
            Locator links = tabletHomePage.footerGroupLinks(i);
            int linkCount = links.count();
            Assert.assertTrue(linkCount > 0, "No links in footer group " + i);

            Integer expectedX = null;

            for (int j = 0; j < linkCount; j++) {
                BoundingBox box = links.nth(j).boundingBox();
                Assert.assertNotNull(box, "BoundingBox is null for link " + j + " in group " + i);
                int currentX = (int) box.x;

                logger.info("Group [{}] Link [{}] X: {}", i, j, currentX);

                if (expectedX == null) {
                    expectedX = currentX;
                } else {
//                    Assert.assertTrue(
//                            Math.abs(currentX - expectedX) <= 3,
//                            "Link " + j + " in group " + i + " is not vertically aligned. X: " + currentX + ", expected: " + expectedX
//                    );
                    Assert.assertEquals(currentX, expectedX,
                            "Link " + j + " in group " + i + " is not vertically aligned. X: " + currentX + ", expected: " + expectedX);
                }
            }

            logger.info("Group [{}] links are vertically aligned by X", i);
        }

        return this;
    }

    public TabletHomeSteps validateFooterLinksAreVerticallyStackedByX2() {
        int groupCount = tabletHomePage.footerGroups.count();
        Assert.assertTrue(groupCount > 0, "No footer groups found");

        for (int i = 0; i < groupCount; i++) {
            Locator group = tabletHomePage.footerGroups.nth(i);

            //  Validate style attribute
            String styleAttribute = group.getAttribute("style");
//            Assert.assertNotNull(styleAttr, "Group " + i + " has no style attribute");
            Assert.assertTrue(
                    styleAttribute.contains("stack"),
                    "Group " + i + " does not have the expected stack spacing in style attribute"
            );

            //  Validate vertical stacking by checking x-coordinates of <li> links
            Locator links = group.locator("li");
            int linkCount = links.count();
            Assert.assertTrue(linkCount > 0, "Group " + i + " has no footer links");

            // expected x = x of first link
            BoundingBox box0 = links.nth(0).boundingBox();
//            Assert.assertNotNull(box0, "BoundingBox is null for first link in group " + i);
            int expectedX = (int) box0.x; // პირველივე ლინკის x კოორდინატი

            for (int j = 0; j < linkCount; j++) {
                BoundingBox box = links.nth(j).boundingBox();
//                Assert.assertNotNull(box, "BoundingBox is null for link " + j + " in group " + i);
                int currentX = (int) box.x;

                Assert.assertEquals(currentX, expectedX,
                        "Link " + j + " in group " + i + " is not vertically aligned. X: " + currentX + ", expected: " + expectedX);
            }
        }

        logger.info("Footer links are vertically stacked by X and have expected style attributes.");
        return this;
    }




}
