package tr.kontas.erp.core.application.tenant;

import tr.kontas.erp.core.domain.tenant.Tenant;

import java.util.List;

public interface GetTenantsUseCase {
    List<Tenant> execute();
}

