package tr.kontas.erp.inventory.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.inventory.application.warehouse.*;
import tr.kontas.erp.inventory.domain.warehouse.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService implements
        CreateWarehouseUseCase,
        GetWarehouseByIdUseCase,
        GetWarehousesByCompanyUseCase,
        GetWarehousesByIdsUseCase,
        RenameWarehouseUseCase,
        DeactivateWarehouseUseCase,
        ActivateWarehouseUseCase {

    private final WarehouseRepository warehouseRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public WarehouseId execute(CreateWarehouseCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        WarehouseId id = WarehouseId.newId();
        Warehouse warehouse = new Warehouse(
                id,
                tenantId,
                companyId,
                new WarehouseCode(command.code()),
                command.name()
        );

        saveAndPublish(warehouse);
        return id;
    }

    @Override
    public Warehouse execute(WarehouseId id) {
        TenantId tenantId = TenantContext.get();
        return warehouseRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + id));
    }

    @Override
    public List<Warehouse> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return warehouseRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<Warehouse> execute(List<WarehouseId> ids) {
        return warehouseRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void execute(String warehouseId, String newName) {
        Warehouse warehouse = loadWarehouse(warehouseId);
        warehouse.rename(newName);
        saveAndPublish(warehouse);
    }

    @Override
    @Transactional
    public void deactivate(String warehouseId) {
        Warehouse warehouse = loadWarehouse(warehouseId);
        warehouse.deactivate();
        saveAndPublish(warehouse);
    }

    @Override
    @Transactional
    public void activate(String warehouseId) {
        Warehouse warehouse = loadWarehouse(warehouseId);
        warehouse.activate();
        saveAndPublish(warehouse);
    }

    private Warehouse loadWarehouse(String warehouseId) {
        TenantId tenantId = TenantContext.get();
        return warehouseRepository.findById(WarehouseId.of(warehouseId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found: " + warehouseId));
    }

    private void saveAndPublish(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
        eventPublisher.publishAll(warehouse.getDomainEvents());
        warehouse.clearDomainEvents();
    }
}
