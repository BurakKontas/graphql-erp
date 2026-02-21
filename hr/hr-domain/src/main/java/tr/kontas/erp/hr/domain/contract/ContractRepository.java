package tr.kontas.erp.hr.domain.contract;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {
    void save(Contract entity);
    Optional<Contract> findById(ContractId id, TenantId tenantId);
    List<Contract> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Contract> findByIds(List<ContractId> ids);
}
