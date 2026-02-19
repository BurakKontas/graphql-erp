package tr.kontas.erp.core.platform.persistence.businesspartner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerRole;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "business_partner")
@Getter
@Setter
@NoArgsConstructor
public class BusinessPartnerJpaEntity {

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

    @Column(name = "taxNumber", nullable = false)
    private String taxNumber;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "business_partner_roles",
            joinColumns = @JoinColumn(name = "business_partner_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<BusinessPartnerRole> roles = new HashSet<>();

    public void deactivate() {
        this.active = false;
    }
}
