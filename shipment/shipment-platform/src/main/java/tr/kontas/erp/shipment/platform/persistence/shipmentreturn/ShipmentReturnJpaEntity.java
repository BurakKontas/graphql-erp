package tr.kontas.erp.shipment.platform.persistence.shipmentreturn;

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
@Table(name = "shipment_returns")
@Data
@NoArgsConstructor
public class ShipmentReturnJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "shipment_id")
    private String shipmentId;

    @Column(name = "sales_order_id")
    private String salesOrderId;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "received_at")
    private Instant receivedAt;

    @OneToMany(mappedBy = "shipmentReturn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShipmentReturnLineJpaEntity> lines = new ArrayList<>();
}

