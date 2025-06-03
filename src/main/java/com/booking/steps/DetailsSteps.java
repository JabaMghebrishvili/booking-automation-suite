package com.booking.steps;

import com.booking.model.OfferData;
import com.booking.pages.DetailsPage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailsSteps {
    Page page;
    DetailsPage detailsPage;
    private static final Logger logger = LoggerFactory.getLogger(DetailsSteps.class);

    public DetailsSteps(Page page) {
        this.page = page;
        this.detailsPage = new DetailsPage(page);
    }

    @Step("Get offer data from details page")
    public OfferData getOfferDataFromDetails() {
        OfferData detailsData =  new OfferData(
                detailsPage.title.innerText().trim(),
                detailsPage.price.innerText().trim(),
                detailsPage.reviewScore.innerText().trim()
        );

        logger.info("From details: {}", detailsData);
        return detailsData;
    }

    @Step("Wait for offer details page to fully load")
    public DetailsSteps waitForDetailsLoaded(){
        page.waitForFunction("document.readyState === 'complete'");
        page.waitForLoadState(LoadState.LOAD);
        return this;
    }
}
