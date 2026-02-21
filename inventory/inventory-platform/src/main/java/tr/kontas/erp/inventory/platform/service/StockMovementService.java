package tr.kontas.erp.inventory.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.inventory.application.stockmovement.*;
import tr.kontas.erp.inventory.domain.item.Item;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.item.ItemRepository;
import tr.kontas.erp.inventory.domain.stocklevel.*;
import tr.kontas.erp.inventory.domain.stockmovement.*;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService implements
        RecordStockMovementUseCase,
        GetStockMovementsByWarehouseUseCase,
        GetStockMovementsByItemUseCase {

    private final StockMovementRepository stockMovementRepository;
    private final StockLevelRepository stockLevelRepository;
    private final ItemRepository itemRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public StockMovementId execute(RecordStockMovementCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        ItemId itemId = ItemId.of(command.itemId());
        WarehouseId warehouseId = WarehouseId.of(command.warehouseId());
        MovementType movementType = MovementType.valueOf(command.movementType());
        ReferenceType referenceType = ReferenceType.valueOf(command.referenceType());
        LocalDate movementDate = command.movementDate() != null ? command.movementDate() : LocalDate.now();

        StockMovementId id = StockMovementId.newId();
        StockMovement movement = new StockMovement(
                id,
                tenantId,
                companyId,
                itemId,
                warehouseId,
                movementType,
                command.quantity(),
                referenceType,
                command.referenceId(),
                command.note(),
                movementDate
        );

        stockMovementRepository.save(movement);
        eventPublisher.publishAll(movement.getDomainEvents());
        movement.clearDomainEvents();

        // Update stock level
        StockLevel stockLevel = stockLevelRepository
                .findByItemAndWarehouse(itemId, warehouseId, tenantId)
                .orElseGet(() -> {
                    boolean allowNegative = itemRepository.findById(itemId, tenantId)
                            .map(Item::isAllowNegativeStock)
                            .orElse(false);
                    return new StockLevel(
                            StockLevelId.newId(),
                            tenantId,
                            companyId,
                            itemId,
                            warehouseId,
                            allowNegative
                    );
                });

        if (movementType.increasesStock()) {
            stockLevel.increaseOnHand(command.quantity());
        } else {
            stockLevel.decreaseOnHand(command.quantity(), false);
        }

        stockLevelRepository.save(stockLevel);
        eventPublisher.publishAll(stockLevel.getDomainEvents());
        stockLevel.clearDomainEvents();

        return id;
    }

    @Override
    public List<StockMovement> execute(WarehouseId warehouseId) {
        TenantId tenantId = TenantContext.get();
        return stockMovementRepository.findByWarehouseId(warehouseId, tenantId);
    }

    @Override
    public List<StockMovement> execute(ItemId itemId) {
        TenantId tenantId = TenantContext.get();
        return stockMovementRepository.findByItemId(itemId, tenantId);
    }
}
