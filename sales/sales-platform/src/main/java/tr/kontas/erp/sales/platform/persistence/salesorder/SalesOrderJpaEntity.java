package tr.kontas.erp.sales.platform.persistence.salesorder;

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
@Table(name = "sales_orders")
@Getter
@Setter
@NoArgsConstructor
public class SalesOrderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "payment_term_code")
    private String paymentTermCode;

    // Shipping address (flat columns)
    @Column(name = "shipping_address_line1")
    private String shippingAddressLine1;

    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;

    @Column(name = "shipping_city")
    private String shippingCity;

    @Column(name = "shipping_state_or_province")
    private String shippingStateOrProvince;

    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;

    @Column(name = "shipping_country_code")
    private String shippingCountryCode;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "fulfillment_status", nullable = false)
    private String fulfillmentStatus;

    @Column(name = "invoiced_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal invoicedAmount;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxTotal;

    @Column(name = "total", nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sequence ASC")
    private List<SalesOrderLineJpaEntity> lines = new ArrayList<>();
}
