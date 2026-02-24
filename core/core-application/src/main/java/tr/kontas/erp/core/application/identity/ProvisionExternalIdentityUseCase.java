package tr.kontas.erp.core.application.identity;

public interface ProvisionExternalIdentityUseCase {
    ProvisionExternalIdentityResult execute(ProvisionExternalIdentityCommand command);
}
