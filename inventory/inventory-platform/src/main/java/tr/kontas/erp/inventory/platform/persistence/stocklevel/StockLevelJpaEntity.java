package tr.kontas.erp.inventory.platform.persistence.stocklevel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "stock_levels")
@Getter
@Setter
@NoArgsConstructor
public class StockLevelJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "warehouse_id", nullable = false)
    private UUID warehouseId;

    @Column(name = "quantity_on_hand", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantityOnHand;

    @Column(name = "quantity_reserved", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantityReserved;

    @Column(name = "reorder_point", precision = 19, scale = 4)
    private BigDecimal reorderPoint;

    @Column(name = "allow_negative_stock", nullable = false)
    private boolean allowNegativeStock;
}
