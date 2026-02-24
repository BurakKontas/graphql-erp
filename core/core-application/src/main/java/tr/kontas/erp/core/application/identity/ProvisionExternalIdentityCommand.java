package tr.kontas.erp.core.application.identity;

public record ProvisionExternalIdentityCommand(
        String tenantId,
        String username,
        String provider,
        String externalId
) {
}
