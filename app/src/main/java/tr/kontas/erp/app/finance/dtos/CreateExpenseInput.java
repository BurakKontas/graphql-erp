package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateExpenseInput {
    private String companyId;
    private String description;
    private String category;
    private String submittedBy;
    private String expenseDate;
    private BigDecimal amount;
    private String currencyCode;
    private String receiptReference;
    private String expenseAccountId;
}

