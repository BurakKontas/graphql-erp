package tr.kontas.erp.core.application.identity;

public record RegisterCommand(
        String tenantId,
        String username,
        String password
) {
}
