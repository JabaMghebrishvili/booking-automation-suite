package com.booking.steps;

import com.booking.enums.*;
import com.booking.model.OfferData;
import com.booking.pages.SearchPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SearchSteps {
    Page page;
    SearchPage searchPage;
    private static final Logger logger = LoggerFactory.getLogger(SearchSteps.class);

    public SearchSteps(Page page) {
        this.page = page;
        this.searchPage = new SearchPage(page);
    }

    public SearchSteps validateSearchResultsTitle(String title) {
//        searchPage.searchResultsTitle.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(searchPage.searchResultsTitle).containsText(title);

        logger.info("Search results count: " + searchPage.offerCards.count());

        return this;
    }

    public SearchSteps validateHotelAddresses(String destination) {
//        assertThat(searchPage.hotelAddress).containsText("Batumi");

        for (int i = 0; i < searchPage.offerAddress.count(); i++) {
            String addressText = searchPage.offerAddress.nth(i).innerText();
            Assert.assertTrue(addressText.contains(destination));
        }
        return this;
    }

    public SearchSteps waitForPricesUpdate() {
        for (int i = 0; i < searchPage.discountedPrice.count(); i++) {
//            String addressText = searchPage.discountedPrice.nth(i).innerText();
            Assert.assertTrue(searchPage.discountedPrice.nth(i).innerText().contains("GEL"));
        }
        return this;
    }

    public SearchSteps clickToFirstOffer() {
        searchPage.firstOffer.click();

        return this;
    }


    public Page clickToFirstOfferAndReturnDetailsPage() {
        // დაჭერა ახალი ტაბის
        Page newTab = searchPage.getPage().waitForPopup(() -> {
            searchPage.firstOffer.click();
        });

        newTab.waitForLoadState(LoadState.DOMCONTENTLOADED);

        return newTab;
    }

    public DetailsSteps clickToFirstOfferAndSwitchToDetailsTab(DetailsSteps detailsSteps) {
        Page newTab = searchPage.getPage().waitForPopup(() -> {
            searchPage.firstOffer.click();
        });

        newTab.waitForLoadState(LoadState.DOMCONTENTLOADED);
        detailsSteps.setPage(newTab);
        return detailsSteps;
    }

    public SearchSteps clickToMinSlider(int value) {
//        searchPage.firstOffer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        searchPage.minSlider.click();
        searchPage.minSliderHandle.scrollIntoViewIfNeeded();
        setSliderToValue(page, searchPage.minSliderHandle, searchPage.minSlider, searchPage.sliderParentDiv, value);
        return this;
    }

    public SearchSteps waitForOffersUpdate(){
//        searchPage.offerCards.all().forEach(card ->
//                card.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)));

        page.waitForCondition(() ->
                !searchPage.offerCards.all().isEmpty()
        );

        page.waitForFunction("document.readyState === 'complete'");

        page.waitForLoadState(LoadState.LOAD);

//        page.waitForFunction("() => document.querySelectorAll('.bc2204a477').length > 0");


        return this;
    }

    public SearchSteps clickToMaxSlider(int value) {
//        searchPage.firstOffer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        searchPage.maxSlider.click();
        searchPage.maxSliderHandle.scrollIntoViewIfNeeded();
        setSliderToValue(page, searchPage.maxSliderHandle, searchPage.maxSlider, searchPage.sliderParentDiv, value);
        return this;
    }


    public static void setSliderToValue(Page page, Locator sliderHandle, Locator relatedInput, Locator sliderParentDiv, int targetValue) {
        // ამოღება min/max მნიშვნელობების aria-valuetext-იდან
        String valueText = relatedInput.getAttribute("aria-valuetext");
        String[] parts = valueText.split("-");
        int min = Integer.parseInt(parts[0].trim());
        int max = Integer.parseInt(parts[1].trim());

//        sliderHandle.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        // slider-ის current განლაგება
        BoundingBox handleBox = sliderHandle.boundingBox();
        double startX = handleBox.x + handleBox.width / 2;
        double startY = handleBox.y + handleBox.height / 2;

        // parent slider input-ის მშობელი ელემენტი
//        Locator sliderTrack = relatedInput.locator("xpath=.."); // ან შესაბამისი ზუსტი ლოკატორი
        BoundingBox trackBox = sliderParentDiv.boundingBox();
        double trackStartX = trackBox.x;
        double trackWidth = trackBox.width;

        // %–ით დათვლილი მიზნობრივი X პიქსელი
        double percent = (targetValue - min) / (double)(max - min);
        double targetX = trackStartX + percent * trackWidth;

        // მოძრაობა slider-ის პოზიციიდან მიზანზე

        page.mouse().move(startX, startY);
        page.mouse().down();
        page.mouse().move(targetX, startY, new Mouse.MoveOptions().setSteps(20)); // ვცადე 20-იც, 30-იც..
        page.mouse().up();

//        page.waitForCondition(() -> {
//            String actualValue = relatedInput.getAttribute("value");
//            return actualValue.equals(String.valueOf(targetValue));
//        });

        // ლოგირება ახალი მნიშვნელობის
        String updatedValue = relatedInput.getAttribute("value");
        System.out.println("Current value: " + updatedValue);
    }

    public static void setSliderToValue2(Page page, Locator sliderHandle, Locator relatedInput, Locator sliderParentDiv, int targetValue) {
        // min/max მნიშვნელობების ამოღება
        String valueText = relatedInput.getAttribute("aria-valuetext");
        String[] parts = valueText.split("-");
        int min = Integer.parseInt(parts[0].trim());
        int max = Integer.parseInt(parts[1].trim());

        int step = 10; // თუ სხვაა slider-ზე მითითებული, შეცვალე აქაც

        // slider-ის მშობლის ზომის გამოთვლა
        BoundingBox trackBox = sliderParentDiv.boundingBox();
        double trackStartX = trackBox.x;
        double trackWidth = trackBox.width;

        int stepCount = (max - min) / step;
        double stepWidth = trackWidth / stepCount;

        // დათვლა - რამდენი ნაბიჯით გადავიდეს
        int stepIndex = (targetValue - min) / step;
        double targetX = trackStartX + (stepWidth * stepIndex);

        // slider-ის handle-ის ცენტრი
        BoundingBox handleBox = sliderHandle.boundingBox();
        double startX = handleBox.x + handleBox.width / 2;
        double startY = handleBox.y + handleBox.height / 2;

        // მოძრაობა slider-ზე
        page.mouse().move(startX, startY);
        page.mouse().down();
        page.mouse().move(targetX, startY, new Mouse.MoveOptions().setSteps(10)); // ნაკლები step უფრო სტაბილურია slider-ებისთვის
        page.mouse().up();

        // დაველოდოთ რომ ვალუე ნამდვილად განახლდა
        page.waitForCondition(() -> {
            String actualValue = relatedInput.getAttribute("value");
            return actualValue != null && actualValue.equals(String.valueOf(targetValue));
        });

        // ლოგი
        String updatedValue = relatedInput.getAttribute("value");
        System.out.println("Current value: " + updatedValue);
    }


    public SearchSteps setSliderRange(int minValue, int maxValue) {
        Locator minSliderInput = page.locator("input[aria-label='Min.']").first();
        Locator maxSliderInput = page.locator("input[aria-label='Max.']").first();

        // მოვიცადოთ სანამ ელემენტები გამოჩნდება
        minSliderInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        maxSliderInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // set value via JS eval, avoiding flakiness
        minSliderInput.evaluate("el => el.value = '" + minValue + "'");
        maxSliderInput.evaluate("el => el.value = '" + maxValue + "'");

        // trigger input & change events manually
        minSliderInput.dispatchEvent("input");
        minSliderInput.dispatchEvent("change");
        maxSliderInput.dispatchEvent("input");
        maxSliderInput.dispatchEvent("change");

        System.out.println("Slider updated to min=" + minSliderInput.getAttribute("value") + ", max=" + maxSliderInput.getAttribute("value"));
        return this;
    }

    public SearchSteps setSliderByAriaLabel(String label, int targetValue) {
        Locator input = page.locator("input[aria-label='" + label + "']").first();
        Locator handle = input.locator("xpath=following-sibling::div[1]").first();
        Locator track = page.locator("div.b99b6ef58f.a10a015434").nth(1); // slider parent container

//        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // ამოღება min/max მონაცემები input-დან
        int min = Integer.parseInt(input.getAttribute("min"));
        int max = Integer.parseInt(input.getAttribute("max"));

        // Track და handle ზომები
        BoundingBox trackBox = track.boundingBox();
        BoundingBox handleBox = handle.boundingBox();

        double startX = handleBox.x + handleBox.width / 2;
        double y = handleBox.y + handleBox.height / 2;

        // slider სიგრძე და მიზნობრივი პიქსელი
        double percent = (targetValue - min) / (double)(max - min);
        double targetX = trackBox.x + percent * trackBox.width;

        // გადაადგილება
        page.mouse().move(startX, y);
        page.mouse().down();
        page.mouse().move(targetX, y, new Mouse.MoveOptions().setSteps(10));
        page.mouse().up();

        System.out.printf("%s slider moved to %s", label, input.getAttribute("value"));
        return this;
    }

    public SearchSteps setSliderByAriaLabel2(String label, int targetValue) {
        Locator input = page.locator("input[aria-label='" + label + "']").first();
        Locator handle = input.locator("xpath=following-sibling::div").first(); // აქ ეწერა div[1]
        Locator track = page.locator("div.b99b6ef58f.a10a015434").nth(1); // slider parent container

//        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // ამოღება min/max მონაცემები input-დან
        int min = Integer.parseInt(searchPage.minSlider.getAttribute("min"));
        int max = Integer.parseInt(searchPage.maxSlider.getAttribute("max"));

        // Track და handle ზომები
        BoundingBox trackBox = searchPage.sliderParentDiv.boundingBox();
        BoundingBox handleBox = handle.boundingBox();

        double startX = handleBox.x + handleBox.width / 2;
        double y = handleBox.y + handleBox.height / 2;

        // slider სიგრძე და მიზნობრივი პიქსელი
        double percent = (targetValue - min) / (double)(max - min);
        double targetX = trackBox.x + percent * trackBox.width;

        // გადაადგილება
        page.mouse().move(startX, y);
        page.mouse().down();
        page.mouse().move(targetX, y, new Mouse.MoveOptions().setSteps(10)); // აქ 10-ზე მუშაობდა
        page.mouse().up();

        System.out.printf("%s slider moved to %s", label, input.getAttribute("value"));
        return this;
    }

    public SearchSteps setSliderByAriaLabel3(String label, Price targetValue) { // აქ იყო int targetValue
//        Locator input = page.locator("input[aria-label='" + label + "']").first();
//        Locator handle = input.locator("xpath=following-sibling::div").first(); // აქ ეწერა div[1]
//        Locator track = page.locator("div.b99b6ef58f.a10a015434").nth(1); // slider parent container

//        input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // ამოღება min/max მონაცემები input-დან
        int min = Integer.parseInt(searchPage.minSlider.getAttribute("min"));
        int max = Integer.parseInt(searchPage.maxSlider.getAttribute("max"));

        // Track და handle ზომები
        BoundingBox trackBox = searchPage.sliderParentDiv.boundingBox();
        BoundingBox handleBox = searchPage.handle(label).boundingBox();

        double startX = handleBox.x + handleBox.width / 2;
        double y = handleBox.y + handleBox.height / 2;

        // slider სიგრძე და მიზნობრივი პიქსელი
        double percent = (targetValue.getValue() - min) / (double)(max - min);  // აქ იყო პირდაპირ targetValue
        double targetX = trackBox.x + percent * trackBox.width;

        // გადაადგილება
        page.mouse().move(startX, y);
        page.mouse().down();
        page.mouse().move(targetX, y, new Mouse.MoveOptions().setSteps(10)); // აქ 10-ზე მუშაობდა
        page.mouse().up();

        System.out.printf("%s slider moved to %s", label, searchPage.input(label).getAttribute("value"));
        return this;
    }

    public SearchSteps fillMinPrice(PriceRange price) {
        searchPage.minSlider.fill(price.getValue());

        return this;
    }

    public SearchSteps fillMaxPrice(PriceRange price) {
        searchPage.maxSlider.fill(price.getValue());
        return this;
    }



//    public SearchSteps clickToMinSlider(int value) {
//        setSliderToValue(page, searchPage.minSliderHandle, searchPage.minSlider, value);
//        return this;
//    }
//
//    public SearchSteps clickToMaxSlider(int value) {
//        setSliderToValue(page, searchPage.maxSliderHandle, searchPage.maxSlider, value);
//        return this;
//    }



//    public static void setSliderToValue(Page page, Locator sliderHandle, Locator relatedInput, int targetValue) {
//        // ამოვიღოთ min და max შესაბამისი <input> ელემენტიდან
//        int min = Integer.parseInt(relatedInput.getAttribute("min"));
//        int max = Integer.parseInt(relatedInput.getAttribute("max"));
//
//        BoundingBox box = sliderHandle.boundingBox();
//
//        // slider handle-ის ცენტრალური წერტილი
//        double startX = box.x + box.width / 2;
//        double startY = box.y + box.height / 2;
//
//        // მთლიან სიგრძეზე 100% = max - min, შესაბამისად % გამოვთვალოთ
//        double percent = (targetValue - min) / (double)(max - min);
//        double totalSliderWidth = 300; // ან დააკვირდი slider-ს რეალურ სიგანეს boundingBox-ით
//        double offsetX = (percent * totalSliderWidth) - (box.x + box.width / 2 - box.x);
//
//        // მოძრაობა slider-ზე
//        page.mouse().move(startX, startY);
//        page.mouse().down();
//        page.mouse().move(startX + offsetX, startY, new Mouse.MoveOptions().setSteps(15));
//        page.mouse().up();
//
//        // დავპრინტოთ მიმდინარე მნიშვნელობა
//        String updatedValue = relatedInput.getAttribute("value");
//        System.out.println("Current value: " + updatedValue);
//    }

    public SearchSteps validateInitialPricesPerNightInRange() {
        // ამოვიღოთ ღამეების რაოდენობა
        String nightsText = searchPage.nightsCount.first().innerText().trim(); // მაგ. "8 nights, 2 adults"
        int nights = Integer.parseInt(nightsText.split(" ")[0]);

        // ამოვიღოთ ფასის დიაპაზონი filter-დან
        String rangeText = searchPage.priceInFilter.innerText(); // მაგ. "GEL 30 - GEL 400+"
        Pattern rangePattern = Pattern.compile("(\\d+)");
        Matcher matcher = rangePattern.matcher(rangeText);
        List<Integer> rangeNumbers = new ArrayList<>();
        while (matcher.find()) {
            rangeNumbers.add(Integer.parseInt(matcher.group()));
        }
        int min = rangeNumbers.get(0);
        int max = rangeNumbers.get(1);

        // ფასების ვალიდაცია ღამის მიხედვით
        for (int i = 0; i < searchPage.discountedPrice.count(); i++) {
            String priceText = searchPage.discountedPrice.nth(i).innerText(); // მაგ. "GEL 1,200"
            String numeric = priceText.replaceAll("[^\\d]", "");     // "1200"

            if (numeric.isEmpty()) continue;

            int total = Integer.parseInt(numeric);
            int perNight = total / nights;

            System.out.println("ფასის შემოწმება: " + perNight + " (ღამეები: " + nights + ")");
            Assert.assertTrue(perNight >= min && perNight <= max); // ეს ასე არუნდა იყოს მაგრამ სხვა გზა არ არის ჯერჯერობით..
        }
        return this;
    }

    public SearchSteps selectRating(Rating rating) {
//        String ratingText = rating.getValue(); // e.g. "2 stars"

//        Locator ratingOptions = page.locator("//span[text()='Property rating']//ancestor::fieldset//label");

//        Locator targetOption = searchPage.propertyRatingLabels.filter(new Locator.FilterOptions()
//                .setHasText(rating.getValue())); // იპოვის იმ label-ს რომელშიც არის ეს ტექსტი
//
//        targetOption.first().click(); // თუ იქნება მხოლოდ ერთი – first() საკმარისია
        searchPage.targetRatingOption(rating.getValue()).click();
        return this;
    }

    public SearchSteps validateOfferRatings(Rating expectedRating) {
//        searchPage.offerCards.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//        assertThat(searchPage.ratingStarsCount.first()).isVisible();
        int expectedStars = Integer.parseInt(expectedRating.getValue().split(" ")[0]);

        List<Locator> cards = searchPage.offerCards.all();
        int count = searchPage.offerCards.count();

        for (int i = 0; i < searchPage.offerCards.count(); i++) {
//            Locator card = cards.get(i);
//            Locator stars = card.getByTestId("rating-stars").locator("span");
            searchPage.ratingStarsCount.first().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE));

//            int actualStarsCount = searchPage.ratingStarsCount.nth(i).count(); // ამ დროს ფეილდება
            int actualStarsCount = searchPage.ratingStarsCount.count();
            logger.info("actual stars count: {}", actualStarsCount);

            Assert.assertEquals(expectedStars, actualStarsCount, "Actual Stars count: " + actualStarsCount);

        }
        logger.info("All offers have correct rating of {} stars.", expectedStars);
        logger.info("offers count {}: ", count);
        return this;
    }

    // ეს მეთოდი წესით იმავეს აკეთებს, რასაც validateOfferRatings, ამიტომ ეს წასაშლელია
    public SearchSteps validateOfferRatings2(Rating expectedRating) {
        int expectedStars = Integer.parseInt(expectedRating.getValue().split(" ")[0]);
        List<Locator> cards = searchPage.offerCards.all();

        for (int i = 0; i < cards.size(); i++) {
            Locator card = cards.get(i);
            Locator starsLocator = searchPage.getRatingStarsLocatorForCard(card);

            // დაელოდე ვარსკვლავის ელემენტებს
            starsLocator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));


            int actualStarsCount = starsLocator.count();
