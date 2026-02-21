package tr.kontas.erp.purchase.platform.persistence.vendorcatalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vendor_catalogs")
@Getter
@Setter
@NoArgsConstructor
public class VendorCatalogJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "vendor_id", nullable = false)
    private String vendorId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VendorCatalogEntryJpaEntity> entries = new ArrayList<>();
}

