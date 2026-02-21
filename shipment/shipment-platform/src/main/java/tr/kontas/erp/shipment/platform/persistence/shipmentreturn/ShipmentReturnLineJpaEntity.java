package tr.kontas.erp.shipment.platform.persistence.shipmentreturn;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shipment_return_lines")
@Data
@NoArgsConstructor
public class ShipmentReturnLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_return_id", nullable = false)
    private ShipmentReturnJpaEntity shipmentReturn;

    @Column(name = "shipment_line_id")
    private String shipmentLineId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(name = "line_reason")
    private String lineReason;
}

