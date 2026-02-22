package tr.kontas.erp.purchase.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.purchase.domain.event.GoodsReceiptPostedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseReadModelEventListener {

    private final NamedParameterJdbcTemplate jdbc;

    @EventListener
    @Transactional
    public void handle(GoodsReceiptPostedEvent event) {
        jdbc.update("""
                UPDATE rpt_purchase_orders SET status = 'PARTIALLY_RECEIVED'
                WHERE po_id = CAST(:poId AS UUID)
                    AND status NOT IN ('RECEIVED', 'INVOICED', 'CANCELLED')
                """, new MapSqlParameterSource("poId", event.getPurchaseOrderId()));
        log.debug("Read model updated for GoodsReceipt posted: {}", event.getGoodsReceiptId());
    }
}

