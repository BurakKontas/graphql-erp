package tr.kontas.erp.finance.application.account;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.account.Account;

import java.util.List;

public interface GetAccountsByCompanyUseCase {
    List<Account> execute(CompanyId companyId);
}

