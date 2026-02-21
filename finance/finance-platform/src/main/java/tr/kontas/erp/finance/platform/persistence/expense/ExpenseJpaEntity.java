package tr.kontas.erp.finance.platform.persistence.expense;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
public class ExpenseJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "expense_number", nullable = false, unique = true)
    private String expenseNumber;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "receipt_reference")
    private String receiptReference;

    @Column(name = "expense_account_id")
    private String expenseAccountId;
}
