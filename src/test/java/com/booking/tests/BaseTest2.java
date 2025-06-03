package com.booking.tests;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Arrays;

public class BaseTest2 {
    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;

    @BeforeClass
    public void setUp() {
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
        launchOptions.setArgs(Arrays.asList("--disable-extensions", "--start-maximized"));
        launchOptions.setHeadless(true);
//        launchOptions.setSlowMo(1150);
        browser = playwright.chromium().launch(launchOptions);
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
