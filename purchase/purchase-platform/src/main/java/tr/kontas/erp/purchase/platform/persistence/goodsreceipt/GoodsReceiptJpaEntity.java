package tr.kontas.erp.purchase.platform.persistence.goodsreceipt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "goods_receipts")
@Getter
@Setter
@NoArgsConstructor
public class GoodsReceiptJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;

    @Column(name = "purchase_order_id")
    private String purchaseOrderId;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "vendor_delivery_note")
    private String vendorDeliveryNote;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GoodsReceiptLineJpaEntity> lines = new ArrayList<>();
}

