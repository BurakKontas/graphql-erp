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
@Table(name = "rpt_purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptPurchaseOrder {

    @Id
    @Column(name = "po_id")
    private UUID poId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "vendor_id")
    private UUID vendorId;

    @Column(name = "vendor_name")
    private String vendorName;

    private String status;

    private BigDecimal total;

    @Column(name = "currency_code")
    private String currencyCode;
}

