package tr.kontas.erp.finance.application.expense;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.expense.Expense;
import java.util.List;

public interface GetExpensesByCompanyUseCase {
    List<Expense> execute(CompanyId companyId);
}

