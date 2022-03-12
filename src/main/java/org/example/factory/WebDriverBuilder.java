package org.example.factory;

import org.example.factory.proxy.SeleniumProxy;
import org.example.listen.Spreader;
import org.example.packet.PacketMemory;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;
import org.openqa.selenium.WebDriver;

public interface WebDriverBuilder {
    WebDriver build(WebDriverType webDriverType);
    WebDriverBuilder proxy(SeleniumProxy seleniumProxy);
    WebDriverBuilder netWork(Spreader<RequestPacket> requestPacketSpreader,
                             Spreader<ResponsePacket> responsePacketSpreader,
                             PacketMemory packetMemory);
    WebDriverBuilder ajaxHook(Spreader<RequestPacket> requestPacketSpreader,
                              Spreader<ResponsePacket> responsePacketSpreader,
                              PacketMemory packetMemory);
}
