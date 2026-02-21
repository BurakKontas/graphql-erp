package tr.kontas.erp.finance.application.account;

import tr.kontas.erp.finance.domain.account.AccountId;

public interface CreateAccountUseCase {
    AccountId execute(CreateAccountCommand command);
}

