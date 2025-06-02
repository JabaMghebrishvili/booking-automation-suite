package com.booking.steps;

import com.booking.model.OfferData;
import com.booking.pages.DetailsPage;
import com.booking.utils.DateUtils;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DetailsSteps {
    Page page;
    DetailsPage detailsPage;
    private static final Logger logger = LoggerFactory.getLogger(DetailsSteps.class);

    public DetailsSteps(Page page) {
        this.page = page;
        this.detailsPage = new DetailsPage(page);
    }

    public DetailsSteps validateStartDate(String expectedStartDate) {
        assertThat(detailsPage.startDate).hasText(DateUtils.formatDateForDisplay(expectedStartDate));
//        logger.info(DateUtils.formatDateForDisplay(expectedStartDate));
        logger.info(detailsPage.startDate.innerText());
        return this;
    }

    public DetailsSteps validateEndDate(String expectedEndDate) {
        assertThat(detailsPage.endDate).hasText(DateUtils.formatDateForDisplay(expectedEndDate));
        logger.info(detailsPage.endDate.innerText());
        return this;
    }

    public OfferData getOfferDataFromDetails() {
//        page.waitForLoadState(LoadState.LOAD);
//        assertThat(detailsPage.title).isVisible();
//        detailsPage.title.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        detailsPage.price.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        detailsPage.reviewScore.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

//        assertThat(detailsPage.price).containsText("GEL"); // წესით არც ეს ვეითი აღარ სჭირდება

        OfferData detailsData =  new OfferData(
                detailsPage.title.innerText().trim(),
                detailsPage.price.innerText().trim(),
                detailsPage.reviewScore.innerText().trim()
        );
//        logger.info("detailsPageData: {}", detailsPageData);
//        return detailsPageData;
        logger.info("From details: {}", detailsData);
        return detailsData;
    }

    public String getTitle() {
        return detailsPage.title.innerText().trim();
    }

    public DetailsSteps waitForOffersUpdate(){
//        searchPage.offerCards.all().forEach(card ->
//                card.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)));
        page.waitForFunction("document.readyState === 'complete'");
        page.waitForLoadState(LoadState.LOAD);
//        page.waitForFunction("() => document.querySelectorAll('.bc2204a477').length > 0");
        return this;
    }

    public OfferData getDetailsOfferData() {
        String title = detailsPage.title.innerText().trim();
        String price = detailsPage.price.innerText().trim();
        String rating = detailsPage.reviewScore.innerText().trim();

        logger.info("Offer from details page: title={}, price={}, rating={}", title, price, rating);
        return new OfferData(title, price, rating);
    }

    public DetailsSteps getAddress(){
        detailsPage.address.hover();
        logger.info(detailsPage.address.innerText());
        return this;
    }

    public void setPage(Page page) {
        this.page = page;
        this.detailsPage = new DetailsPage(page); // დეტალების გვერდის ელემენტების თავიდან ინიციალიზაცია
    }
}
