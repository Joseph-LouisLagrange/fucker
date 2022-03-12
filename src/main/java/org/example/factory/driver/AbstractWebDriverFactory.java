package org.example.factory.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.example.factory.options.DefaultWebDriverOptionsFactory;
import org.example.factory.WebDriverLife;
import org.example.factory.options.WebDriverOptionsFactory;
import org.example.factory.WebDriverType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.AbstractDriverOptions;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractWebDriverFactory<W extends WebDriver,O extends AbstractDriverOptions<?>> implements WebDriverFactory<W,O> {


    protected WebDriverOptionsFactory webDriverOptionsFactory;

    protected WebDriverLife webDriverLife;

    public abstract void loadRemoteDriver();

    public AbstractWebDriverFactory(WebDriverOptionsFactory webDriverOptionsFactory,WebDriverLife webDriverLife){
        this.webDriverOptionsFactory = webDriverOptionsFactory;
        this.webDriverLife = webDriverLife;
    }

    public AbstractWebDriverFactory(WebDriverLife webDriverLife){
        this.webDriverOptionsFactory = new DefaultWebDriverOptionsFactory();
        this.webDriverLife = webDriverLife;
    }

    public AbstractWebDriverFactory(){
        this(null);
    }

    public abstract WebDriver createWebDriver();

    @Override
    public WebDriver getWebDriver() {
        loadRemoteDriver();
        if (this.webDriverLife == null){
            // 没有生命周期
            return createWebDriver();
        }
        return new WebDriverWarp(this::createWebDriver,this.webDriverLife).createWebDriver();
    }

    protected O getOptions(WebDriverType webDriverType){
        return webDriverOptionsFactory.getOptions(webDriverType);
    }

    public void setWebDriverOptionsFactory(WebDriverOptionsFactory webDriverOptionsFactory) {
        this.webDriverOptionsFactory = webDriverOptionsFactory;
    }

    public WebDriverOptionsFactory getWebDriverOptionsFactory() {
        return webDriverOptionsFactory;
    }

    public WebDriverLife getWebDriverLife() {
        return webDriverLife;
    }

    public void setWebDriverLife(WebDriverLife webDriverLife) {
        this.webDriverLife = webDriverLife;
    }

    @Slf4j
    static class WebDriverWarp implements WebDriver, WebDriverLife{

        private final Supplier<WebDriver> webDriverSupplier;

        private final WebDriverLife webDriverLife;

        private WebDriver webDriver;

        public WebDriverWarp(Supplier<WebDriver> webDriverSupplier, WebDriverLife webDriverLife){
            this.webDriverSupplier = webDriverSupplier;
            this.webDriverLife = webDriverLife;
        }

        public WebDriver createWebDriver(){
            this.startBefore();
            this.webDriver = webDriverSupplier.get();
            log.info("WebDriver {} 启动成功",webDriver);
            this.startAfter(this);
            return this.webDriver;
        }

        @Override
        public void get(String url) {
            webDriver.get(url);
        }

        @Override
        public String getCurrentUrl() {
            return webDriver.getCurrentUrl();
        }

        @Override
        public String getTitle() {
            return webDriver.getTitle();
        }

        @Override
        public List<WebElement> findElements(By by) {
            return webDriver.findElements(by);
        }

        @Override
        public WebElement findElement(By by) {
            return webDriver.findElement(by);
        }

        @Override
        public String getPageSource() {
            return webDriver.getPageSource();
        }

        @Override
        public void close() {
            webDriver.close();
        }

        @Override
        public void quit() {
            webDriverLife.quitBefore(this);
            webDriver.quit();
            log.info("WebDriver {} 退出",webDriver);
            webDriverLife.quitAfter(this);
        }

        @Override
        public Set<String> getWindowHandles() {
            return webDriver.getWindowHandles();
        }

        @Override
        public String getWindowHandle() {
            return webDriver.getWindowHandle();
        }

        @Override
        public TargetLocator switchTo() {
            return webDriver.switchTo();
        }

        @Override
        public Navigation navigate() {
            return webDriver.navigate();
        }

        @Override
        public Options manage() {
            return webDriver.manage();
        }

        @Override
        public void quitBefore(WebDriver webDriver) {
            webDriverLife.quitBefore(webDriver);
        }

        @Override
        public void quitAfter(WebDriver webDriver) {
            webDriverLife.quitAfter(webDriver);
        }

        @Override
        public void startBefore() {
            webDriverLife.startBefore();
        }

        @Override
        public void startAfter(WebDriver webDriver) {
            webDriverLife.startAfter(webDriver);
        }
    }
}
