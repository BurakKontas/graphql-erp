package tr.kontas.erp.finance.platform.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountJpaEntity, UUID> {
    Optional<AccountJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<AccountJpaEntity> findByTenantIdAndCompanyIdAndCode(UUID tenantId, UUID companyId, String code);
    List<AccountJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    boolean existsByTenantIdAndCompanyIdAndCode(UUID tenantId, UUID companyId, String code);
    List<AccountJpaEntity> findByIdIn(List<UUID> ids);
}

