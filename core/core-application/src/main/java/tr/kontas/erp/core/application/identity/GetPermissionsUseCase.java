package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.Permission;

import java.util.List;

public interface GetPermissionsUseCase {
    List<Permission> execute();
}
