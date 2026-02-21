package tr.kontas.erp.finance.platform.persistence.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.account.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository jpa;

    @Override
    public void save(Account account) {
        jpa.save(AccountMapper.toEntity(account));
    }

    @Override
    public Optional<Account> findById(AccountId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(AccountMapper::toDomain);
    }

    @Override
    public Optional<Account> findByCode(TenantId tenantId, CompanyId companyId, String code) {
        return jpa.findByTenantIdAndCompanyIdAndCode(tenantId.asUUID(), companyId.asUUID(), code)
                .map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(AccountMapper::toDomain).toList();
    }

    @Override
    public List<Account> findByIds(List<AccountId> ids) {
        return jpa.findByIdIn(ids.stream().map(AccountId::asUUID).toList())
                .stream().map(AccountMapper::toDomain).toList();
    }

    @Override
    public boolean existsByCode(TenantId tenantId, CompanyId companyId, String code) {
        return jpa.existsByTenantIdAndCompanyIdAndCode(tenantId.asUUID(), companyId.asUUID(), code);
    }
}
