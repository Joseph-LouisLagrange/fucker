package org.example.factory.options;

import org.example.factory.WebDriverType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.AbstractDriverOptions;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultWebDriverOptionsFactory implements WebDriverOptionsFactory {
    @Override
    public <O extends AbstractDriverOptions<?>> O getOptions(Class<O> cls) {
        Object options = null;
        if (ChromeOptions.class.equals(cls)) {
            options = getChromeOptions();
        }else if (FirefoxOptions.class.equals(cls)){
            options = getFirefoxOptions();
        }else if(EdgeOptions.class.equals(cls)){
            options = getEdgeOptions();
        }else{
            throw new IllegalArgumentException(String.format("Class %s 不是合法的 Options",cls));
        }
        commonsOptions((O)options);
        return (O) options;
    }

    @Override
    public <O extends AbstractDriverOptions<?>> O getOptions(WebDriverType webDriverType) {
        return (O) getOptions(webDriverType.getOptionsCls());
    }

    @Override
    public <O extends AbstractDriverOptions<?>> void every(O options) {}

    protected <O extends AbstractDriverOptions<?>> void commonsOptions(O options){
        options
                .setImplicitWaitTimeout(Duration.ofSeconds(100))
                .setPageLoadTimeout(Duration.ofSeconds(100))
                .setScriptTimeout(Duration.ofSeconds(100))
                .setAcceptInsecureCerts(true);
        every(options);
    }

    public ChromeOptions getChromeOptions(){
        ChromeOptions chromeOptions = new ChromeOptions();
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.images",2);
        chromeOptions
                .addArguments("--start-maximized")
                //.addArguments("user-agent=\"MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1\"")
                .addArguments("--disable-blink-features=AutomationControlled")
                .addArguments("--disable-application-cache")
                .addArguments("--disable-gpu")
                .addArguments("--no-sandbox")
                .addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                .setExperimentalOption("useAutomationExtension",false)
                .setExperimentalOption("prefs",prefs)
                .setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        return chromeOptions;
    }

    public FirefoxOptions getFirefoxOptions(){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        // options.setPageLoadStrategy(PageLoadStrategy.NONE);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setAcceptUntrustedCertificates(true);
        firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
        options.setProfile(firefoxProfile);
        return options;
    }

    public EdgeOptions getEdgeOptions(){
        EdgeOptions edgeOptions = new EdgeOptions();
        return edgeOptions;
    }
}
