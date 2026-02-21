package tr.kontas.erp.purchase.platform.persistence.purchasereturn;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "purchase_return_lines")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseReturnLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private PurchaseReturnJpaEntity purchaseReturn;

    @Column(name = "receipt_line_id")
    private String receiptLineId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(name = "line_reason")
    private String lineReason;
}

