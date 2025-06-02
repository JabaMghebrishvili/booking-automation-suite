package com.booking.pages;

import com.booking.enums.Rating;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SearchPage {
    Page page;
    public Locator searchResultsTitle;
    public Locator offerCards;
    public Locator offerAddress;
    public Locator firstOffer;
    public Locator minSlider;
    public Locator maxSlider;
    public Locator minSliderHandle;
    public Locator maxSliderHandle;
    public Locator sliderParentDiv;
    public Locator filtersHistogram;
    public Locator mapImage;
    public Locator discountedPrice;
    public Locator nightsCount;
    public Locator priceInFilter;
    public Locator propertyRatingLabels;
    public Locator ratingStarsCount;
    public Locator mealsLabels;
    public Locator recommendedUnits;
    public Locator reservationPolicy;
    public Locator reviewScoreLabels;
    public Locator sortersDropdownButton;
    public Locator topReviewedOption;
    public Locator reviewScores;
    public Locator offerTitle;
//    public Locator input2;

    public SearchPage(Page page) {
        this.page = page;
        this.searchResultsTitle = page.locator("h1[aria-live]"); // h1.b87c397a13 ამ სელექტორითაც შეიძლება
        this.offerCards = page.getByTestId("property-card-container");
        this.offerAddress = offerCards.getByTestId("address");
        this.firstOffer = offerCards.getByTestId("property-card-desktop-single-image").first();
        this.minSlider = page.locator("div.e7e72a1761 input[aria-label='Min.']").first();
        this.maxSlider = page.locator("div.e7e72a1761 input[aria-label='Max.']").first();
        this.minSliderHandle = page.locator("div.fc835e65e6").first();
        this.maxSliderHandle = page.locator("div.fc835e65e6").last();
        this.sliderParentDiv = page.locator("div.d27b5e9710").first(); // div.e7e72a1761
        this.filtersHistogram = page.getByTestId("filters-group-histogram");
        this.mapImage = page.locator("div.a88a546fb2");
        this.discountedPrice = page.getByTestId("price-and-discounted-price");
        this.nightsCount = page.getByTestId("price-for-x-nights");
        this.priceInFilter = page.getByRole(AriaRole.STATUS).locator("div"); // [role='status'] div
        this.propertyRatingLabels = page.locator("//span[text()='Property rating']//ancestor::fieldset//label");
        this.ratingStarsCount = offerCards.getByTestId("rating-stars").locator("span");
//        this.ratingStarsCount = page.locator("[data-testid='rating-stars'] span");
        this.mealsLabels = page.locator("//span[text()='Meals']//ancestor::fieldset//label");
        this.recommendedUnits = offerCards.getByTestId("recommended-units");
        this.reservationPolicy = page.locator("//span[text()='Reservation policy']//ancestor::fieldset//label");
//        this.sortersDropdownButton = page.getByTestId("sorters-dropdown-trigger"); // ცოტა ხნით ამას დავაკომენტარებ
        this.sortersDropdownButton = page.locator("button[data-testid='sorters-dropdown-trigger']");
        this.reviewScoreLabels = page.locator("//span[text()='Review score']//ancestor::fieldset//label");
        this.topReviewedOption = page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Top reviewed"));
//        this.reviewScores = offerCards.getByTestId("review-score").locator("div.bc946a29db"); // [data-testid='review-score'] div.bc946a29db
        this.reviewScores = page.locator("[data-testid='review-score'] > div.bc946a29db");
        this.offerTitle = offerCards.getByTestId("title");
    }

    public Locator input(String label) {
        return page.locator("input[aria-label='" + label + "']").first();
    }

    public Locator handle(String label) {
        return input(label).locator("xpath=following-sibling::div").first();
    }

    public Locator targetRatingOption(String rating){
        return propertyRatingLabels.filter(new Locator.FilterOptions()
                .setHasText(rating)).first();
    }

    public Locator mealsOption(String label) {
        return mealsLabels.filter(new Locator.FilterOptions()
                .setHasText(label)).first();
    }

    public Locator reservationPolicyOption(String label) {
        return reservationPolicy.filter(new Locator.FilterOptions()
                .setHasText(label)).first();
    }

    public Locator reviewScoreOption(String label) {
        return reviewScoreLabels.filter(new Locator.FilterOptions()
                .setHasText(label)).first();
    }

    public Locator getRatingStarsLocatorForCard(Locator card) {
        return card.getByTestId("rating-stars").locator("span");
    }

    public Page getPage() {
        return this.page;
    }


}
