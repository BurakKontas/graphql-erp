package tr.kontas.erp.finance.platform.persistence.salesinvoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sales_invoices")
@Getter
@Setter
@NoArgsConstructor
public class SalesInvoiceJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "invoice_type", nullable = false)
    private String invoiceType;

    @Column(name = "sales_order_id")
    private String salesOrderId;

    @Column(name = "sales_order_number")
    private String salesOrderNumber;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate", precision = 19, scale = 6)
    private BigDecimal exchangeRate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "subtotal", precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_total", precision = 19, scale = 2)
    private BigDecimal taxTotal;

    @Column(name = "total", precision = 19, scale = 2)
    private BigDecimal total;

    @Column(name = "paid_amount", precision = 19, scale = 2)
    private BigDecimal paidAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SalesInvoiceLineJpaEntity> lines = new ArrayList<>();
}
