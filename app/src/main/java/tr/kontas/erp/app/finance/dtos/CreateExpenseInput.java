package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreateExpenseInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = CreateExpenseInputValidator.class)
public class CreateExpenseInput implements Validatable {
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
