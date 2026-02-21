package tr.kontas.erp.finance.application.accountingperiod;

import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriod;
import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriodId;

public interface GetAccountingPeriodByIdUseCase { AccountingPeriod execute(AccountingPeriodId id); }

