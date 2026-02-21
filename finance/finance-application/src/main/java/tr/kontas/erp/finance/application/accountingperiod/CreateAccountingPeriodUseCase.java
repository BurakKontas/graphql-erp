package tr.kontas.erp.finance.application.accountingperiod;

import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriodId;

public interface CreateAccountingPeriodUseCase { AccountingPeriodId execute(CreateAccountingPeriodCommand command); }

