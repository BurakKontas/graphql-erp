package tr.kontas.erp.finance.domain.account;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findById(AccountId id, TenantId tenantId);
    Optional<Account> findByCode(TenantId tenantId, CompanyId companyId, String code);
    List<Account> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Account> findByIds(List<AccountId> ids);
    boolean existsByCode(TenantId tenantId, CompanyId companyId, String code);
}

