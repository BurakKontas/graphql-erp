package tr.kontas.erp.finance.platform.persistence.accountingperiod;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.accountingperiod.*;

public class AccountingPeriodMapper {
    public static AccountingPeriodJpaEntity toEntity(AccountingPeriod d) {
        AccountingPeriodJpaEntity e = new AccountingPeriodJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setPeriodType(d.getPeriodType().name());
        e.setStartDate(d.getStartDate());
        e.setEndDate(d.getEndDate());
        e.setStatus(d.getStatus().name());
        return e;
    }

    public static AccountingPeriod toDomain(AccountingPeriodJpaEntity e) {
        return new AccountingPeriod(
                AccountingPeriodId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                PeriodType.valueOf(e.getPeriodType()),
                e.getStartDate(),
                e.getEndDate(),
                PeriodStatus.valueOf(e.getStatus())
        );
    }
}

