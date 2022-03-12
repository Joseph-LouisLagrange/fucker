package org.example.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.example.listen.Spreader;
import org.example.packet.PacketMemory;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v97.network.Network;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.http.HttpResponse;

import javax.activation.MimeTypeParseException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 静态工厂，所有的 WebDriver 都可以从这里加载获取
 */
@Deprecated
public class StaticFactory {
    static {
        loadRemoteDriver();
      }

    /**
     * 使用 System.Property 加载本地驱动与本地浏览器内核
     */
    private static void loadLocalProperty(){
         System.setProperty("webdriver.firefox.bin","C:/Program Files/Mozilla Firefox/firefox.exe");
         System.setProperty("webdriver.gecko.driver","E:/FDMDownload/geckodriver-v0.30.0-win64/geckodriver.exe");
    }

    /**
     * 加载远程驱动
     */
    public static void loadRemoteDriver(){
        WebDriverManager.firefoxdriver().setup();
    }

    /**
     * 获取 Selenium代理 WebDriver
     * @param proxy SeleniumProxy
     * @return WebDriver
     */
    public static WebDriver getProxyWebDriver(Proxy proxy){
        FirefoxOptions options = getDefaultFirefoxOptions();
        options.setProxy(proxy);
        return getFirefoxDriver(options);
    }

    /**
     * 获取已经被代理的 WebDriver (代理已开启)
     * @param requestSpreader 请求包的事件广播
     * @param responseSpreader 响应包的事件广播
     * @return WebDriver
     */
    public static WebDriver getProxyWebDriver(Spreader<RequestPacket> requestSpreader,
                                              Spreader<ResponsePacket> responseSpreader,
                                              PacketMemory packetMemory){
        BrowserMobProxyServer proxyServer = getBrowserMobProxyServer(requestSpreader, responseSpreader,packetMemory);
        proxyServer.start();
        return getProxyWebDriver(ClientUtil.createSeleniumProxy(proxyServer));
    }

    /**
     * 获取默认的一个简单 WebDriver
     * @return WebDriver
     */
    public static WebDriver getWebDriver(){
        //return getFirefoxDriver(getDefaultFirefoxOptions());
        return getChromeDriver(getDefaultChromeOptions());
    }

