package org.example.listen;

import org.example.packet.Packet;

public interface Listener<T extends Packet> {
    void accept(T packet);
}
