package tr.kontas.erp.core.domain.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OidcSettings extends ValueObject {
    private String issuer;
    private String audience;
    private String jwkSetUri;
    @Builder.Default
    private long clockSkewSeconds = 60;

    public boolean isConfigured() {
        return issuer != null && !issuer.isBlank()
                && jwkSetUri != null && !jwkSetUri.isBlank();
    }
}
