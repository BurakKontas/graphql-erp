package tr.kontas.erp.notification.domain;

public enum OutboxStatus {
    PENDING,
    SENDING,
    SENT,
    FAILED,
    CANCELLED
}

