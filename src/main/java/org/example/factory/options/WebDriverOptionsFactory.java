package org.example.factory.options;

import org.example.factory.WebDriverType;
import org.openqa.selenium.remote.AbstractDriverOptions;

public interface WebDriverOptionsFactory {
    <O extends AbstractDriverOptions<?>> O getOptions(Class<O> cls);
    <O extends AbstractDriverOptions<?>> O getOptions(WebDriverType webDriverType);
    <O extends AbstractDriverOptions<?>> void every(O options);
}
