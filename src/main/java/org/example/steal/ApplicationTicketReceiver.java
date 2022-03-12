package org.example.steal;

@FunctionalInterface
public interface ApplicationTicketReceiver {
    void receive(String ticket);
}
