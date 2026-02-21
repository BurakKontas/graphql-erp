package tr.kontas.erp.finance.platform.persistence.salesinvoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "sales_invoice_lines")
@Getter
@Setter
@NoArgsConstructor
public class SalesInvoiceLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private SalesInvoiceJpaEntity invoice;

    @Column(name = "sales_order_line_id")
    private String salesOrderLineId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "quantity", precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 19, scale = 6)
    private BigDecimal unitPrice;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "tax_rate", precision = 10, scale = 4)
    private BigDecimal taxRate;

    @Column(name = "line_total", precision = 19, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "line_total_with_tax", precision = 19, scale = 2)
    private BigDecimal lineTotalWithTax;

    @Column(name = "revenue_account_id")
    private String revenueAccountId;
}
