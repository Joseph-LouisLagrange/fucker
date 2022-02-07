package org.example.steal;

import org.example.listen.Listener;
import org.example.packet.ResponsePacket;

public interface ApplicationTicketStealer extends Listener<ResponsePacket> {
    String steal();
}
