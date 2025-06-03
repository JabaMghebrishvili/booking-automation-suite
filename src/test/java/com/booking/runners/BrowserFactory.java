package com.booking.runners;

import com.booking.tests.UIResponsivenessTests2;
import com.microsoft.playwright.*;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import static com.booking.data.Constants.*;

public class BrowserFactory {
    @Factory
    @Parameters({"browserType", "deviceType"})
    public Object[] createBrowserInstances(String browserType, String deviceType) {
        Playwright playwright = Playwright.create();
        BrowserType type;

        switch (browserType.toLowerCase()) {
            case "chromium" -> type = playwright.chromium();
            case "webkit" -> type = playwright.webkit();
            case "firefox" -> type = playwright.firefox();
            default -> throw new IllegalArgumentException(UNSUPPORTED_BROWSER + browserType);
        }

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

        Browser browser = type.launch(new BrowserType.LaunchOptions().setHeadless(false));
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(width, height)
                .setDeviceScaleFactor(2.0)
                .setIsMobile(deviceType.equalsIgnoreCase("mobile"));
        BrowserContext context = browser.newContext(contextOptions);
        Page page = context.newPage();

        return new Object[] {
                new UIResponsivenessTests2(page, browser, context, deviceType)
        };
    }
}
