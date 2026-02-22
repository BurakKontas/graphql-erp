package tr.kontas.erp.inventory.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.inventory.domain.event.StockLevelChangedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @EventListener
    @Transactional
    public void handle(StockLevelChangedEvent event) {
        var params = new MapSqlParameterSource()
                .addValue("stockId", event.getStockLevelId())
                .addValue("tenantId", event.getTenantId())
                .addValue("companyId", event.getCompanyId())
                .addValue("itemId", event.getItemId())
                .addValue("warehouseId", event.getWarehouseId())
                .addValue("qtyOnHand", event.getNewOnHand())
                .addValue("qtyReserved", event.getReserved())
                .addValue("qtyAvailable", event.getAvailable());

        jdbc.update("""
                INSERT INTO rpt_inventory_stock (stock_id, tenant_id, company_id, item_id, warehouse_id,
                    qty_on_hand, qty_reserved, qty_available)
                VALUES (:stockId, :tenantId, :companyId, :itemId, :warehouseId,
                    :qtyOnHand, :qtyReserved, :qtyAvailable)
                ON CONFLICT (stock_id) DO UPDATE SET
                    qty_on_hand = EXCLUDED.qty_on_hand,
                    qty_reserved = EXCLUDED.qty_reserved,
                    qty_available = EXCLUDED.qty_available
                """, params);
        log.debug("Read model updated for StockLevel changed: {}", event.getStockLevelId());
    }
}

