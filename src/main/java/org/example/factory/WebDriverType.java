package org.example.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public enum WebDriverType {
    FIREFOX(FirefoxDriver.class, FirefoxOptions.class),
    CHROME(ChromeDriver.class, ChromeOptions.class),
    EDGE(EdgeDriver.class, EdgeOptions.class);
    private final Class<? extends WebDriver> webDriverCls;
    private final Class<? extends AbstractDriverOptions<?>> optionsCls;
    WebDriverType(Class<? extends WebDriver> webDriverCls, Class<? extends AbstractDriverOptions<?>> optionsCls){
        this.webDriverCls = webDriverCls;
        this.optionsCls = optionsCls;
    }

    public Class<? extends WebDriver> getWebDriverCls() {
        return webDriverCls;
    }

    public Class<? extends AbstractDriverOptions<?>> getOptionsCls() {
        return optionsCls;
    }
}
