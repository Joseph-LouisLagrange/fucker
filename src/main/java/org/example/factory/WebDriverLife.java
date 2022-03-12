package org.example.factory;

import org.openqa.selenium.WebDriver;

public interface WebDriverLife {
    void quitBefore(WebDriver webDriver);

    void quitAfter(WebDriver webDriver);

    void startBefore();

    void startAfter(WebDriver webDriver);
}
