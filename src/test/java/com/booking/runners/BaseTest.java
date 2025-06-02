package com.booking.runners;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

import java.util.Arrays;

public class BaseTest {
//    Playwright playwright;
//    Browser browser;
//    BrowserContext browserContext;
//    protected Page page;
//
//    @BeforeClass
//    public void setUp() {
//        playwright = Playwright.create();
//        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
//        launchOptions.setArgs(Arrays.asList("--disable-extensions", "--start-maximized"));
//        launchOptions.setHeadless(false);
////        launchOptions.setSlowMo(2150);
//        browser = playwright.chromium().launch(launchOptions);
////        browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
//        browserContext = browser.newContext();
//    }
//
//    @BeforeMethod
//    public void beforeMethod() {
//        page = browserContext.newPage();
//    }
//
//    @AfterClass
//    public void tearDown() {
//        browserContext.close();
//        browser.close();
//        playwright.close();
//    }

    Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    protected Page page;

    @Parameters({"browserType"})
    @BeforeClass
    public void setUp(@Optional("chrome") String browserType) {
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
        launchOptions.setArgs(Arrays.asList("--disable-extensions", "--start-maximized"));
        launchOptions.setHeadless(false);
//        launchOptions.setSlowMo(2000);
        if (browserType.equalsIgnoreCase("chrome")){
            browser = playwright.chromium().launch(launchOptions);
        } else if (browserType.equalsIgnoreCase("safari")) {
            browser = playwright.webkit().launch(launchOptions);
        }
//        browser = playwright.chromium().launch(launchOptions);
//        browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        browserContext = browser.newContext();
    }

    @BeforeMethod
    public void beforeMethod() {
        page = browserContext.newPage();
    }

    @AfterClass
    public void tearDown() {
        browserContext.close();
        browser.close();
        playwright.close();
    }
}
