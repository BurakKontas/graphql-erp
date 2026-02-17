package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.valueobjects.PermissionAction;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionModule;

import java.util.Set;

public record PermissionSnapshot(Set<String> permissions) {
    public boolean allows(PermissionModule module, PermissionAction action) {
        return permissions.contains(module.getValue() + ":" + action.getValue());
    }
}
