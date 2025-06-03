package com.booking.runners;

import com.booking.tests.UIResponsivenessTests3;
import org.testng.annotations.Factory;

public class DeviceTestFactory {
    @Factory
    public Object[] createInstances() {
        return new Object[] {
                new UIResponsivenessTests3("chromium", "desktop"),
                new UIResponsivenessTests3("chromium", "tablet"),
                new UIResponsivenessTests3("chromium", "mobile")
        };
    }
}
