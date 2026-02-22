package tr.kontas.erp.notification.platform.dispatcher;

import tr.kontas.erp.notification.domain.NotificationChannel;

public interface ChannelDispatcher {
    NotificationChannel getChannel();
    void dispatch(String recipientAddress, String subject, String body);
}

