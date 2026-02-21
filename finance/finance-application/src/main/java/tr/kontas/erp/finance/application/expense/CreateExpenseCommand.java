package tr.kontas.erp.finance.application.expense;

import tr.kontas.erp.core.domain.company.CompanyId;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseCommand(
        CompanyId companyId,
        String description,
        String category,
        String submittedBy,
        LocalDate expenseDate,
        BigDecimal amount,
        String currencyCode,
        String receiptReference,
        String expenseAccountId
) {}

