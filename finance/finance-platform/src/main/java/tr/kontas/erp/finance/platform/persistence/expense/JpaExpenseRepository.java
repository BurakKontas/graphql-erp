package tr.kontas.erp.finance.platform.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaExpenseRepository extends JpaRepository<ExpenseJpaEntity, UUID> {
    Optional<ExpenseJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ExpenseJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ExpenseJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.expenseNumber, 10) AS int)), 0) FROM ExpenseJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

