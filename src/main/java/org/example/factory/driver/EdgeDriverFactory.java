package org.example.factory.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.factory.WebDriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverFactory extends AbstractWebDriverFactory<EdgeDriver, EdgeOptions> {
    @Override
    public void loadRemoteDriver() {
        WebDriverManager.edgedriver().setup();
    }

    @Override
    public WebDriver createWebDriver() {
        EdgeDriver edgeDriver = new EdgeDriver(getOptions(WebDriverType.EDGE));
        return edgeDriver;
    }
}
