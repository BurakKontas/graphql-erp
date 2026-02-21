package tr.kontas.erp.purchase.platform.persistence.purchasereturn;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_returns")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseReturnJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "return_number", nullable = false, unique = true)
    private String returnNumber;

    @Column(name = "purchase_order_id")
    private String purchaseOrderId;

    @Column(name = "goods_receipt_id")
    private String goodsReceiptId;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "purchaseReturn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PurchaseReturnLineJpaEntity> lines = new ArrayList<>();
}

