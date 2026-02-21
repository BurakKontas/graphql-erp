package tr.kontas.erp.app.audit.dtos;

public record FieldChangePayload(
        String fieldName,
        String oldValue,
        String newValue,
        String maskLevel
) {}

