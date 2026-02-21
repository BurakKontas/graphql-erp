package tr.kontas.erp.finance.platform.persistence.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class AccountJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "nature", nullable = false)
    private String nature;

    @Column(name = "parent_account_id")
    private UUID parentAccountId;

    @Column(name = "system_account", nullable = false)
    private boolean systemAccount;

    @Column(name = "active", nullable = false)
    private boolean active;
}
