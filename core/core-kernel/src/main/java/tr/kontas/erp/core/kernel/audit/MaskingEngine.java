package tr.kontas.erp.core.kernel.audit;

public class MaskingEngine {

    public static String mask(String value, MaskLevel level) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return switch (level) {
            case NONE -> value;
            case FULL -> "[REDACTED]";
            case PARTIAL -> applyPartialMask(value);
        };
    }

    private static String applyPartialMask(String value) {
        if (value.contains("@")) {
            return maskEmail(value);
        }
        if (value.length() > 4) {
            return value.substring(0, 2) + "*".repeat(value.length() - 4) + value.substring(value.length() - 2);
        }
        return "*".repeat(value.length());
    }

    private static String maskEmail(String email) {
        int atIdx = email.indexOf('@');
        if (atIdx <= 0) {
            return email;
        }
        String local = email.substring(0, atIdx);
        String domain = email.substring(atIdx);
        if (local.length() <= 1) {
            return local + "***" + domain;
        }
        return local.charAt(0) + "***" + domain;
    }
}

