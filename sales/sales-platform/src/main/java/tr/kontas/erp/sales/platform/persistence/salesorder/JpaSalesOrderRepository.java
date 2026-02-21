package tr.kontas.erp.sales.platform.persistence.salesorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaSalesOrderRepository extends JpaRepository<SalesOrderJpaEntity, UUID> {
    Optional<SalesOrderJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<SalesOrderJpaEntity> findByOrderNumberAndTenantId(String orderNumber, UUID tenantId);

    boolean existsByOrderNumberAndTenantId(String orderNumber, UUID tenantId);

    List<SalesOrderJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);

    List<SalesOrderJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.orderNumber, 9) AS int)), 0) FROM SalesOrderJpaEntity e WHERE e.tenantId = :tenantId")
    int findMaxSequenceByTenantId(UUID tenantId);
}
