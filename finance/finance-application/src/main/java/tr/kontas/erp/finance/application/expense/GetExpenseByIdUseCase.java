package tr.kontas.erp.finance.application.expense;

import tr.kontas.erp.finance.domain.expense.Expense;
import tr.kontas.erp.finance.domain.expense.ExpenseId;

public interface GetExpenseByIdUseCase {
    Expense execute(ExpenseId id);
}

