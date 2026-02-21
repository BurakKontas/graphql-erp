package tr.kontas.erp.inventory.platform.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.application.stockmovement.RecordStockMovementCommand;
import tr.kontas.erp.inventory.application.stockmovement.RecordStockMovementUseCase;
import tr.kontas.erp.shipment.domain.event.ShipmentReturnReceivedEvent;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentReturnReceivedEventListener {

    private final RecordStockMovementUseCase recordStockMovementUseCase;

    @EventListener
    @Transactional
    public void handle(ShipmentReturnReceivedEvent event) {
        log.info("ShipmentReturnReceivedEvent received for return {} — recording RECEIPT movements", event.getShipmentReturnId());

        for (ShipmentReturnReceivedEvent.ReturnLineData line : event.getLines()) {
            RecordStockMovementCommand command = new RecordStockMovementCommand(
                    CompanyId.of(event.getCompanyId()),
                    line.getItemId(),
                    event.getWarehouseId(),
                    "RECEIPT",
                    line.getQuantity(),
                    "SHIPMENT_RETURN",
                    event.getShipmentReturnId().toString(),
                    "Auto-created from shipment return receipt",
                    LocalDate.now()
            );
            recordStockMovementUseCase.execute(command);
        }

        log.info("Recorded {} RECEIPT stock movements for shipment return {}", event.getLines().size(), event.getShipmentReturnId());
    }
}

