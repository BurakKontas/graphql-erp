package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.account.*;
import tr.kontas.erp.finance.domain.account.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, GetAccountByIdUseCase, GetAccountsByCompanyUseCase {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountId execute(CreateAccountCommand cmd) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = cmd.companyId();

        if (accountRepository.existsByCode(tenantId, companyId, cmd.code())) {
            throw new IllegalArgumentException("Account code already exists: " + cmd.code());
        }

        AccountId parentId = cmd.parentAccountId() != null ? AccountId.of(cmd.parentAccountId()) : null;
        AccountId id = AccountId.newId();
        Account account = Account.create(id, tenantId, companyId,
                cmd.code(), cmd.name(),
                AccountType.valueOf(cmd.type()), AccountNature.valueOf(cmd.nature()),
                parentId, cmd.systemAccount());

        accountRepository.save(account);
        return id;
    }

    @Override
    public Account execute(AccountId id) {
        TenantId tenantId = TenantContext.get();
        return accountRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
    }

    @Override
    public List<Account> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return accountRepository.findByCompanyId(tenantId, companyId);
    }
}

