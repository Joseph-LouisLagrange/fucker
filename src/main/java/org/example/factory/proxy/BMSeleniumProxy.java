package org.example.factory.proxy;

import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;

@Slf4j
public class BMSeleniumProxy implements SeleniumProxy {
    private final BrowserMobProxy browserMobProxy;
    public BMSeleniumProxy(BrowserMobProxy browserMobProxy){
        this.browserMobProxy = browserMobProxy;
    }

    @Override
    public Proxy getProxy() {
        return ClientUtil.createSeleniumProxy(browserMobProxy);
    }

    @Override
    public void close() {
        browserMobProxy.abort();
        log.info("BMSeleniumProxy 关闭");
    }

    @Override
    public void start() {
        browserMobProxy.start();
        log.info("BMSeleniumProxy 开启成功");
    }
}
