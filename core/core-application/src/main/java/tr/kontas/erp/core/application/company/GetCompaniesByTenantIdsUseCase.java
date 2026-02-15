package tr.kontas.erp.core.application.company;

import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Map;

public interface GetCompaniesByTenantIdsUseCase {
    Map<TenantId, List<Company>> executeByTenantIds(List<TenantId> ids);
}
