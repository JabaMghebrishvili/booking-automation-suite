package com.booking.steps.uiresponsiveness;

import com.booking.data.Constants;
import com.booking.pages.uiresponsiveness.TabletHomePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import io.qameta.allure.Step;
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

    @Step("Validate that the search bar is visible")
    public TabletHomeSteps validateSearchBarIsVisible() {
        assertThat(tabletHomePage.searchBar).isVisible();

        return this;
    }

    @Step("Fill search input with text: {text}")
    public TabletHomeSteps fillSearchInput(String text) {
        tabletHomePage.searchInput.fill(text);

        return this;
    }

    @Step("Initiate search by pressing Enter key")
    public TabletHomeSteps initiateSearch() {
        tabletHomePage.searchInput.press("Enter");
        return this;
    }

    @Step("Validate that footer links are vertically stacked")
    public TabletHomeSteps validateFooterLinksAreVerticallyStacked() {
        int groupCount = tabletHomePage.footerGroups.count();
        Assert.assertTrue(groupCount > 0);

        for (int i = 0; i < groupCount; i++) {
            Locator group = tabletHomePage.footerGroups.nth(i);

            //  style ატრიბუტის ვალიდაცია
            String styleAttribute = group.getAttribute("style");
            Assert.assertTrue(styleAttribute.contains(Constants.STACK));

            //  ლინკების ვერტიკალური სტაკინგის ვალიდაცია
            Locator links = tabletHomePage.footerGroupLinks(i);
            int linkCount = links.count();
            Assert.assertTrue(linkCount > 0);

            BoundingBox box0 = links.nth(0).boundingBox();
            int expectedX = (int) box0.x; // პირველივე ლინკის x კოორდინატი

            for (int j = 0; j < linkCount; j++) {
                BoundingBox box = links.nth(j).boundingBox();
                int currentX = (int) box.x;

                Assert.assertEquals(currentX, expectedX);
            }
        }

        logger.info("Footer links are vertically stacked by X and have expected style attributes.");
        return this;
    }
}
