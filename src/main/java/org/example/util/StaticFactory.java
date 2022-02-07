package org.example.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;
import org.example.packet.RequestPacket;
import org.example.packet.ResponsePacket;
import org.example.listen.Spreader;
import org.example.packet.BMPRequestPacket;
import org.example.packet.BMPResponsePacket;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

public class StaticFactory {
    static {
        WebDriverManager.firefoxdriver().setup();
    }


    public static WebDriver getProxyWebDriver(Proxy proxy){
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless(true);
        options.setProxy(proxy);
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5,TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(6,TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getWebDriver(){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setAcceptInsecureCerts(true);
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    public static BrowserMobProxyServer getBrowserMobProxyServer(Spreader<RequestPacket> requestSpreader, Spreader<ResponsePacket> responseSpreader){
        BrowserMobProxyServer proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.addRequestFilter((httpRequest, httpMessageContents, httpMessageInfo) -> {
            requestSpreader.spread(new BMPRequestPacket(httpRequest, httpMessageContents, httpMessageInfo));
            return null;
        });
        proxy.addResponseFilter((httpResponse, httpMessageContents, httpMessageInfo) -> {
            responseSpreader.spread(new BMPResponsePacket(httpResponse,httpMessageContents,httpMessageInfo));
        });
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS,CaptureType.REQUEST_CONTENT,CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_HEADERS);
        return proxy;
    }
}
