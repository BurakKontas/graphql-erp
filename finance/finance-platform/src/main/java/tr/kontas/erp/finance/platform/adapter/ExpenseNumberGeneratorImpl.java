package tr.kontas.erp.finance.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.application.port.ExpenseNumberGeneratorPort;
import tr.kontas.erp.finance.platform.persistence.expense.JpaExpenseRepository;

@Component
@RequiredArgsConstructor
public class ExpenseNumberGeneratorImpl implements ExpenseNumberGeneratorPort {
    private final JpaExpenseRepository jpa;

    @Override
    public String generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpa.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        return "EXP-%04d-%06d".formatted(year, nextSeq);
    }
}

