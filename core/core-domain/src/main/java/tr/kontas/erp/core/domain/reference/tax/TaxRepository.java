package tr.kontas.erp.core.domain.reference.tax;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface TaxRepository {
    Optional<Tax> findByCode(TenantId tenantId, CompanyId companyId, TaxCode code);

    List<Tax> findByCompany(TenantId tenantId, CompanyId companyId);

    List<Tax> findAllActiveByCompany(TenantId tenantId, CompanyId companyId);

    void save(Tax tax);
}