package tr.kontas.erp.finance.application.expense;

import tr.kontas.erp.finance.domain.expense.ExpenseId;

public interface CreateExpenseUseCase {
    ExpenseId execute(CreateExpenseCommand command);
}