    /**
     * 获取 BMP 代理服务器
     * @param requestSpreader 请求包的事件广播
     * @param responseSpreader 响应包的事件广播
     * @return BrowserMobProxyServer
     */
    public static BrowserMobProxyServer getBrowserMobProxyServer(Spreader<RequestPacket> requestSpreader,
                                                                 Spreader<ResponsePacket> responseSpreader,
                                                                 PacketMemory packetMemory){
        BrowserMobProxyServer proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        ConcurrentMap<String,Long> tempMap = new ConcurrentHashMap<>();
        proxy.addRequestFilter((httpRequest, httpMessageContents, httpMessageInfo) -> {
            // 包装为 RequestPacket
            RequestPacket requestPacket = null;
            try {
                requestPacket = PacketUtil.warp(httpRequest, httpMessageContents, httpMessageInfo);
            } catch (MimeTypeParseException e) {
                e.printStackTrace();
            }
            // 缓存这个 RequestPacket
            packetMemory.save(requestPacket);
            // 临时存储
            assert requestPacket != null;
            tempMap.put(httpMessageInfo.getOriginalUrl(), requestPacket.getID());
            // 传播给各个Listener
            requestSpreader.spread(requestPacket);
            return null;
        });
        proxy.addResponseFilter((httpResponse, httpMessageContents, httpMessageInfo) -> {
            String URL = httpMessageInfo.getOriginalUrl();
            // 包装为 ResponsePacket
            ResponsePacket responsePacket = null;
            try {
                responsePacket = PacketUtil.warp(httpResponse, httpMessageContents, httpMessageInfo);
            } catch (MimeTypeParseException e) {
                e.printStackTrace();
            }
            // 缓存这个 ResponsePacket
            packetMemory.save(responsePacket);
            assert responsePacket != null;
            Long rspPID = responsePacket.getID();
            Long reqPID = tempMap.remove(URL);
            // 绑定 Request --- Response 的对相应关系
            packetMemory.bind(reqPID,rspPID);
            // 传播给各个Listener
            responseSpreader.spread(responsePacket);
            // 删除 Packet 缓存
            packetMemory.unbindAndClear(reqPID,rspPID);
        });
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS,CaptureType.REQUEST_CONTENT,CaptureType.RESPONSE_CONTENT,CaptureType.RESPONSE_HEADERS);
        return proxy;
    }

    /**
     * 获取监听 NetWork 的 WebDriver
     * @param requestSpreader 请求包的事件广播
     * @param responseSpreader 响应包的事件广播
     * @return 在 NetWork 已经安装了 Filter 的 WebDriver
     */
    public static WebDriver getWebDriverAndListeningNetWork(Spreader<RequestPacket> requestSpreader,
                                                            Spreader<ResponsePacket> responseSpreader,
                                                            PacketMemory packetMemory){
        FirefoxDriver driver = getFirefoxDriver(getDefaultFirefoxOptions());
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.of(100000000)));
        devTools.send(Network.setCacheDisabled(true));
        org.openqa.selenium.devtools.idealized.Network<?, ?> network = devTools.getDomains().network();
        network.prepareToInterceptTraffic();
        network.interceptTrafficWith(httpHandler -> req -> {
            RequestPacket requestPacket;
            ResponsePacket responsePacket;
            HttpResponse httpResponse = null;
            try {
                requestPacket = PacketUtil.warp(req);
                packetMemory.save(requestPacket);
                requestSpreader.spread(requestPacket);
                httpResponse = httpHandler.execute(req);
                responsePacket = PacketUtil.warp(httpResponse);
                packetMemory.save(responsePacket);
                packetMemory.bind(requestPacket.getID(), responsePacket.getID());
                responseSpreader.spread(responsePacket);
                packetMemory.unbindAndClear(requestPacket.getID(), responsePacket.getID());
            } catch (MimeTypeParseException e) {
                e.printStackTrace();
            }
            return httpResponse;
        });
        return driver;
    }

    public static FirefoxOptions getDefaultFirefoxOptions(){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setAcceptInsecureCerts(true);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setAcceptUntrustedCertificates(true);
        firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
        options.setProfile(firefoxProfile);
        return options;
    }

    public static ChromeOptions getDefaultChromeOptions(){
        ChromeOptions options = new ChromeOptions();
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.images",2);
        options.setHeadless(false)
                .setAcceptInsecureCerts(true)
                .addArguments("--start-maximized")
                //.addArguments("user-agent=\"MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1\"")
                .addArguments("--disable-blink-features=AutomationControlled")
                .addArguments("--disable-application-cache")
                .addArguments("--disable-gpu")
                .addArguments("--no-sandbox")
                .addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                .setExperimentalOption("useAutomationExtension",false)
                .setExperimentalOption("prefs",prefs)
                .setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"))
                .setImplicitWaitTimeout(Duration.ofSeconds(10))
                .setPageLoadTimeout(Duration.ofSeconds(10))
                .setScriptTimeout(Duration.ofSeconds(10));
        return options;
    }

    public static FirefoxDriver getFirefoxDriver(FirefoxOptions options){
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        return driver;
    }

    public static ChromeDriver getChromeDriver(ChromeOptions options){
        ChromeDriver driver = new ChromeDriver(options);
        Map<String,Object> parameters = new HashMap<>();
        String sourceValue  =   "    Object.defineProperty(navigator, 'webdriver', {" +
                                "          get: () => undefined" +
                                "    })";
        parameters.put("source",sourceValue);
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",parameters);
        return driver;
    }
}
