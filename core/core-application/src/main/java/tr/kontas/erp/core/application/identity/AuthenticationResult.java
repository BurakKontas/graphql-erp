package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;

import java.util.Set;
import java.util.UUID;

public record AuthenticationResult(
        UUID userId,
        UserName username,
        ExternalIdentity externalIdentity,
        Set<String> permissions
) {
}