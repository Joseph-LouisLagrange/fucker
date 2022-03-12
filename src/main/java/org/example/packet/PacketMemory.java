package org.example.packet;

import org.example.packet.request.RequestPacket;
import org.example.packet.response.ResponsePacket;

import java.util.Optional;

public interface PacketMemory {
    void save(RequestPacket requestPacket);
    void save(ResponsePacket responsePacket);
    void bind(Long requestPID,Long responsePID);
    Optional<ResponsePacket> lookupResponsePacket(Long PID);
    Optional<RequestPacket> lookupRequestPacket(Long PID);
    void unbindAndClear(Long requestPID,Long responsePID);
    RequestPacket getRequestPacket(Long PID);
}
