package tr.kontas.erp.notification.platform.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.notification.domain.NotificationChannel;

@Slf4j
@Component
public class PushDispatcher implements ChannelDispatcher {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.PUSH;
    }

    @Override
    public void dispatch(String recipientAddress, String subject, String body) {
        log.info("[PUSH] To: {} | Title: {} | Body: {}", recipientAddress, subject, body);
    }
}

