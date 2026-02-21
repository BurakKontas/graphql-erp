package tr.kontas.erp.finance.domain.accountingperiod;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.exception.InvalidPeriodStateException;

import java.time.LocalDate;

@Getter
public class AccountingPeriod extends AggregateRoot<AccountingPeriodId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final PeriodType periodType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private PeriodStatus status;

    public AccountingPeriod(AccountingPeriodId id,
                            TenantId tenantId,
                            CompanyId companyId,
                            PeriodType periodType,
                            LocalDate startDate,
                            LocalDate endDate,
                            PeriodStatus status) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (periodType == null) throw new IllegalArgumentException("periodType cannot be null");
        if (startDate == null) throw new IllegalArgumentException("startDate cannot be null");
        if (endDate == null) throw new IllegalArgumentException("endDate cannot be null");
        if (!endDate.isAfter(startDate)) throw new IllegalArgumentException("endDate must be after startDate");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status != null ? status : PeriodStatus.OPEN;
    }

    public static AccountingPeriod create(AccountingPeriodId id,
                                          TenantId tenantId,
                                          CompanyId companyId,
                                          PeriodType periodType,
                                          LocalDate startDate,
                                          LocalDate endDate) {
        return new AccountingPeriod(id, tenantId, companyId, periodType, startDate, endDate, PeriodStatus.OPEN);
    }

    public void softClose() {
        if (status != PeriodStatus.OPEN) {
            throw new InvalidPeriodStateException(status.name(), "softClose");
        }
        this.status = PeriodStatus.SOFT_CLOSED;
    }

    public void hardClose() {
        if (status != PeriodStatus.SOFT_CLOSED) {
            throw new InvalidPeriodStateException(status.name(), "hardClose");
        }
        this.status = PeriodStatus.HARD_CLOSED;
    }

    public void reopen() {
        if (status == PeriodStatus.OPEN) {
            throw new InvalidPeriodStateException(status.name(), "reopen");
        }
        this.status = PeriodStatus.OPEN;
    }

    public boolean containsDate(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}

