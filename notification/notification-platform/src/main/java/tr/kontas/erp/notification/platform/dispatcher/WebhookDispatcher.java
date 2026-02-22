package tr.kontas.erp.notification.platform.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.notification.domain.NotificationChannel;

@Slf4j
@Component
public class WebhookDispatcher implements ChannelDispatcher {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.WEBHOOK;
    }

    @Override
    public void dispatch(String targetUrl, String subject, String body) {
        log.info("[WEBHOOK] URL: {} | Payload: {}", targetUrl, body);
    }
}

