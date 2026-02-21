package tr.kontas.erp.finance.platform.persistence.account;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.account.*;

public class AccountMapper {
    public static AccountJpaEntity toEntity(Account d) {
        AccountJpaEntity e = new AccountJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setCode(d.getCode());
        e.setName(d.getName());
        e.setType(d.getType().name());
        e.setNature(d.getNature().name());
        e.setParentAccountId(d.getParentAccountId() != null ? d.getParentAccountId().asUUID() : null);
        e.setSystemAccount(d.isSystemAccount());
        e.setActive(d.isActive());
        return e;
    }

    public static Account toDomain(AccountJpaEntity e) {
        return new Account(
                AccountId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                e.getCode(),
                e.getName(),
                AccountType.valueOf(e.getType()),
                AccountNature.valueOf(e.getNature()),
                e.getParentAccountId() != null ? AccountId.of(e.getParentAccountId()) : null,
                e.isSystemAccount(),
                e.isActive()
        );
    }
}

