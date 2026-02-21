package tr.kontas.erp.finance.domain.expense;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    void save(Expense expense);
    Optional<Expense> findById(ExpenseId id, TenantId tenantId);
    List<Expense> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Expense> findByIds(List<ExpenseId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}

