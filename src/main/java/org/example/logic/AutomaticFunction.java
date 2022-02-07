package org.example.logic;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.example.application.HealthyPunch;
import org.example.User;
import org.example.core.AssemblyLiner;
import org.example.core.AssemblyLinerBuilder;
import org.example.core.DefaultAssemblyLinerBuilder;
import org.example.listen.Spreader;
import org.example.packet.RequestPacket;
import org.example.packet.ResponsePacket;
import org.example.steal.ApplicationTicketStealer;
import org.example.steal.PersonLessonTicketProxyStealer;
import org.example.util.ClasspathUtils;
import org.example.util.CommonAction;
import org.example.util.StaticFactory;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 系统中所有已内置搭建完成的自动化功能
 */
public class AutomaticFunction {
    // 顶一下
    public static void goreAtHealthManagement(String username, String password) {
        goreAtHealthManagement(null, username, password);
    }

    // 顶一下
    public static void goreAtHealthManagement(String name, String username, String password) {
        if (name == null) {
            name = "Fucker";
        }
        goreAtHealthManagement(new User(username, password, name));
    }

    // 顶一下
    public static void goreAtHealthManagement(User user) {
        Spreader<RequestPacket> requestSpreader = new Spreader<>();
        Spreader<ResponsePacket> responseSpreader = new Spreader<>();
        BrowserMobProxyServer proxyServer = StaticFactory.getBrowserMobProxyServer(requestSpreader, responseSpreader);
        proxyServer.start();
        WebDriver webDriver = StaticFactory.getProxyWebDriver(ClientUtil.createSeleniumProxy(proxyServer));
        ApplicationTicketStealer ticketStealer = new PersonLessonTicketProxyStealer(webDriver);
        responseSpreader.addListener(ticketStealer);
        WebDriver newDriver = StaticFactory.getWebDriver();
        try {
            AssemblyLinerBuilder assemblyLinerBuilder = new DefaultAssemblyLinerBuilder();
            AtomicReference<String> stealTicket = new AtomicReference<>();
            AssemblyLiner assemblyLiner = assemblyLinerBuilder
                    .next(() -> CommonAction.login(webDriver, user.getUsername(), user.getPassword()))    // 登录
                    .drinkCoffee(3, TimeUnit.SECONDS)                                                // 坐下喝杯咖啡
                    .next(() -> stealTicket.set(ticketStealer.steal()))                                   // 偷取访问应用的票据
                    .drinkCoffee(2, TimeUnit.SECONDS)                                                // 再次喝咖啡
                    .next(() -> new HealthyPunch(newDriver).punch(user, stealTicket.get()))                        // 进行 Click 打卡
                    .build();
            assemblyLiner.run();
        } finally {
            newDriver.quit();
            webDriver.quit();
            proxyServer.abort();
        }
    }

    // 顶一下
    public static void serializedGoreAtHealthManagement(List<User> users) {
        Spreader<RequestPacket> requestSpreader = new Spreader<>();
        Spreader<ResponsePacket> responseSpreader = new Spreader<>();
        BrowserMobProxyServer proxyServer = StaticFactory.getBrowserMobProxyServer(requestSpreader, responseSpreader);
        proxyServer.start();
        WebDriver mainWebDriver = StaticFactory.getProxyWebDriver(ClientUtil.createSeleniumProxy(proxyServer));
        ApplicationTicketStealer ticketStealer = new PersonLessonTicketProxyStealer(mainWebDriver);
        responseSpreader.addListener(ticketStealer);
        WebDriver newDriver = StaticFactory.getWebDriver();
        try {
            for (User user : users) {
                AssemblyLinerBuilder assemblyLinerBuilder = new DefaultAssemblyLinerBuilder();
                AtomicReference<String> stealTicket = new AtomicReference<>();
                AssemblyLiner assemblyLiner = assemblyLinerBuilder
                        .next(() -> CommonAction.login(mainWebDriver, user.getUsername(), user.getPassword()))    // 登录
                        .drinkCoffee(3, TimeUnit.SECONDS)                                                // 坐下喝杯咖啡
                        .next(() -> stealTicket.set(ticketStealer.steal()))                                   // 偷取访问应用的票据
                        .drinkCoffee(2, TimeUnit.SECONDS)                                                // 再次喝咖啡
                        .next(() -> new HealthyPunch(newDriver).punch(user, stealTicket.get()))                        // 进行 Click 打卡
                        .build();
                    assemblyLiner.run();
            }
        } finally {
            newDriver.quit();
            mainWebDriver.quit();
            proxyServer.abort();
        }
    }

    /**
     * 默认读取 classpath 下的 users.json 文件
     * 顶一下
     */
    public static void defaultGoreAtHealthManagement() {
        Gson gson = new Gson();
        List<User> users = gson.fromJson(ClasspathUtils.getReader("users.json"), new TypeToken<List<User>>() {
        }.getType());
        serializedGoreAtHealthManagement(users);
    }
}
