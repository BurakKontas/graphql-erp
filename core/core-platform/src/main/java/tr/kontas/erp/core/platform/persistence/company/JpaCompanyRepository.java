package tr.kontas.erp.core.platform.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface JpaCompanyRepository extends JpaRepository<CompanyJpaEntity, UUID> {
    List<CompanyJpaEntity> findByTenantId(UUID tenantId);

    List<CompanyJpaEntity> findByIdIn(Collection<UUID> ids);

    List<CompanyJpaEntity> findByTenantIdIn(Collection<UUID> tenantIds);
}
