package tr.kontas.erp.shipment.platform.persistence.shipment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shipment_lines")
@Data
@NoArgsConstructor
public class ShipmentLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private ShipmentJpaEntity shipment;

    @Column(name = "delivery_order_line_id")
    private String deliveryOrderLineId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;
}

