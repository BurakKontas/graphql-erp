package tr.kontas.erp.reporting.platform.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rpt_shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptShipment {

    @Id
    @Column(name = "shipment_id")
    private UUID shipmentId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Column(name = "delivery_order_id")
    private UUID deliveryOrderId;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Column(name = "warehouse_name")
    private String warehouseName;

    private String status;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "dispatched_at")
    private Instant dispatchedAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;
}

