package org.example.factory.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.factory.WebDriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class ChromeDriverFactory extends AbstractWebDriverFactory<ChromeDriver,ChromeOptions> {

    @Override
    public void loadRemoteDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @Override
    public WebDriver createWebDriver() {
        ChromeDriver chromeDriver = new ChromeDriver(super.getOptions(WebDriverType.CHROME));
        Map<String,Object> parameters = new HashMap<>();
        String sourceValue  =   "    Object.defineProperty(navigator, 'webdriver', {" +
                "          get: () => undefined" +
                "    })";
        parameters.put("source",sourceValue);
        chromeDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",parameters);
        return chromeDriver;
    }
}
