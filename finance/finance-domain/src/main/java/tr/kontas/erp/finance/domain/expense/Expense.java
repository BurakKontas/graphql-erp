package tr.kontas.erp.finance.domain.expense;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.exception.InvalidExpenseStateException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class Expense extends AggregateRoot<ExpenseId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String expenseNumber;
    private final String description;
    private final String category;
    private final String submittedBy;
    private final LocalDate expenseDate;
    private final BigDecimal amount;
    private final String currencyCode;
    private ExpenseStatus status;
    private String approvedBy;
    private final String receiptReference;
    private final String expenseAccountId;

    public Expense(ExpenseId id,
                   TenantId tenantId,
                   CompanyId companyId,
                   String expenseNumber,
                   String description,
                   String category,
                   String submittedBy,
                   LocalDate expenseDate,
                   BigDecimal amount,
                   String currencyCode,
                   ExpenseStatus status,
                   String approvedBy,
                   String receiptReference,
                   String expenseAccountId) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("description cannot be blank");
        if (expenseDate == null) throw new IllegalArgumentException("expenseDate cannot be null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("amount must be positive");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.expenseNumber = expenseNumber;
        this.description = description;
        this.category = category;
        this.submittedBy = submittedBy;
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.status = status != null ? status : ExpenseStatus.DRAFT;
        this.approvedBy = approvedBy;
        this.receiptReference = receiptReference;
        this.expenseAccountId = expenseAccountId;
    }

    public void submit() {
        if (status != ExpenseStatus.DRAFT) {
            throw new InvalidExpenseStateException(status.name(), "submit");
        }
        this.status = ExpenseStatus.SUBMITTED;
    }

    public void approve(String approverId) {
        if (status != ExpenseStatus.SUBMITTED) {
            throw new InvalidExpenseStateException(status.name(), "approve");
        }
        this.approvedBy = approverId;
        this.status = ExpenseStatus.APPROVED;
    }

    public void reject(String reason) {
        if (status != ExpenseStatus.SUBMITTED) {
            throw new InvalidExpenseStateException(status.name(), "reject");
        }
        this.status = ExpenseStatus.REJECTED;
    }

    public void post() {
        if (status != ExpenseStatus.APPROVED) {
            throw new InvalidExpenseStateException(status.name(), "post");
        }
        this.status = ExpenseStatus.POSTED;
    }

    public void cancel() {
        if (status != ExpenseStatus.DRAFT && status != ExpenseStatus.REJECTED) {
            throw new InvalidExpenseStateException(status.name(), "cancel");
        }
        this.status = ExpenseStatus.CANCELLED;
    }
}

