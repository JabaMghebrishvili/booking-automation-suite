package com.booking.steps.uiresponsiveness;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSteps {
    Page page;
    private static final Logger logger = LoggerFactory.getLogger(BaseSteps.class);

    public BaseSteps(Page page) {
        this.page = page;
    }

    @Step("Set viewport size to width: {width}, height: {height}")
    public BaseSteps setViewPort(int width, int height) {
        page.setViewportSize(width, height);

        int innerWidth = (int) page.evaluate("() => window.innerWidth");
        int innerHeight = (int) page.evaluate("() => window.innerHeight");
        logger.info("Viewport size: width: {}, height: {}", innerWidth, innerHeight);

        return this;
    }
}
