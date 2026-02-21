package tr.kontas.erp.inventory.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.application.stockmovement.RecordStockMovementCommand;
import tr.kontas.erp.inventory.application.stockmovement.RecordStockMovementUseCase;
import tr.kontas.erp.shipment.domain.event.ShipmentDeliveredEvent;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentDeliveredEventListener {

    private final RecordStockMovementUseCase recordStockMovementUseCase;

    @EventListener
    @Transactional
    public void handle(ShipmentDeliveredEvent event) {
        log.info("ShipmentDeliveredEvent received for shipment {} — recording ISSUE movements", event.getShipmentId());

        for (ShipmentDeliveredEvent.DeliveredLineData line : event.getLines()) {
            RecordStockMovementCommand command = new RecordStockMovementCommand(
                    CompanyId.of(event.getCompanyId()),
                    line.getItemId(),
                    event.getWarehouseId(),
                    "ISSUE",
                    line.getQuantity(),
                    "SHIPMENT",
                    event.getShipmentId().toString(),
                    "Auto-created from shipment delivery",
                    LocalDate.now()
            );
            recordStockMovementUseCase.execute(command);
        }

        log.info("Recorded {} ISSUE stock movements for shipment {}", event.getLines().size(), event.getShipmentId());
    }
}

