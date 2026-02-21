package tr.kontas.erp.inventory.platform.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryJpaEntity, UUID> {
    Optional<CategoryJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<CategoryJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<CategoryJpaEntity> findByIdIn(List<UUID> ids);
}
