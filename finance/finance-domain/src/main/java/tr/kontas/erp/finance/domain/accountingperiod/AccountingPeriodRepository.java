package tr.kontas.erp.finance.domain.accountingperiod;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountingPeriodRepository {
    void save(AccountingPeriod period);
    Optional<AccountingPeriod> findById(AccountingPeriodId id, TenantId tenantId);
    List<AccountingPeriod> findByCompanyId(TenantId tenantId, CompanyId companyId);
    Optional<AccountingPeriod> findByDate(TenantId tenantId, CompanyId companyId, LocalDate date);
    List<AccountingPeriod> findByIds(List<AccountingPeriodId> ids);
}

