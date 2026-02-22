package tr.kontas.erp.notification.application.inapp;

public interface GetInAppNotificationsByUserUseCase {
    java.util.List<InAppNotificationResult> execute(String userId);
}

