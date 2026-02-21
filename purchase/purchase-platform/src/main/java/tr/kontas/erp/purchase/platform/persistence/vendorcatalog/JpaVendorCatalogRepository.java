package tr.kontas.erp.purchase.platform.persistence.vendorcatalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaVendorCatalogRepository extends JpaRepository<VendorCatalogJpaEntity, UUID> {
    Optional<VendorCatalogJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<VendorCatalogJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<VendorCatalogJpaEntity> findByIdIn(List<UUID> ids);
}

