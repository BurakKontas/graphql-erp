package tr.kontas.erp.core.domain.identity;

import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionKey;

public record Permission(PermissionId id, PermissionKey key) {

    public String asKey() {
        return key.getModule().getValue() + ":" + key.getAction().getValue();
    }
}
