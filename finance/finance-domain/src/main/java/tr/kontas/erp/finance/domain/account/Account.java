package tr.kontas.erp.finance.domain.account;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.exception.InvalidAccountStateException;

@Getter
public class Account extends AggregateRoot<AccountId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private String code;
    private String name;
    private AccountType type;
    private AccountNature nature;
    private AccountId parentAccountId;
    private boolean systemAccount;
    private boolean active;

    public Account(AccountId id,
                   TenantId tenantId,
                   CompanyId companyId,
                   String code,
                   String name,
                   AccountType type,
                   AccountNature nature,
                   AccountId parentAccountId,
                   boolean systemAccount,
                   boolean active) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("code cannot be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be blank");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (nature == null) throw new IllegalArgumentException("nature cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.nature = nature;
        this.parentAccountId = parentAccountId;
        this.systemAccount = systemAccount;
        this.active = active;
    }

    public static Account create(AccountId id,
                                 TenantId tenantId,
                                 CompanyId companyId,
                                 String code,
                                 String name,
                                 AccountType type,
                                 AccountNature nature,
                                 AccountId parentAccountId,
                                 boolean systemAccount) {
        return new Account(id, tenantId, companyId, code, name, type, nature,
                parentAccountId, systemAccount, true);
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank())
            throw new IllegalArgumentException("name cannot be blank");
        this.name = newName;
    }

    public void update(String code, String name, AccountType type, AccountNature nature, AccountId parentAccountId) {
        if (code != null && !code.isBlank()) this.code = code;
        if (name != null && !name.isBlank()) this.name = name;
        if (type != null) this.type = type;
        if (nature != null) this.nature = nature;
        this.parentAccountId = parentAccountId;
    }

    public void deactivate() {
        if (systemAccount) {
            throw new InvalidAccountStateException("Cannot deactivate system account: " + code);
        }
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}

