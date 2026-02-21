package tr.kontas.erp.inventory.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.inventory.application.item.*;
import tr.kontas.erp.inventory.application.port.UnitResolutionPort;
import tr.kontas.erp.inventory.domain.category.CategoryId;
import tr.kontas.erp.inventory.domain.item.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService implements
        CreateItemUseCase,
        GetItemByIdUseCase,
        GetItemsByCompanyUseCase,
        GetItemsByIdsUseCase,
        UpdateItemUseCase,
        DeactivateItemUseCase,
        ActivateItemUseCase {

    private final ItemRepository itemRepository;
    private final UnitResolutionPort unitResolution;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public ItemId execute(CreateItemCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        Unit unit = command.unitCode() != null
                ? unitResolution.resolveUnit(command.unitCode()).orElse(null)
                : null;

        CategoryId categoryId = command.categoryId() != null
                ? CategoryId.of(command.categoryId())
                : null;

        ItemId id = ItemId.newId();
        Item item = new Item(
                id,
                tenantId,
                companyId,
                new ItemCode(command.code()),
                new ItemName(command.name()),
                ItemType.valueOf(command.type()),
                unit,
                categoryId,
                command.allowNegativeStock()
        );

        saveAndPublish(item);
        return id;
    }

    @Override
    public Item execute(ItemId id) {
        TenantId tenantId = TenantContext.get();
        return itemRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + id));
    }

    @Override
    public List<Item> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return itemRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<Item> execute(List<ItemId> ids) {
        return itemRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void execute(UpdateItemCommand command) {
        Item item = loadItem(command.itemId());

        Unit unit = command.unitCode() != null
                ? unitResolution.resolveUnit(command.unitCode()).orElse(null)
                : null;

        CategoryId categoryId = command.categoryId() != null
                ? CategoryId.of(command.categoryId())
                : null;

        item.update(new ItemName(command.name()), unit, categoryId);
        saveAndPublish(item);
    }

    @Override
    @Transactional
    public void deactivate(String itemId) {
        Item item = loadItem(itemId);
        item.deactivate();
        saveAndPublish(item);
    }

    @Override
    @Transactional
    public void activate(String itemId) {
        Item item = loadItem(itemId);
        item.activate();
        saveAndPublish(item);
    }

    private Item loadItem(String itemId) {
        TenantId tenantId = TenantContext.get();
        return itemRepository.findById(ItemId.of(itemId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
    }

    private void saveAndPublish(Item item) {
        itemRepository.save(item);
        eventPublisher.publishAll(item.getDomainEvents());
        item.clearDomainEvents();
    }
}
