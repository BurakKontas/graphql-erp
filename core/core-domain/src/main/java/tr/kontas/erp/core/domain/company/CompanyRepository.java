package tr.kontas.erp.core.domain.company;

import tr.kontas.erp.core.kernel.domain.repository.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CompanyRepository extends Repository<Company, CompanyId> {
    Optional<Company> findById(CompanyId id);
    List<Company> findAll();
    void save(Company company);
    List<Company> findByTenant(TenantId tenantId);
    List<Company> findByCompanyIds(List<CompanyId> ids);
    Set<Company> findByTenantIds(List<TenantId> ids);
}