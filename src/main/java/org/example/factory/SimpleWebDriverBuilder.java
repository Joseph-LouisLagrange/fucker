package org.example.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.example.factory.driver.ChromeDriverFactory;
import org.example.factory.driver.EdgeDriverFactory;
import org.example.factory.driver.FirefoxDriverFactory;
import org.example.factory.driver.WebDriverFactory;
import org.example.factory.options.DefaultWebDriverOptionsFactory;
import org.example.factory.proxy.SeleniumProxy;
import org.example.listen.Spreader;
import org.example.packet.PacketMemory;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;
import org.example.util.PacketUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v97.network.Network;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.http.HttpResponse;

import javax.activation.MimeTypeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SimpleWebDriverBuilder implements WebDriverBuilder {
    private Map<WebDriverType, WebDriverFactory<? extends WebDriver,? extends AbstractDriverOptions<?>>> webDriverFactoryMap = new HashMap<>();

    private Proxy proxy ;

    private NetWork netWork;

    private AjaxHook ajaxHook;


    private static class Proxy{
        SeleniumProxy seleniumProxy;
    }

    private static class NetWork{
        Spreader<RequestPacket> requestPacketSpreader;
        Spreader<ResponsePacket> responsePacketSpreader;
        PacketMemory packetMemory;
    }

    private static class AjaxHook{
        Spreader<RequestPacket> requestPacketSpreader;
        Spreader<ResponsePacket> responsePacketSpreader;
        PacketMemory packetMemory;
    }

    {
        webDriverFactoryMap.put(WebDriverType.CHROME, new ChromeDriverFactory());
        webDriverFactoryMap.put(WebDriverType.FIREFOX, new FirefoxDriverFactory());
        webDriverFactoryMap.put(WebDriverType.EDGE,new EdgeDriverFactory());
    }

    protected WebDriverLife getWebDriverLifeForProxy(SeleniumProxy proxy){
        return new WebDriverLife() {
            @Override
            public void quitBefore(WebDriver webDriver) {
                // no do
            }

            @Override
            public void quitAfter(WebDriver webDriver) {
                proxy.close();
            }

            @Override
            public void startBefore() {
                proxy.start();
            }

            @Override
            public void startAfter(WebDriver webDriver) {
                webDriver.manage().window().maximize();
            }
        };
    }

    private Optional<DevTools> getDevTools(WebDriver webDriver){
        if (ClassUtils.isAssignable(webDriver.getClass(), HasDevTools.class)){
           return  ((HasDevTools)webDriver).maybeGetDevTools();
        }
        return Optional.empty();
    }

    private void addNetWork(WebDriver webDriver){
        DevTools devTools = getDevTools(webDriver)
                .orElseThrow(() -> new DevToolsException("当前不能创建 DevTool 连接，当前浏览器可能不支持"));
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.of(100000000)));
        devTools.send(Network.setCacheDisabled(true));
        org.openqa.selenium.devtools.idealized.Network<?, ?> network = devTools.getDomains().network();
        network.interceptTrafficWith(httpHandler -> req -> {
            RequestPacket requestPacket;
            ResponsePacket responsePacket;
            HttpResponse httpResponse = null;
            try {
                requestPacket = PacketUtil.warp(req);
                netWork.packetMemory.save(requestPacket);
                netWork.requestPacketSpreader.spread(requestPacket);
                httpResponse = httpHandler.execute(req);
                responsePacket = PacketUtil.warp(httpResponse);
                netWork.packetMemory.save(responsePacket);
                netWork.packetMemory.bind(requestPacket.getID(), responsePacket.getID());
                netWork.responsePacketSpreader.spread(responsePacket);
                netWork.packetMemory.unbindAndClear(requestPacket.getID(), responsePacket.getID());
            } catch (MimeTypeParseException e) {
                e.printStackTrace();
            }
            return httpResponse;
        });
    }

    private void addProxy(WebDriverFactory<? extends WebDriver, ? extends AbstractDriverOptions<?>> webDriverFactory){
        webDriverFactory.setWebDriverOptionsFactory(new DefaultWebDriverOptionsFactory(){
            @Override
            public <O extends AbstractDriverOptions<?>> void every(O options) {
                super.every(options);
                options.setProxy(proxy.seleniumProxy.getProxy());
            }
        });
        // 注入生命周期
        webDriverFactory.setWebDriverLife(getWebDriverLifeForProxy(proxy.seleniumProxy));
    }

    @Override
    public WebDriver build(WebDriverType webDriverType) {
        WebDriverFactory<? extends WebDriver, ? extends AbstractDriverOptions<?>> webDriverFactory = webDriverFactoryMap.get(webDriverType);
        if (webDriverFactory == null){
            throw new IllegalArgumentException(String.format("%s 尚未支持",webDriverType));
        }
        if (proxy!=null){
            addProxy(webDriverFactory);
        }
        WebDriver webDriver = webDriverFactory.getWebDriver();
        if (netWork!=null){
            addNetWork(webDriver);
        }
        if (ajaxHook!=null){
            // 暂未实现，敬请期待
        }
        return webDriver;
    }

    @Override
    public WebDriverBuilder proxy(SeleniumProxy seleniumProxy) {
        if (proxy==null){
            proxy = new Proxy();
        }
        proxy.seleniumProxy = seleniumProxy;
        return this;
    }

    @Override
    public WebDriverBuilder netWork(Spreader<RequestPacket> requestPacketSpreader,
                                    Spreader<ResponsePacket> responsePacketSpreader,
                                    PacketMemory packetMemory) {
        if (netWork==null){
            netWork = new NetWork();
        }
        netWork.packetMemory =packetMemory;
        netWork.requestPacketSpreader = requestPacketSpreader;
        netWork.responsePacketSpreader = responsePacketSpreader;
        return this;
    }

    @Override
    public WebDriverBuilder ajaxHook(Spreader<RequestPacket> requestPacketSpreader,
                                     Spreader<ResponsePacket> responsePacketSpreader,
                                     PacketMemory packetMemory) {
        if (ajaxHook==null){
            ajaxHook = new AjaxHook();
        }
        log.warn("当前的 ajaxHook 还未做出实现");
        return this;
    }
}
