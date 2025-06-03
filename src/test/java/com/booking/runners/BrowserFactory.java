package com.booking.runners;

import com.booking.tests.UIResponsivenessTests;
import org.testng.annotations.Factory;

public class BrowserFactory {
    @Factory
    public Object[] createInstances() {
        return new Object[] {
                new UIResponsivenessTests("chromium", "desktop"),
                new UIResponsivenessTests("chromium", "tablet"),
                new UIResponsivenessTests("chromium", "mobile")
        };
    }
}