//            System.out.println("actual stars count: " + actualStarsCount);
            logger.info("Offer [{}]: expected stars = {}, actual stars = {}", i, expectedStars, actualStarsCount);
            Assert.assertEquals(actualStarsCount, expectedStars, "Stars count: " + actualStarsCount);
        }
        logger.info("offers size: {}", cards.size());
        logger.info("All {} offers have correct rating of {} stars.", cards.size(), expectedStars);
        return this;
    }


    public SearchSteps selectMeals(Meals meals) {
        searchPage.mealsOption(meals.getValue()).click();
        return this;
    }

    public SearchSteps validateMealsIncluded(Meals meals) {
        for (int i=0; i < searchPage.recommendedUnits.count(); i++) {

            assertThat(searchPage.recommendedUnits.nth(i)).containsText(meals.getValue()); // აქ თუ დაფეილდა nth(i)-ის ბრალი იქნება
        }

        logger.info("All recommended units have included {}", searchPage.recommendedUnits.first().innerText());
        return this;
    }

    public SearchSteps selectReservationOption(ReservationPolicy option) {

        searchPage.reservationPolicyOption(option.getValue()).click();
        return this;
    }

    public SearchSteps validateReservationIncluded(ReservationPolicy option) {
        for (int i=0; i < searchPage.recommendedUnits.count(); i++) {

            assertThat(searchPage.recommendedUnits.nth(i)).containsText(option.getValue()); // აქ თუ დაფეილდა nth(i)-ის ბრალი იქნება
        }
        logger.info("All recommended units have included {}", searchPage.recommendedUnits.first().innerText());
        return this;
    }

    public SearchSteps selectReviewScoreOption(ReviewScore option) {
        searchPage.reviewScoreOption(option.getValue()).click();
        return this;
    }

    public SearchSteps expandSortersDropdown() {
//        searchPage.sortersDropdownButton.evaluate("element => element.click()");
        searchPage.sortersDropdownButton.click();
        return this;
    }

    public SearchSteps chooseTopReviewedOption() {
        searchPage.topReviewedOption.click();
        return this;
    }

    public SearchSteps validateTopRatedScores(ReviewScore expectedScore) {
//        searchPage.reviewScores.first().waitFor(new Locator.WaitForOptions()
//                .setState(WaitForSelectorState.VISIBLE));

        double expectedMinScore = Double.parseDouble(expectedScore.getValue().replaceAll("[^\\d.]+", ""));

        List<Locator> cards = searchPage.offerCards.all();

        for (int i = 0; i < cards.size(); i++) {
//            Locator card = cards.get(i);
//            Locator reviewScoreLocator = card.getByTestId("review-score").locator("div.bc946a29db");

            String scoreText = searchPage.reviewScores.nth(i).textContent().trim(); // "Scored 9.6"
            double actualScore = Double.parseDouble(scoreText.replaceAll("[^\\d.]+", "")); // "9.6"

            Assert.assertTrue(expectedMinScore <= actualScore);

            System.out.println("actual score: " + actualScore);
        }
        logger.info("All offers have review scores ≥ {}", expectedMinScore);

        return this;
    }

    public SearchSteps waitForRatingScoresUpdate() {
        searchPage.reviewScores.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return this;
    }

    public SearchSteps validateScoresAreSortedCorrectly() {
//        List<Locator> cards = searchPage.offerCards.all();
//        searchPage.reviewScores.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        List<Double> scores = new ArrayList<>();
        int count = searchPage.reviewScores.count();

        for (int i = 0; i < count; i++) {
//            Locator scoreLocator = cards.get(i).getByTestId("review-score").locator("div.bc946a29db");

//            if (scoreLocator.count() == 0) continue; // skip cards without score

            String scoreText = searchPage.reviewScores.nth(i).textContent().trim();//  "Scored 9.4"
            System.out.println("score text: " + scoreText);
            double score = Double.parseDouble(scoreText.replaceAll("[^\\d.]+", ""));
            scores.add(score);
        }

        for (int i = 1; i < scores.size(); i++) {
            Assert.assertTrue(scores.get(i) <= scores.get(i - 1),
                    "Scores not sorted descending at position " + i +
                            ": " + scores.get(i - 1) + " -> " + scores.get(i));
//            System.out.println(i);
        }
        System.out.println("Found scores count: " + count);
        return this;
    }

    public OfferData getFirstOfferData() {
        OfferData listingData =  new OfferData(
                searchPage.offerTitle.first().innerText().trim(),
                searchPage.discountedPrice.first().innerText().trim(),
                searchPage.reviewScores.first().innerText().trim()
        );
        logger.info("First offer: {}", listingData);
        return listingData;
    }

    public SearchSteps validateGuestsCount(int count) {
        for (int i = 0; i < searchPage.offerCards.count(); i++) {
            assertThat(searchPage.nightsCount.nth(i)).containsText(count +" adults");
        }
        return this;
    }

    public SearchSteps validateNightCount(String checkIn, String checkOut) {
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

//    public SearchSteps validateScoresAreSortedDescending() {
//        List<Locator> cards = searchPage.offerCards.all();
//        List<Double> scores = new ArrayList<>();
//
//        for (int i = 0; i < cards.size(); i++) {
//            Locator scoreLocator = cards.get(i).getByTestId("review-score").locator("div.bc946a29db");
//
//            if (scoreLocator.count() == 0) continue; // skip cards without score
//
//            String scoreText = scoreLocator.first().textContent().trim(); // e.g., "Scored 9.4"
//            double score = Double.parseDouble(scoreText.replaceAll("[^\\d.]+", ""));
//            scores.add(score);
//        }
//
//        for (int i = 1; i < scores.size(); i++) {
//            Assert.assertTrue(
//                    scores.get(i) <= scores.get(i - 1),
//                    "Scores not sorted descending at position " + i +
//                            ": " + scores.get(i - 1) + " → " + scores.get(i)
//            );
//        }
//
//        return this;
//    }

}
