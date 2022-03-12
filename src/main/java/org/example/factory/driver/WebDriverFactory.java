package org.example.factory.driver;

import org.example.factory.WebDriverLife;
import org.example.factory.options.WebDriverOptionsFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;

public interface WebDriverFactory<W extends WebDriver,O extends AbstractDriverOptions<?>> {
    /**
     * 获取无监听的普通 WebDriver
     * @return WebDriver
     */
    WebDriver getWebDriver();

    WebDriverOptionsFactory getWebDriverOptionsFactory();

    void setWebDriverOptionsFactory(WebDriverOptionsFactory webDriverOptionsFactory);

    WebDriverLife getWebDriverLife() ;

    void setWebDriverLife(WebDriverLife webDriverLife);
}
