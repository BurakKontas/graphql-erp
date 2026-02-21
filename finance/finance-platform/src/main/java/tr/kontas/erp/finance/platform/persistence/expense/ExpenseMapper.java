package tr.kontas.erp.finance.platform.persistence.expense;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.expense.*;

public class ExpenseMapper {

    public static ExpenseJpaEntity toEntity(Expense d) {
        ExpenseJpaEntity e = new ExpenseJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setExpenseNumber(d.getExpenseNumber());
        e.setDescription(d.getDescription());
        e.setCategory(d.getCategory());
        e.setSubmittedBy(d.getSubmittedBy());
        e.setExpenseDate(d.getExpenseDate());
        e.setAmount(d.getAmount());
        e.setCurrencyCode(d.getCurrencyCode());
        e.setStatus(d.getStatus().name());
        e.setApprovedBy(d.getApprovedBy());
        e.setReceiptReference(d.getReceiptReference());
        e.setExpenseAccountId(d.getExpenseAccountId());
        return e;
    }

    public static Expense toDomain(ExpenseJpaEntity e) {
        return new Expense(
                ExpenseId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                e.getExpenseNumber(),
                e.getDescription(),
                e.getCategory(),
                e.getSubmittedBy(),
                e.getExpenseDate(),
                e.getAmount(),
                e.getCurrencyCode(),
                ExpenseStatus.valueOf(e.getStatus()),
                e.getApprovedBy(),
                e.getReceiptReference(),
                e.getExpenseAccountId()
        );
    }
}
