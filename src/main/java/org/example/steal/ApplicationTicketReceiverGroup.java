package org.example.steal;

import com.google.common.collect.Lists;

import java.util.List;

public class ApplicationTicketReceiverGroup implements ApplicationTicketReceiver {
    private List<ApplicationTicketReceiver> applicationTicketReceivers;
    public ApplicationTicketReceiverGroup(ApplicationTicketReceiver...applicationTicketReceivers){
        this.applicationTicketReceivers = Lists.newArrayList(applicationTicketReceivers);
    }
    @Override
    public void receive(String ticket) {
        applicationTicketReceivers.forEach(receiver->receiver.receive(ticket));
    }
}
