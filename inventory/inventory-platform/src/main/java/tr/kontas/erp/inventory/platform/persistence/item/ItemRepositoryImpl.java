package tr.kontas.erp.inventory.platform.persistence.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.application.port.UnitResolutionPort;
import tr.kontas.erp.inventory.domain.item.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final JpaItemRepository jpaRepository;
    private final UnitResolutionPort unitResolution;

    @Override
    public void save(Item item) {
        jpaRepository.save(ItemMapper.toEntity(item));
    }

    @Override
    public Optional<Item> findById(ItemId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(this::mapToDomain);
    }

    @Override
    public Optional<Item> findByCode(ItemCode code, TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByCodeAndTenantIdAndCompanyId(code.getValue(), tenantId.asUUID(), companyId.asUUID())
                .map(this::mapToDomain);
    }

    @Override
    public List<Item> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByIds(List<ItemId> ids) {
        List<UUID> uuids = ids.stream().map(ItemId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Item mapToDomain(ItemJpaEntity entity) {
        Unit unit = null;
        if (entity.getUnitCode() != null) {
            unit = unitResolution.resolveUnit(entity.getUnitCode()).orElse(null);
        }
        return ItemMapper.toDomain(entity, unit);
    }
}
