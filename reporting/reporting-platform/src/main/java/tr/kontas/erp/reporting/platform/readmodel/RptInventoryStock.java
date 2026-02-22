package tr.kontas.erp.reporting.platform.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "rpt_inventory_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptInventoryStock {

    @Id
    @Column(name = "stock_id")
    private UUID stockId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "qty_on_hand")
    private BigDecimal qtyOnHand;

    @Column(name = "qty_reserved")
    private BigDecimal qtyReserved;

    @Column(name = "qty_available")
    private BigDecimal qtyAvailable;

    @Column(name = "reorder_point")
    private BigDecimal reorderPoint;

    @Column(name = "below_reorder")
    private boolean belowReorder;
}

