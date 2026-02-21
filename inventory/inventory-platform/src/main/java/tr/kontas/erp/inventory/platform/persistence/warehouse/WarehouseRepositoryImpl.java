package tr.kontas.erp.inventory.platform.persistence.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.warehouse.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final JpaWarehouseRepository jpaRepository;

    @Override
    public void save(Warehouse warehouse) {
        jpaRepository.save(WarehouseMapper.toEntity(warehouse));
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(WarehouseMapper::toDomain);
    }

    @Override
    public Optional<Warehouse> findByCode(WarehouseCode code, TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByCodeAndTenantIdAndCompanyId(code.getValue(), tenantId.asUUID(), companyId.asUUID())
                .map(WarehouseMapper::toDomain);
    }

    @Override
    public List<Warehouse> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(WarehouseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Warehouse> findByIds(List<WarehouseId> ids) {
        List<UUID> uuids = ids.stream().map(WarehouseId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(WarehouseMapper::toDomain)
                .collect(Collectors.toList());
    }
}
