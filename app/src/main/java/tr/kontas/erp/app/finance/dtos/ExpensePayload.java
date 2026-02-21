package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExpensePayload {
    private String id;
    private String companyId;
    private String expenseNumber;
    private String description;
    private String category;
    private String submittedBy;
    private String expenseDate;
    private BigDecimal amount;
    private String currencyCode;
    private String status;
    private String approvedBy;
    private String receiptReference;
    private String expenseAccountId;
}

