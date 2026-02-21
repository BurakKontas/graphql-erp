package tr.kontas.erp.finance.platform.persistence.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.expense.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepository {

    private final JpaExpenseRepository jpa;

    @Override
    public void save(Expense expense) {
        jpa.save(ExpenseMapper.toEntity(expense));
    }

    @Override
    public Optional<Expense> findById(ExpenseId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(ExpenseMapper::toDomain);
    }

    @Override
    public List<Expense> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(ExpenseMapper::toDomain).toList();
    }

    @Override
    public List<Expense> findByIds(List<ExpenseId> ids) {
        return jpa.findByIdIn(ids.stream().map(ExpenseId::asUUID).toList())
                .stream().map(ExpenseMapper::toDomain).toList();
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpa.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}
