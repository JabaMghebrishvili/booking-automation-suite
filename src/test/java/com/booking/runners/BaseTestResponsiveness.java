package com.booking.runners;

import com.microsoft.playwright.*;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import static com.booking.data.Constants.*;

public abstract class BaseTestResponsiveness {
    protected Page page;
    protected Browser browser;
    protected BrowserContext context;
    protected String deviceType;

    public BaseTestResponsiveness(String browserType, String deviceType) {
        this.deviceType = deviceType;

        Playwright playwright = Playwright.create();

        BrowserType type = switch (browserType.toLowerCase()) {
            case "chromium" -> playwright.chromium();
            case "webkit" -> playwright.webkit();
            case "firefox" -> playwright.firefox();
            default -> throw new IllegalArgumentException(UNSUPPORTED_BROWSER + browserType);
        };

        int width, height;
        switch (deviceType.toLowerCase()) {
            case "desktop" -> {
                width = DESKTOP_WIDTH; height = DESKTOP_HEIGHT;
            }
            case "tablet" -> {
                width = TABLET_WIDTH; height = TABLET_HEIGHT;
            }
            case "mobile" -> {
                width = MOBILE_WIDTH; height = MOBILE_HEIGHT;
            }
            default -> throw new IllegalArgumentException(UNSUPPORTED_DEVICE + deviceType);
        }

        browser = type.launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(width, height)
                .setDeviceScaleFactor(2.0)
                .setIsMobile(deviceType.equalsIgnoreCase("mobile")));
        page = context.newPage();
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenshotOnFailure(ITestResult result) {
        if (!result.isSuccess()) {
            attachScreenshot("Failure screenshot - " + result.getName());
        }
    }

    @Attachment(value = "{0}", type = "image/png")
    public byte[] attachScreenshot(String name) {
        try {
            return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
