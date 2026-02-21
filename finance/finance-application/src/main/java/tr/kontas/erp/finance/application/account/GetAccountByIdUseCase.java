package tr.kontas.erp.finance.application.account;

import tr.kontas.erp.finance.domain.account.Account;
import tr.kontas.erp.finance.domain.account.AccountId;

public interface GetAccountByIdUseCase {
    Account execute(AccountId id);
}

