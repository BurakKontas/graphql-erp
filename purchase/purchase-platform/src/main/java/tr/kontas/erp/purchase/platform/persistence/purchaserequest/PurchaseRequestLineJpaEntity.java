package tr.kontas.erp.purchase.platform.persistence.purchaserequest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "purchase_request_lines")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequestLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private PurchaseRequestJpaEntity request;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(name = "preferred_vendor_id")
    private String preferredVendorId;

    @Column(name = "notes")
    private String notes;
}

