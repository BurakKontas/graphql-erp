package tr.kontas.erp.finance.platform.persistence.accountingperiod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.accountingperiod.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountingPeriodRepositoryImpl implements AccountingPeriodRepository {

    private final JpaAccountingPeriodRepository jpa;

    @Override
    public void save(AccountingPeriod period) {
        jpa.save(AccountingPeriodMapper.toEntity(period));
    }

    @Override
    public Optional<AccountingPeriod> findById(AccountingPeriodId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(AccountingPeriodMapper::toDomain);
    }

    @Override
    public List<AccountingPeriod> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(AccountingPeriodMapper::toDomain).toList();
    }

    @Override
    public Optional<AccountingPeriod> findByDate(TenantId tenantId, CompanyId companyId, LocalDate date) {
        return jpa.findByDate(tenantId.asUUID(), companyId.asUUID(), date)
                .map(AccountingPeriodMapper::toDomain);
    }

    @Override
    public List<AccountingPeriod> findByIds(List<AccountingPeriodId> ids) {
        return jpa.findByIdIn(ids.stream().map(AccountingPeriodId::asUUID).toList())
                .stream().map(AccountingPeriodMapper::toDomain).toList();
    }
}
