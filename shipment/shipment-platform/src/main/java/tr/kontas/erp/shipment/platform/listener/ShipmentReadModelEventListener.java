package tr.kontas.erp.shipment.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tr.kontas.erp.shipment.domain.event.ShipmentCreatedEvent;
import tr.kontas.erp.shipment.domain.event.ShipmentDeliveredEvent;
import tr.kontas.erp.shipment.domain.event.ShipmentDispatchedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ShipmentCreatedEvent event) {
        try {
            handleCreated(event);
        } catch (Exception e) {
            log.error("Failed to update shipment read model {}", event.getShipmentId(), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCreated(ShipmentCreatedEvent event) {
        String schema = schemaNameFromTenantId(event.getTenantId().toString());
        String table = schema + ".rpt_shipments";

        var params = new MapSqlParameterSource()
                .addValue("shipmentId", event.getShipmentId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("shipmentNumber", event.getNumber())
                .addValue("deliveryOrderId", event.getDeliveryOrderId())
                .addValue("warehouseId", event.getWarehouseId())
                .addValue("status", "PREPARING");

        String sql = """
            INSERT INTO %s (shipment_id, tenant_id, company_id, shipment_number,
                delivery_order_id, warehouse_id, status)
            VALUES (:shipmentId, :tenantId, :companyId, :shipmentNumber,
                CAST(:deliveryOrderId AS UUID), CAST(:warehouseId AS UUID), :status)
            ON CONFLICT (shipment_id) DO UPDATE SET
                status = EXCLUDED.status
        """.formatted(table);

        jdbc.update(sql, params);
        log.debug("Read model updated for Shipment created: {} (schema={})", event.getShipmentId(), schema);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ShipmentDispatchedEvent event) {
        try {
            handleDispatched(event);
        } catch (Exception e) {
            log.error("Failed to update shipment read model dispatched {}", event.getShipmentId(), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleDispatched(ShipmentDispatchedEvent event) {
        String schema = schemaNameFromTenantId(event.getTenantId().toString());
        String table = schema + ".rpt_shipments";
        jdbc.update(
                "UPDATE " + table + " SET status = 'DISPATCHED', dispatched_at = NOW() WHERE shipment_id = :shipmentId",
                new MapSqlParameterSource("shipmentId", event.getShipmentId())
        );
        log.debug("Read model updated for Shipment dispatched: {} (schema={})", event.getShipmentId(), schema);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ShipmentDeliveredEvent event) {
        try {
            handleDelivered(event);
        } catch (Exception e) {
            log.error("Failed to update shipment read model delivered {}", event.getShipmentId(), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleDelivered(ShipmentDeliveredEvent event) {
        String schema = schemaNameFromTenantId(event.getTenantId().toString());
        String table = schema + ".rpt_shipments";
        jdbc.update(
                "UPDATE " + table + " SET status = 'DELIVERED', delivered_at = NOW() WHERE shipment_id = :shipmentId",
                new MapSqlParameterSource("shipmentId", event.getShipmentId())
        );
        log.debug("Read model updated for Shipment delivered: {} (schema={})", event.getShipmentId(), schema);
    }

    private String schemaNameFromTenantId(String tenantId) {
        if (tenantId == null) return "public";
        return "tenant_" + tenantId.replace("-", "");
    }
}
