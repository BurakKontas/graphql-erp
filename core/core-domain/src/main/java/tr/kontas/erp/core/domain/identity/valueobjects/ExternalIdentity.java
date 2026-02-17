package tr.kontas.erp.core.domain.identity.valueobjects;

import lombok.Getter;
import tr.kontas.erp.core.domain.identity.enums.AuthProviderType;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class ExternalIdentity extends ValueObject {

    private final AuthProviderType provider;
    private final String externalId;

    public ExternalIdentity(AuthProviderType provider, String externalId) {
        if (provider == null || externalId == null || externalId.isBlank()) {
            throw new IllegalArgumentException("External identity invalid");
        }
        this.provider = provider;
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        return provider.name() + ":" + externalId;
    }
}
