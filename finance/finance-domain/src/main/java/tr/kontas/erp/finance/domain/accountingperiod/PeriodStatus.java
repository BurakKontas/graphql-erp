package tr.kontas.erp.finance.domain.accountingperiod;

public enum PeriodStatus {
    OPEN,
    SOFT_CLOSED,
    HARD_CLOSED;

    public boolean canPost(String requesterRole) {
        return switch (this) {
            case OPEN -> true;
            case SOFT_CLOSED -> "FINANCE:ADMIN".equals(requesterRole) || "GENERAL:ADMIN".equals(requesterRole);
            case HARD_CLOSED -> "GENERAL:ADMIN".equals(requesterRole);
        };
    }
}

