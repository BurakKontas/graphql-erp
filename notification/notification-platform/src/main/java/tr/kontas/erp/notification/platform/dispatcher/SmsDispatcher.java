package tr.kontas.erp.notification.platform.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.notification.domain.NotificationChannel;

@Slf4j
@Component
public class SmsDispatcher implements ChannelDispatcher {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public void dispatch(String recipientAddress, String subject, String body) {
        log.info("[SMS] To: {} | Body: {}", recipientAddress, body);
    }
}

