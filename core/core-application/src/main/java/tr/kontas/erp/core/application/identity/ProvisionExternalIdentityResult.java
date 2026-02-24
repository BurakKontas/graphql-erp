package tr.kontas.erp.core.application.identity;

import lombok.Getter;

@Getter
public class ProvisionExternalIdentityResult {
    private final String userId;
    private final String username;
    private final String provider;
    private final String externalId;

    public ProvisionExternalIdentityResult(String userId, String username, String provider, String externalId) {
        this.userId = userId;
        this.username = username;
        this.provider = provider;
        this.externalId = externalId;
    }

}
