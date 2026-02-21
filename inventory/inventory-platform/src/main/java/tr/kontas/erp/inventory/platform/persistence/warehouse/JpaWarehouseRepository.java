package tr.kontas.erp.inventory.platform.persistence.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaWarehouseRepository extends JpaRepository<WarehouseJpaEntity, UUID> {
    Optional<WarehouseJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<WarehouseJpaEntity> findByCodeAndTenantIdAndCompanyId(String code, UUID tenantId, UUID companyId);
    List<WarehouseJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<WarehouseJpaEntity> findByIdIn(List<UUID> ids);
}
