package tr.kontas.erp.purchase.platform.persistence.vendorinvoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "vendor_invoice_lines")
@Getter
@Setter
@NoArgsConstructor
public class VendorInvoiceLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private VendorInvoiceJpaEntity invoice;

    @Column(name = "po_line_id")
    private String poLineId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 6)
    private BigDecimal unitPrice;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "tax_rate", precision = 10, scale = 4)
    private BigDecimal taxRate;

    @Column(name = "line_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "tax_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "line_total_with_tax", nullable = false, precision = 19, scale = 2)
    private BigDecimal lineTotalWithTax;

    @Column(name = "account_id")
    private String accountId;
}

