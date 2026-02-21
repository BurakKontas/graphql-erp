package tr.kontas.erp.shipment.platform.persistence.deliveryorder;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "delivery_order_lines")
@Data
@NoArgsConstructor
public class DeliveryOrderLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_order_id", nullable = false)
    private DeliveryOrderJpaEntity deliveryOrder;

    @Column(name = "sales_order_line_id")
    private String salesOrderLineId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "ordered_qty", nullable = false, precision = 19, scale = 4)
    private BigDecimal orderedQty;

    @Column(name = "shipped_qty", nullable = false, precision = 19, scale = 4)
    private BigDecimal shippedQty;
}

