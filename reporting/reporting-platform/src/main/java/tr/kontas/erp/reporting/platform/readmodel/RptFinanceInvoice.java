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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rpt_finance_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptFinanceInvoice {

    @Id
    @Column(name = "invoice_id")
    private UUID invoiceId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "currency_code")
    private String currencyCode;

    private BigDecimal total;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;

    @Column(name = "payment_status")
    private String paymentStatus;
}

