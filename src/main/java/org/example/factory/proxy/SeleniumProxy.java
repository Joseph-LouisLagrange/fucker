package org.example.factory.proxy;

import org.openqa.selenium.Proxy;

public interface SeleniumProxy{
    Proxy getProxy();
    void close();
    void start();
}
