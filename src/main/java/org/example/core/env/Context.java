package org.example.core.env;

import org.openqa.selenium.WebDriver;

/**
 * WebDriver 的运行上下文
 */
public interface Context {
    WebDriver getWebDriver();
}
