package org.example.factory.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.factory.WebDriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverFactory extends AbstractWebDriverFactory<FirefoxDriver, FirefoxOptions> {

    @Override
    public void loadRemoteDriver() {
        WebDriverManager.firefoxdriver().setup();
    }

    @Override
    public WebDriver createWebDriver() {
        return new FirefoxDriver(super.getOptions(WebDriverType.FIREFOX));
    }
}
