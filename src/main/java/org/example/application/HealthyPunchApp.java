package org.example.application;

import org.example.User;
import org.example.core.AssemblyLiner;
import org.example.core.DefaultAssemblyLinerBuilder;
import org.example.core.step.EnterServiceCenter;
import org.example.core.step.LoginStep;
import org.example.core.step.OpenLesson;
import org.example.factory.SimpleWebDriverBuilder;
import org.example.factory.WebDriverType;
import org.example.factory.proxy.BrowseMobProxyFactory;
import org.example.listen.Spreader;
import org.example.packet.PacketMemoryImpl;
import org.example.packet.response.ResponsePacket;
import org.example.steal.HealthyPunchReceiver;
import org.example.steal.PersonLessonTicketStealer;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HealthyPunchApp {
    public AssemblyLiner build(WebDriver webDriver, User user) {
        return new DefaultAssemblyLinerBuilder()
                .next(new LoginStep(webDriver, user))
                .drinkCoffee(10, TimeUnit.SECONDS)
                .next(new EnterServiceCenter(webDriver))
                .drinkCoffee(10, TimeUnit.SECONDS)
                .next(new OpenLesson(webDriver))
                .drinkCoffee(10, TimeUnit.SECONDS)
                .build();
    }

    public void punch(User user) {
        BrowseMobProxyFactory proxyFactory = new BrowseMobProxyFactory();
        SimpleWebDriverBuilder builder = new SimpleWebDriverBuilder();
        Spreader<ResponsePacket> packetSpreader = new Spreader<>();
        PacketMemoryImpl packetMemory = new PacketMemoryImpl();
        WebDriver webDriver = builder
                .proxy(
                        proxyFactory.getProxy(new Spreader<>(), packetSpreader, packetMemory)
                )
                .build(WebDriverType.FIREFOX);
        packetSpreader.addListener(new PersonLessonTicketStealer(packetMemory, new HealthyPunchReceiver()));
        try {
            build(webDriver, user).run();
        } finally {
            webDriver.quit();
        }
    }

    public void punch(List<User> users) {
        BrowseMobProxyFactory proxyFactory = new BrowseMobProxyFactory();
        SimpleWebDriverBuilder builder = new SimpleWebDriverBuilder();
        Spreader<ResponsePacket> packetSpreader = new Spreader<>();
        PacketMemoryImpl packetMemory = new PacketMemoryImpl();
        WebDriver webDriver = builder
                .proxy(
                        proxyFactory.getProxy(new Spreader<>(), packetSpreader, packetMemory)
                )
                .build(WebDriverType.FIREFOX);
        HealthyPunchReceiver healthyPunchReceiver = new HealthyPunchReceiver();
        packetSpreader.addListener(new PersonLessonTicketStealer(packetMemory, healthyPunchReceiver));
        try {
            for (User user : users) {
                healthyPunchReceiver.setUser(user);
                build(webDriver, user).run();
            }
        } finally {
            webDriver.quit();
        }
    }
}
