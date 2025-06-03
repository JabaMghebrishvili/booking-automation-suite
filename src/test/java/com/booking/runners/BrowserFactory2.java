package com.booking.runners;

import com.booking.tests.UIResponsivenessTests3;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;

public class BrowserFactory2 {
    @Factory
    @Parameters({"deviceType"})
    public Object[] createTests(String deviceType) {
        return new Object[] {
                new UIResponsivenessTests3("chromium", deviceType),
                // ორ ბრაუზერზე სამი სხვადასხვა ვიუპორტით
                // გაშვება ძალიან ამძიმებს ტესტს, ამიტომ ამ ეტაპზე chrome-ზე დავტოვებ
//                new UIResponsivenessTests3("webkit", deviceType),
        };
    }
}
