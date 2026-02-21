package tr.kontas.erp.finance.application.accountingperiod;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriod;
import java.util.List;

public interface GetAccountingPeriodsByCompanyUseCase { List<AccountingPeriod> execute(CompanyId companyId); }

