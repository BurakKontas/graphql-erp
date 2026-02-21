package tr.kontas.erp.shipment.platform.persistence.deliveryorder;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "delivery_orders")
@Data
@NoArgsConstructor
public class DeliveryOrderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "sales_order_id")
    private String salesOrderId;

    @Column(name = "sales_order_number")
    private String salesOrderNumber;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_line2")
    private String addressLine2;
    @Column(name = "city")
    private String city;
    @Column(name = "state_or_province")
    private String stateOrProvince;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "deliveryOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DeliveryOrderLineJpaEntity> lines = new ArrayList<>();
}

