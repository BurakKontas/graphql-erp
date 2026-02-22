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
@Table(name = "rpt_sales_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptSalesOrder {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    private String status;

    @Column(name = "fulfillment_status")
    private String fulfillmentStatus;

    @Column(name = "invoicing_status")
    private String invoicingStatus;

    private BigDecimal subtotal;

    @Column(name = "tax_total")
    private BigDecimal taxTotal;

    private BigDecimal total;

    @Column(name = "currency_code")
    private String currencyCode;
}

