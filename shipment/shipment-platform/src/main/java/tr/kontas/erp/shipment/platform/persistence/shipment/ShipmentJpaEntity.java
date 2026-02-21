package tr.kontas.erp.shipment.platform.persistence.shipment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
public class ShipmentJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "delivery_order_id")
    private String deliveryOrderId;

    @Column(name = "sales_order_id")
    private String salesOrderId;

    @Column(name = "warehouse_id")
    private String warehouseId;

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

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "dispatched_at")
    private Instant dispatchedAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShipmentLineJpaEntity> lines = new ArrayList<>();
}

