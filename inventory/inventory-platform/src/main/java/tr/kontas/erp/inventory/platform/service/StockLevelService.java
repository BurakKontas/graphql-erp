package tr.kontas.erp.inventory.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.inventory.application.stocklevel.*;
import tr.kontas.erp.inventory.domain.item.Item;
import tr.kontas.erp.inventory.domain.item.ItemId;
import tr.kontas.erp.inventory.domain.item.ItemRepository;
import tr.kontas.erp.inventory.domain.stocklevel.*;
import tr.kontas.erp.inventory.domain.warehouse.WarehouseId;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockLevelService implements
        GetStockLevelByIdUseCase,
        GetStockLevelsByWarehouseUseCase,
        GetStockLevelsByItemUseCase,
        ReserveStockUseCase,
        ReleaseReservationUseCase,
        AdjustStockUseCase,
        SetReorderPointUseCase {

    private final StockLevelRepository stockLevelRepository;
    private final ItemRepository itemRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public StockLevel execute(StockLevelId id) {
        TenantId tenantId = TenantContext.get();
        return stockLevelRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("StockLevel not found: " + id));
    }

    @Override
    public List<StockLevel> execute(WarehouseId warehouseId) {
        TenantId tenantId = TenantContext.get();
        return stockLevelRepository.findByWarehouseId(warehouseId, tenantId);
    }

    @Override
    public List<StockLevel> execute(ItemId itemId) {
        TenantId tenantId = TenantContext.get();
        return stockLevelRepository.findByItemId(itemId, tenantId);
    }

    @Override
    @Transactional
    public void execute(ReserveStockCommand command) {
        StockLevel stockLevel = loadOrCreateStockLevel(
                ItemId.of(command.itemId()),
                WarehouseId.of(command.warehouseId())
        );
        stockLevel.reserve(command.quantity(), command.referenceId());
        saveAndPublish(stockLevel);
    }

    @Override
    @Transactional
    public void execute(ReleaseReservationCommand command) {
        TenantId tenantId = TenantContext.get();
        StockLevel stockLevel = stockLevelRepository
                .findByItemAndWarehouse(ItemId.of(command.itemId()), WarehouseId.of(command.warehouseId()), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("StockLevel not found for item: " + command.itemId() + " warehouse: " + command.warehouseId()));
        stockLevel.releaseReservation(command.quantity(), command.referenceId());
        saveAndPublish(stockLevel);
    }

    @Override
    @Transactional
    public void execute(AdjustStockCommand command) {
        StockLevel stockLevel = loadOrCreateStockLevel(
                ItemId.of(command.itemId()),
                WarehouseId.of(command.warehouseId())
        );
        stockLevel.adjust(command.adjustment());
        saveAndPublish(stockLevel);
    }

    @Override
    @Transactional
    public void execute(String stockLevelId, BigDecimal reorderPoint) {
        TenantId tenantId = TenantContext.get();
        StockLevel stockLevel = stockLevelRepository.findById(StockLevelId.of(stockLevelId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("StockLevel not found: " + stockLevelId));
        stockLevel.setReorderPoint(reorderPoint);
        saveAndPublish(stockLevel);
    }

    private StockLevel loadOrCreateStockLevel(ItemId itemId, WarehouseId warehouseId) {
        TenantId tenantId = TenantContext.get();
        return stockLevelRepository.findByItemAndWarehouse(itemId, warehouseId, tenantId)
                .orElseGet(() -> {
                    Item item = itemRepository.findById(itemId, tenantId)
                            .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
                    return new StockLevel(
                            StockLevelId.newId(),
                            tenantId,
                            item.getCompanyId(),
                            itemId,
                            warehouseId,
                            item.isAllowNegativeStock()
                    );
                });
    }

    private void saveAndPublish(StockLevel stockLevel) {
        stockLevelRepository.save(stockLevel);
        eventPublisher.publishAll(stockLevel.getDomainEvents());
        stockLevel.clearDomainEvents();
    }
}
