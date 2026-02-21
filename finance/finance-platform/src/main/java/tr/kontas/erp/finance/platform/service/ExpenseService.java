package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.expense.*;
import tr.kontas.erp.finance.application.port.ExpenseNumberGeneratorPort;
import tr.kontas.erp.finance.domain.expense.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService implements
        CreateExpenseUseCase, GetExpenseByIdUseCase, GetExpensesByCompanyUseCase {

    private final ExpenseRepository expenseRepository;
    private final ExpenseNumberGeneratorPort numberGenerator;

    @Override
    @Transactional
    public ExpenseId execute(CreateExpenseCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LocalDate date = cmd.expenseDate() != null ? cmd.expenseDate() : LocalDate.now();
        String number = numberGenerator.generate(tenantId, cmd.companyId(), date.getYear());

        ExpenseId id = ExpenseId.newId();
        Expense expense = new Expense(
                id, tenantId, cmd.companyId(), number,
                cmd.description(), cmd.category(), cmd.submittedBy(), date,
                cmd.amount(), cmd.currencyCode(), ExpenseStatus.DRAFT, null,
                cmd.receiptReference(), cmd.expenseAccountId()
        );

        expenseRepository.save(expense);
        return id;
    }

    @Override
    public Expense execute(ExpenseId id) {
        TenantId tenantId = TenantContext.get();
        return expenseRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found: " + id));
    }

    @Override
    public List<Expense> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return expenseRepository.findByCompanyId(tenantId, companyId);
    }

    @Transactional
    public void submit(String expenseId) {
        Expense expense = loadExpense(expenseId);
        expense.submit();
        expenseRepository.save(expense);
    }

    @Transactional
    public void approve(String expenseId, String approverId) {
        Expense expense = loadExpense(expenseId);
        expense.approve(approverId);
        expenseRepository.save(expense);
    }

    @Transactional
    public void reject(String expenseId, String reason) {
        Expense expense = loadExpense(expenseId);
        expense.reject(reason);
        expenseRepository.save(expense);
    }

    @Transactional
    public void post(String expenseId) {
        Expense expense = loadExpense(expenseId);
        expense.post();
        expenseRepository.save(expense);
    }

    private Expense loadExpense(String expenseId) {
        TenantId tenantId = TenantContext.get();
        return expenseRepository.findById(ExpenseId.of(expenseId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found: " + expenseId));
    }
}
