package tr.kontas.erp.purchase.platform.persistence.vendorcatalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "vendor_catalog_entries")
@Getter
@Setter
@NoArgsConstructor
public class VendorCatalogEntryJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private VendorCatalogJpaEntity catalog;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 6)
    private BigDecimal unitPrice;

    @Column(name = "minimum_order_qty", precision = 19, scale = 4)
    private BigDecimal minimumOrderQty;

    @Column(name = "price_break_qty", precision = 19, scale = 4)
    private BigDecimal priceBreakQty;

    @Column(name = "price_break_unit_price", precision = 19, scale = 6)
    private BigDecimal priceBreakUnitPrice;
}

