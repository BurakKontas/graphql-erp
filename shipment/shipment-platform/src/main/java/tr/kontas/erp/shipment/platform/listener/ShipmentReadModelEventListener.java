package tr.kontas.erp.shipment.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.shipment.domain.event.ShipmentCreatedEvent;
import tr.kontas.erp.shipment.domain.event.ShipmentDeliveredEvent;
import tr.kontas.erp.shipment.domain.event.ShipmentDispatchedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @EventListener
    @Transactional
    public void handle(ShipmentCreatedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("shipmentId", event.getShipmentId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("shipmentNumber", event.getNumber())
                .addValue("deliveryOrderId", event.getDeliveryOrderId())
                .addValue("warehouseId", event.getWarehouseId())
                .addValue("status", "PREPARING");

        jdbc.update("""
                INSERT INTO rpt_shipments (shipment_id, tenant_id, company_id, shipment_number,
                    delivery_order_id, warehouse_id, status)
                VALUES (:shipmentId, :tenantId, :companyId, :shipmentNumber,
                    CAST(:deliveryOrderId AS UUID), CAST(:warehouseId AS UUID), :status)
                ON CONFLICT (shipment_id) DO UPDATE SET
                    status = EXCLUDED.status
                """, params);
        log.debug("Read model updated for Shipment created: {}", event.getShipmentId());
    }

    @EventListener
    @Transactional
    public void handle(ShipmentDispatchedEvent event) {
        jdbc.update(
                "UPDATE rpt_shipments SET status = 'DISPATCHED', dispatched_at = NOW() WHERE shipment_id = :shipmentId",
                new MapSqlParameterSource("shipmentId", event.getShipmentId())
        );
        log.debug("Read model updated for Shipment dispatched: {}", event.getShipmentId());
    }

    @EventListener
    @Transactional
    public void handle(ShipmentDeliveredEvent event) {
        jdbc.update(
                "UPDATE rpt_shipments SET status = 'DELIVERED', delivered_at = NOW() WHERE shipment_id = :shipmentId",
                new MapSqlParameterSource("shipmentId", event.getShipmentId())
        );
        log.debug("Read model updated for Shipment delivered: {}", event.getShipmentId());
    }
}

