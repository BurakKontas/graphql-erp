package tr.kontas.erp.inventory.platform.persistence.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaItemRepository extends JpaRepository<ItemJpaEntity, UUID> {
    Optional<ItemJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<ItemJpaEntity> findByCodeAndTenantIdAndCompanyId(String code, UUID tenantId, UUID companyId);
    List<ItemJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ItemJpaEntity> findByIdIn(List<UUID> ids);
}
