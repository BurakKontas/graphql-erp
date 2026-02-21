package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.accountingperiod.*;
import tr.kontas.erp.finance.domain.accountingperiod.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingPeriodService implements
        CreateAccountingPeriodUseCase, GetAccountingPeriodByIdUseCase,
        GetAccountingPeriodsByCompanyUseCase {

    private final AccountingPeriodRepository periodRepository;

    @Override
    @Transactional
    public AccountingPeriodId execute(CreateAccountingPeriodCommand cmd) {
        TenantId tenantId = TenantContext.get();
        AccountingPeriodId id = AccountingPeriodId.newId();
        AccountingPeriod period = AccountingPeriod.create(id, tenantId, cmd.companyId(),
                PeriodType.valueOf(cmd.periodType()), cmd.startDate(), cmd.endDate());
        periodRepository.save(period);
        return id;
    }

    @Override
    public AccountingPeriod execute(AccountingPeriodId id) {
        TenantId tenantId = TenantContext.get();
        return periodRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("AccountingPeriod not found: " + id));
    }

    @Override
    public List<AccountingPeriod> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return periodRepository.findByCompanyId(tenantId, companyId);
    }

    @Transactional
    public void softClose(String periodId) {
        AccountingPeriod period = loadPeriod(periodId);
        period.softClose();
        periodRepository.save(period);
    }

    @Transactional
    public void hardClose(String periodId) {
        AccountingPeriod period = loadPeriod(periodId);
        period.hardClose();
        periodRepository.save(period);
    }

    @Transactional
    public void reopen(String periodId) {
        AccountingPeriod period = loadPeriod(periodId);
        period.reopen();
        periodRepository.save(period);
    }

    private AccountingPeriod loadPeriod(String periodId) {
        TenantId tenantId = TenantContext.get();
        return periodRepository.findById(AccountingPeriodId.of(periodId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("AccountingPeriod not found: " + periodId));
    }
}
