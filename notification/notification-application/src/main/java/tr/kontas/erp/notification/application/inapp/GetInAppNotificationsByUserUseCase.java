package tr.kontas.erp.notification.application.inapp;

import java.util.List;

public interface GetInAppNotificationsByUserUseCase {
    List<InAppNotificationResult> executeGet(String userId);
}

