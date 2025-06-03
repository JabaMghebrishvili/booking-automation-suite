package com.booking.steps;

import com.booking.enums.*;
import com.booking.model.OfferData;
import com.booking.pages.SearchPage;
import com.booking.utils.DateFormatter;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.booking.data.Constants.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SearchSteps {
    Page page;
    SearchPage searchPage;
    private static final Logger logger = LoggerFactory.getLogger(SearchSteps.class);

    public SearchSteps(Page page) {
        this.page = page;
        this.searchPage = new SearchPage(page);
    }

    @Step("Validate search results title is: {title}")
    public SearchSteps validateSearchResultsTitle(String title) {
        assertThat(searchPage.searchResultsTitle).containsText(title);
        return this;
    }

    @Step("Validate all hotel addresses contain destination: {destination}")
    public SearchSteps validateHotelAddresses(String destination) {
        searchPage.offerAddress.all()
                .stream()
                .map(Locator::innerText)
                .forEach(addressText ->
                        Assert.assertTrue(addressText.contains(destination),
                                WRONG_ADDRESS + addressText));
        return this;
    }

    @Step("Wait for prices to be updated")
    public SearchSteps waitForPricesUpdate() {
        for (int i = 0; i < searchPage.discountedPrice.count(); i++) {
            Assert.assertTrue(searchPage.discountedPrice.nth(i).innerText().contains(CURRENCY_GEL));
        }
        return this;
    }

    @Step("Click on the first offer")
    public SearchSteps clickToFirstOffer() {
        searchPage.firstOffer.evaluate("el => el.removeAttribute('target')");
        searchPage.firstOffer.evaluate("element => element.click()");
//        searchPage.firstOffer.click();
        return this;
    }

    @Step("Validate start date is: {expectedStartDate}")
    public SearchSteps validateStartDate(String expectedStartDate) {
        assertThat(searchPage.startDate).hasText(DateFormatter.formatDateForDisplay(expectedStartDate));
        logger.info("Start date: {}", searchPage.startDate.innerText());
        return this;
    }

    @Step("Validate end date is: {expectedEndDate}")
    public SearchSteps validateEndDate(String expectedEndDate) {
        assertThat(searchPage.endDate).hasText(DateFormatter.formatDateForDisplay(expectedEndDate));
        logger.info("End date: {}", searchPage.endDate.innerText());
        return this;
    }

    @Step("Wait for offers to load and page to fully render")
    public SearchSteps waitForOffersUpdate(){
        page.waitForCondition(() ->
                !searchPage.offerCards.all().isEmpty()
        );
        page.waitForFunction("document.readyState === 'complete'");
        page.waitForLoadState(LoadState.LOAD);

        return this;
    }

    @Step("Set slider '{label}' to value: {targetValue}")
    public SearchSteps setSliderByAriaLabel(String label, Price targetValue) {
//        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        int min = Integer.parseInt(searchPage.minSlider.getAttribute("min"));
        int max = Integer.parseInt(searchPage.maxSlider.getAttribute("max"));

        BoundingBox divBox = searchPage.sliderParentDiv.boundingBox();
        BoundingBox handleBox = searchPage.handle(label).boundingBox();

        double startX = handleBox.x + handleBox.width / 2;
        double y = handleBox.y + handleBox.height / 2;

        double percent = (targetValue.getValue() - min) / (double)(max - min);  // targetValue
        double targetX = divBox.x + percent * divBox.width;

        page.mouse().move(startX, y);
        page.mouse().down();
        page.mouse().move(targetX, y, new Mouse.MoveOptions().setSteps(10));
        page.mouse().up();

        logger.info("{} slider moved to {}", label, searchPage.input(label).getAttribute("value"));
        return this;
    }

    @Step("Validate all initial prices per night are within selected range")
    public SearchSteps validateInitialPricesPerNightInRange() {
        String nightsText = searchPage.nightsCount.first().innerText().trim(); // "8 nights, 2 adults"
        int nights = Integer.parseInt(nightsText.split(" ")[0]);

        // ფასის დიაპაზონი filter-დან
        String rangeText = searchPage.priceInFilter.innerText(); //  "GEL 30 - GEL 400+"
        Pattern rangePattern = Pattern.compile("(\\d+)");
        Matcher matcher = rangePattern.matcher(rangeText);
        List<Integer> rangeNumbers = new ArrayList<>();
        while (matcher.find()) {
            rangeNumbers.add(Integer.parseInt(matcher.group()));
        }
        int min = rangeNumbers.get(0);
        int max = rangeNumbers.get(1);

        // ფასების ვალიდაცია
        for (int i = 0; i < searchPage.discountedPrice.count(); i++) {
            String priceText = searchPage.discountedPrice.nth(i).innerText(); // "GEL 1,200"
            String numeric = priceText.replaceAll("[^\\d]", "");     // "1200"

            if (numeric.isEmpty()) continue;

            int total = Integer.parseInt(numeric);
            int perNight = total / nights;

            logger.info("ფასის შემოწმება: {} (ღამეები: {})", perNight, nights);
            Assert.assertTrue(perNight >= min && perNight <= max);
        }
        return this;
    }

    @Step("Select rating filter: {rating}")
    public SearchSteps selectRating(Rating rating) {
        searchPage.targetRatingOption(rating.getValue()).click();
        return this;
    }

    @Step("Validate all offers have rating: {expectedRating}")
    public SearchSteps validateOfferRatings(Rating expectedRating) {
        int expectedStars = Integer.parseInt(expectedRating.getValue().split(" ")[0]);
        List<Locator> cards = searchPage.offerCards.all();

        for (int i = 0; i < cards.size(); i++) {
            Locator card = cards.get(i);
            Locator starsLocator = searchPage.getRatingStarsLocatorForCard(card);

            starsLocator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            int actualStarsCount = starsLocator.count();
            logger.info("Offer [{}]: expected stars = {}, actual stars = {}", i, expectedStars, actualStarsCount);
            Assert.assertEquals(actualStarsCount, expectedStars, STARS_COUNT + actualStarsCount);
        }
        logger.info("offers size: {}", cards.size());
        logger.info("All {} offers have correct rating of {} stars.", cards.size(), expectedStars);
        return this;
    }

    @Step("Select meal option: {meals}")
    public SearchSteps selectMeals(Meals meals) {
        searchPage.mealsOption(meals.getValue()).click();
        return this;
    }

    @Step("Validate all offers include meal: {meals}")
    public SearchSteps validateMealsIncluded(Meals meals) {
        searchPage.recommendedUnits.all()
                .stream()
                .map(Locator::innerText)
                .forEach(text ->
                        Assert.assertTrue(
                                text.contains(meals.getValue()), DOES_NOT_INCLUDE_MEAL + text
                        )
                );
        return this;
    }

    @Step("Select reservation option: {option}")
    public SearchSteps selectReservationOption(ReservationPolicy option) {
        searchPage.reservationPolicyOption(option.getValue()).click();
        return this;
    }

    @Step("Validate all recommended units include reservation option: {option}")
    public SearchSteps validateReservationIncluded(ReservationPolicy option) {
        for (int i=0; i < searchPage.recommendedUnits.count(); i++) {
            assertThat(searchPage.recommendedUnits.nth(i)).containsText(option.getValue());
        }
        logger.info("All recommended units have included {}", searchPage.recommendedUnits.first().innerText());
        return this;
    }

    @Step("Select review score option: {option}")
    public SearchSteps selectReviewScoreOption(ReviewScore option) {
        searchPage.reviewScoreOption(option.getValue()).click();
        return this;
    }

    @Step("Expand sorters dropdown")
    public SearchSteps expandSortersDropdown() {
        searchPage.sortersDropdownButton.click();
        return this;
    }

    @Step("Choose top reviewed option from sorters")
    public SearchSteps chooseTopReviewedOption() {
        searchPage.topReviewedOption.click();
        return this;
    }

    @Step("Validate all offers have review score ≥ {expectedScore}")
    public SearchSteps validateTopRatedScores(ReviewScore expectedScore) {
        double expectedMinScore = Double.parseDouble(expectedScore.getValue().replaceAll("[^\\d.]+", ""));
        List<Locator> cards = searchPage.offerCards.all();

        for (int i = 0; i < cards.size(); i++) {
            String scoreText = searchPage.reviewScores.nth(i).textContent().trim(); // "Scored 9.6"
            double actualScore = Double.parseDouble(scoreText.replaceAll("[^\\d.]+", "")); // "9.6"

            Assert.assertTrue(expectedMinScore <= actualScore);

            logger.info("actual score: {}", actualScore);
        }
        logger.info("All offers have review scores ≥ {}", expectedMinScore);
        return this;
    }

    @Step("Wait for rating scores update")
    public SearchSteps waitForRatingScoresUpdate() {
        searchPage.reviewScores.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return this;
    }

    @Step("Validate review scores are sorted in descending order")
    public SearchSteps validateScoresAreSortedCorrectly() {
        List<Double> scores = new ArrayList<>();
        int count = searchPage.reviewScores.count();

        for (int i = 0; i < count; i++) {
            String scoreText = searchPage.reviewScores.nth(i).textContent().trim();//  "Scored 9.4"
            logger.info("score text: {}", scoreText);
            double score = Double.parseDouble(scoreText.replaceAll("[^\\d.]+", ""));
            scores.add(score);
        }

        for (int i = 1; i < scores.size(); i++) {
            Assert.assertTrue(scores.get(i) <= scores.get(i - 1), SCORES_NOT_SORTED_CORRECTLY);
        }
        logger.info("Found scores count: {}", count);
        return this;
    }

    @Step("Get offer data from first offer card")
    public OfferData getFirstOfferData() {
        OfferData listingData =  new OfferData(
                searchPage.offerTitle.first().innerText().trim(),
                searchPage.discountedPrice.first().innerText().trim(),
                searchPage.reviewScores.first().innerText().trim()
        );
        logger.info("Offer data from listing : {}", listingData);
        return listingData;
    }


    @Step("Validate guests count: {count} adults in each offer")
    public SearchSteps validateGuestsCount(int count) {
        for (int i = 0; i < searchPage.offerCards.count(); i++) {
            assertThat(searchPage.nightsCount.nth(i)).containsText(count +" adults");
        }
        return this;
    }

    @Step("Validate nights count between {checkIn} and {checkOut}")
    public SearchSteps validateNightsCount(String checkIn, String checkOut) {
        LocalDate inDate = LocalDate.parse(checkIn);
        LocalDate outDate = LocalDate.parse(checkOut);
        long nights = ChronoUnit.DAYS.between(inDate, outDate);

        String expectedText;
        if (nights == 7) {
            expectedText = "1 week";
        } else if (nights == 14) {
            expectedText = "2 weeks";
        } else if (nights == 21) {
            expectedText = "3 weeks";
        } else if (nights == 28) {
            expectedText = "4 weeks";
        } else {
            expectedText = nights + " nights";
        }

        for (int i = 0; i < searchPage.nightsCount.count(); i++) {
            String fullText = searchPage.nightsCount.nth(i).textContent().toLowerCase(); //  "1 week, 2 adults"
            String nightsPart = fullText.split(",")[0].trim(); // "1 week" or "10 nights"

            Assert.assertEquals(nightsPart, expectedText.toLowerCase());
        }
        return this;
    }

    @Step("Change layout to grid view")
    public SearchSteps changeLayoutToGrid(){
//        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForLoadState(LoadState.LOAD);
        searchPage.gridLayoutButton.click();
        return this;
    }

    @Step("Validate grid layout: maximum 3 offers per row")
    public SearchSteps validateGridLayoutHasThreeOffersPerRow() {
        page.waitForLoadState(LoadState.LOAD);

        int offerCount = searchPage.offerCards.count();
        Assert.assertTrue(offerCount > 0);

        // Map-ის გამოყენებით ვალაგებთ offer-ებს row-ებად y კოორდინატის მიხედვით
        // key = y coordinate, value = list of x coordinates
        Map<Integer, List<Integer>> rowMap = new TreeMap<>(); // y -> list of x positions

        for (int i = 0; i < offerCount; i++) {
            BoundingBox box = searchPage.offerCards.nth(i).boundingBox();

            if (box == null) {
                throw new AssertionError(BOUNDING_BOX_IS_NULL + i);
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
            Assert.assertTrue(xPositions.size() <= 3, MORE_THAN_THREE_ROW + rowY);
        }
        logger.info("Grid layout validation passed: Max 3 offers per row.");

        return this;
    }
}
