package tr.kontas.erp.core.platform.persistence.identity.permission;

import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionAction;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionKey;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionModule;

import java.util.UUID;

public final class PermissionMapper {

    public static Permission toDomain(PermissionJpaEntity entity) {
        return new Permission(
                PermissionId.of(entity.getId()),
                new PermissionKey(
                        new PermissionModule(entity.getModule()),
                        new PermissionAction(entity.getAction())
                )
        );
    }

    public static PermissionJpaEntity toEntity(Permission domain) {
        PermissionJpaEntity entity = new PermissionJpaEntity();
        entity.setId((UUID) domain.id().getValue());
        entity.setModule(domain.key().getModule().getValue());
        entity.setAction(domain.key().getAction().getValue());
        return entity;
    }
}