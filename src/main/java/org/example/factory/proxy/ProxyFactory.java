package org.example.factory.proxy;

import org.example.listen.Spreader;
import org.example.packet.PacketMemory;
import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;

public interface ProxyFactory {
    SeleniumProxy getProxy(Spreader<RequestPacket> requestPacketSpreader,
                           Spreader<ResponsePacket> responsePacketSpreader,
                           PacketMemory packetMemory);
}
