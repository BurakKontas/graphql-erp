package tr.kontas.erp.finance.platform.persistence.accountingperiod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAccountingPeriodRepository extends JpaRepository<AccountingPeriodJpaEntity, UUID> {
    Optional<AccountingPeriodJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<AccountingPeriodJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<AccountingPeriodJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT p FROM AccountingPeriodJpaEntity p WHERE p.tenantId = :tenantId AND p.companyId = :companyId AND p.startDate <= :date AND p.endDate >= :date")
    Optional<AccountingPeriodJpaEntity> findByDate(@Param("tenantId") UUID tenantId, @Param("companyId") UUID companyId, @Param("date") LocalDate date);
}

