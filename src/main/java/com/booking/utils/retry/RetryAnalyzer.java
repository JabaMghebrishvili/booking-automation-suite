package com.booking.utils.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        RetryCount retryCountAnnotation = iTestResult.getMethod().getConstructorOrMethod().getMethod()
                .getAnnotation(RetryCount.class);

        if (count < retryCountAnnotation.count()){
            count++;
            return true;
        }
        return false;
    }
}
