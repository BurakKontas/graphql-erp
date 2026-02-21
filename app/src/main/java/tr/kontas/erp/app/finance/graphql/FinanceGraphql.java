package tr.kontas.erp.app.finance.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.finance.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.application.account.*;
import tr.kontas.erp.finance.application.accountingperiod.*;
import tr.kontas.erp.finance.application.creditnote.*;
import tr.kontas.erp.finance.application.expense.*;
import tr.kontas.erp.finance.application.journalentry.*;
import tr.kontas.erp.finance.application.payment.*;
import tr.kontas.erp.finance.application.salesinvoice.*;
import tr.kontas.erp.finance.domain.account.*;
import tr.kontas.erp.finance.domain.accountingperiod.*;
import tr.kontas.erp.finance.domain.creditnote.*;
import tr.kontas.erp.finance.domain.expense.*;
import tr.kontas.erp.finance.domain.journalentry.*;
import tr.kontas.erp.finance.domain.payment.*;
import tr.kontas.erp.finance.domain.salesinvoice.*;
import tr.kontas.erp.finance.platform.service.AccountingPeriodService;
import tr.kontas.erp.finance.platform.service.ExpenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class FinanceGraphql {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final GetAccountsByCompanyUseCase getAccountsByCompanyUseCase;
    private final CreateAccountingPeriodUseCase createAccountingPeriodUseCase;
    private final GetAccountingPeriodByIdUseCase getAccountingPeriodByIdUseCase;
    private final GetAccountingPeriodsByCompanyUseCase getAccountingPeriodsByCompanyUseCase;
    private final AccountingPeriodService accountingPeriodService;
    private final CreateJournalEntryUseCase createJournalEntryUseCase;
    private final GetJournalEntryByIdUseCase getJournalEntryByIdUseCase;
    private final GetJournalEntriesByCompanyUseCase getJournalEntriesByCompanyUseCase;
    private final PostJournalEntryUseCase postJournalEntryUseCase;
    private final ReverseJournalEntryUseCase reverseJournalEntryUseCase;
    private final CreateSalesInvoiceUseCase createSalesInvoiceUseCase;
    private final GetSalesInvoiceByIdUseCase getSalesInvoiceByIdUseCase;
    private final GetSalesInvoicesByCompanyUseCase getSalesInvoicesByCompanyUseCase;
    private final PostSalesInvoiceUseCase postSalesInvoiceUseCase;
    private final CancelSalesInvoiceUseCase cancelSalesInvoiceUseCase;
    private final CreatePaymentUseCase createPaymentUseCase;
    private final GetPaymentByIdUseCase getPaymentByIdUseCase;
    private final GetPaymentsByCompanyUseCase getPaymentsByCompanyUseCase;
    private final PostPaymentUseCase postPaymentUseCase;
    private final CancelPaymentUseCase cancelPaymentUseCase;
    private final CreateCreditNoteUseCase createCreditNoteUseCase;
    private final GetCreditNoteByIdUseCase getCreditNoteByIdUseCase;
    private final GetCreditNotesByCompanyUseCase getCreditNotesByCompanyUseCase;
    private final PostCreditNoteUseCase postCreditNoteUseCase;
    private final ApplyCreditNoteUseCase applyCreditNoteUseCase;
    private final CreateExpenseUseCase createExpenseUseCase;
    private final GetExpenseByIdUseCase getExpenseByIdUseCase;
    private final GetExpensesByCompanyUseCase getExpensesByCompanyUseCase;
    private final ExpenseService expenseService;

    public static AccountPayload toAccountPayload(Account a) {
        return new AccountPayload(
                a.getId().asUUID().toString(),
                a.getCompanyId().asUUID().toString(),
                a.getCode(),
                a.getName(),
                a.getType().name(),
                a.getNature().name(),
                a.getParentAccountId() != null ? a.getParentAccountId().asUUID().toString() : null,
                a.isSystemAccount(),
                a.isActive()
        );
    }

    public static AccountingPeriodPayload toPeriodPayload(AccountingPeriod p) {
        return new AccountingPeriodPayload(
                p.getId().asUUID().toString(),
                p.getCompanyId().asUUID().toString(),
                p.getPeriodType().name(),
                p.getStartDate().toString(),
                p.getEndDate().toString(),
                p.getStatus().name()
        );
    }

    public static JournalEntryLinePayload toJeLinePayload(JournalEntryLine l) {
        return new JournalEntryLinePayload(
                l.getId().asUUID().toString(),
                l.getAccountId(),
                l.getAccountCode(),
                l.getAccountName(),
                l.getDebitAmount(),
                l.getCreditAmount(),
                l.getDescription()
        );
    }

    public static JournalEntryPayload toJePayload(JournalEntry e) {
        List<JournalEntryLinePayload> lines = e.getLines().stream()
                .map(FinanceGraphql::toJeLinePayload)
                .collect(Collectors.toList());

        return new JournalEntryPayload(
                e.getId().asUUID().toString(),
                e.getCompanyId().asUUID().toString(),
                e.getEntryNumber().getValue(),
                e.getType().name(),
                e.getPeriodId() != null ? e.getPeriodId().asUUID().toString() : null,
                e.getEntryDate().toString(),
                e.getDescription(),
                e.getReferenceType(),
                e.getReferenceId(),
                e.getCurrencyCode(),
                e.getExchangeRate(),
                e.getStatus().name(),
                e.getTotalDebit(),
                e.getTotalCredit(),
                lines
        );
    }

    public static SalesInvoiceLinePayload toInvLinePayload(SalesInvoiceLine l) {
        return new SalesInvoiceLinePayload(
                l.getId().asUUID().toString(),
                l.getSalesOrderLineId(),
                l.getItemId(),
                l.getItemDescription(),
                l.getUnitCode(),
                l.getQuantity(),
                l.getUnitPrice(),
                l.getTaxCode(),
                l.getTaxRate(),
                l.getLineTotal(),
                l.getTaxAmount(),
                l.getLineTotalWithTax(),
                l.getRevenueAccountId()
        );
    }

    public static SalesInvoicePayload toInvoicePayload(SalesInvoice inv) {
        List<SalesInvoiceLinePayload> lines = inv.getLines().stream()
                .map(FinanceGraphql::toInvLinePayload)
                .collect(Collectors.toList());

        return new SalesInvoicePayload(
                inv.getId().asUUID().toString(),
                inv.getCompanyId().asUUID().toString(),
                inv.getInvoiceNumber().getValue(),
                inv.getInvoiceType().name(),
                inv.getSalesOrderId(),
                inv.getSalesOrderNumber(),
                inv.getCustomerId(),
                inv.getCustomerName(),
                inv.getInvoiceDate().toString(),
                inv.getDueDate() != null ? inv.getDueDate().toString() : null,
                inv.getCurrencyCode(),
                inv.getExchangeRate(),
                inv.getStatus().name(),
                inv.getSubtotal(),
                inv.getTaxTotal(),
                inv.getTotal(),
                inv.getPaidAmount(),
                inv.getRemainingAmount(),
                lines
        );
    }

    public static PaymentPayload toPaymentPayload(Payment p) {
        return new PaymentPayload(
                p.getId().asUUID().toString(),
                p.getCompanyId().asUUID().toString(),
                p.getPaymentNumber(),
                p.getPaymentType().name(),
                p.getInvoiceId(),
                p.getCustomerId(),
                p.getPaymentDate().toString(),
                p.getAmount(),
                p.getCurrencyCode(),
                p.getExchangeRate(),
                p.getPaymentMethod().name(),
                p.getStatus().name(),
                p.getBankAccountRef(),
                p.getReferenceNumber()
        );
    }

    public static CreditNoteLinePayload toCnLinePayload(CreditNoteLine l) {
        return new CreditNoteLinePayload(
                l.getId().asUUID().toString(),
                l.getItemId(),
                l.getItemDescription(),
                l.getUnitCode(),
                l.getQuantity(),
                l.getUnitPrice(),
                l.getTaxCode(),
                l.getTaxRate(),
                l.getLineTotal(),
                l.getTaxAmount(),
                l.getLineTotalWithTax()
        );
    }

    public static CreditNotePayload toCreditNotePayload(CreditNote cn) {
        List<CreditNoteLinePayload> lines = cn.getLines().stream()
                .map(FinanceGraphql::toCnLinePayload)
                .collect(Collectors.toList());

        return new CreditNotePayload(
                cn.getId().asUUID().toString(),
                cn.getCompanyId().asUUID().toString(),
                cn.getCreditNoteNumber(),
                cn.getInvoiceId(),
                cn.getCustomerId(),
                cn.getCreditNoteDate().toString(),
                cn.getCurrencyCode(),
                cn.getStatus().name(),
                cn.getTotal(),
                cn.getAppliedAmount(),
                cn.getReason(),
                lines
        );
    }

    public static ExpensePayload toExpensePayload(Expense e) {
        return new ExpensePayload(
                e.getId().asUUID().toString(),
                e.getCompanyId().asUUID().toString(),
                e.getExpenseNumber(),
                e.getDescription(),
                e.getCategory(),
                e.getSubmittedBy(),
                e.getExpenseDate().toString(),
                e.getAmount(),
                e.getCurrencyCode(),
                e.getStatus().name(),
                e.getApprovedBy(),
                e.getReceiptReference(),
                e.getExpenseAccountId()
        );
    }

    @DgsQuery
    public AccountPayload account(@InputArgument("id") String id) {
        return toAccountPayload(getAccountByIdUseCase.execute(AccountId.of(id)));
    }

    @DgsQuery
    public List<AccountPayload> accounts(@InputArgument("companyId") String companyId) {
        return getAccountsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toAccountPayload).toList();
    }

    @DgsMutation
    public AccountPayload createAccount(@InputArgument("input") CreateAccountInput input) {
        AccountId id = createAccountUseCase.execute(new CreateAccountCommand(
                CompanyId.of(input.getCompanyId()),
                input.getCode(),
                input.getName(),
                input.getType(),
                input.getNature(),
                input.getParentAccountId(),
                input.getSystemAccount() != null && input.getSystemAccount()
        ));
        return toAccountPayload(getAccountByIdUseCase.execute(id));
    }

    @DgsData(parentType = "AccountPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        AccountPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsQuery
    public AccountingPeriodPayload accountingPeriod(@InputArgument("id") String id) {
        return toPeriodPayload(getAccountingPeriodByIdUseCase.execute(AccountingPeriodId.of(id)));
    }

    @DgsQuery
    public List<AccountingPeriodPayload> accountingPeriods(@InputArgument("companyId") String companyId) {
        return getAccountingPeriodsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toPeriodPayload).toList();
    }

    @DgsMutation
    public AccountingPeriodPayload createAccountingPeriod(@InputArgument("input") CreateAccountingPeriodInput input) {
        AccountingPeriodId id = createAccountingPeriodUseCase.execute(new CreateAccountingPeriodCommand(
                CompanyId.of(input.getCompanyId()),
                input.getPeriodType(),
                LocalDate.parse(input.getStartDate()),
                LocalDate.parse(input.getEndDate())
        ));
        return toPeriodPayload(getAccountingPeriodByIdUseCase.execute(id));
    }

    @DgsMutation
    public AccountingPeriodPayload softCloseAccountingPeriod(@InputArgument("periodId") String periodId) {
        accountingPeriodService.softClose(periodId);
        return toPeriodPayload(getAccountingPeriodByIdUseCase.execute(AccountingPeriodId.of(periodId)));
    }

    @DgsMutation
    public AccountingPeriodPayload hardCloseAccountingPeriod(@InputArgument("periodId") String periodId) {
        accountingPeriodService.hardClose(periodId);
        return toPeriodPayload(getAccountingPeriodByIdUseCase.execute(AccountingPeriodId.of(periodId)));
    }

    @DgsMutation
    public AccountingPeriodPayload reopenAccountingPeriod(@InputArgument("periodId") String periodId) {
        accountingPeriodService.reopen(periodId);
        return toPeriodPayload(getAccountingPeriodByIdUseCase.execute(AccountingPeriodId.of(periodId)));
    }

    @DgsData(parentType = "AccountingPeriodPayload")
    public CompletableFuture<CompanyPayload> periodCompany(DgsDataFetchingEnvironment dfe) {
        AccountingPeriodPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsQuery
    public JournalEntryPayload journalEntry(@InputArgument("id") String id) {
        return toJePayload(getJournalEntryByIdUseCase.execute(JournalEntryId.of(id)));
    }

    @DgsQuery
    public List<JournalEntryPayload> journalEntries(@InputArgument("companyId") String companyId) {
        return getJournalEntriesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toJePayload).toList();
    }

    @DgsMutation
    public JournalEntryPayload createJournalEntry(@InputArgument("input") CreateJournalEntryInput input) {
        List<CreateJournalEntryCommand.LineInput> lines = input.getLines().stream().map(l ->
                new CreateJournalEntryCommand.LineInput(
                        l.getAccountId(),
                        l.getAccountCode(),
                        l.getAccountName(),
                        l.getDebitAmount(),
                        l.getCreditAmount(),
                        l.getDescription()
                )).toList();

        JournalEntryId id = createJournalEntryUseCase.execute(new CreateJournalEntryCommand(
                CompanyId.of(input.getCompanyId()),
                input.getType(),
                input.getPeriodId(),
                LocalDate.parse(input.getEntryDate()),
                input.getDescription(),
                input.getReferenceType(),
                input.getReferenceId(),
                input.getCurrencyCode(),
                input.getExchangeRate(),
                lines
        ));
        return toJePayload(getJournalEntryByIdUseCase.execute(id));
    }

    @DgsMutation
    public JournalEntryPayload postJournalEntry(@InputArgument("entryId") String entryId) {
        postJournalEntryUseCase.execute(entryId);
        return toJePayload(getJournalEntryByIdUseCase.execute(JournalEntryId.of(entryId)));
    }

    @DgsMutation
    public JournalEntryPayload reverseJournalEntry(@InputArgument("entryId") String entryId,
                                                    @InputArgument("reason") String reason) {
        JournalEntryId reversalId = reverseJournalEntryUseCase.execute(entryId, reason);
        return toJePayload(getJournalEntryByIdUseCase.execute(reversalId));
    }

    @DgsData(parentType = "JournalEntryPayload")
    public CompletableFuture<CompanyPayload> jeCompany(DgsDataFetchingEnvironment dfe) {
        JournalEntryPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsQuery
    public SalesInvoicePayload salesInvoice(@InputArgument("id") String id) {
        return toInvoicePayload(getSalesInvoiceByIdUseCase.execute(SalesInvoiceId.of(id)));
    }

    @DgsQuery
    public List<SalesInvoicePayload> salesInvoices(@InputArgument("companyId") String companyId) {
        return getSalesInvoicesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toInvoicePayload).toList();
    }

    @DgsMutation
    public SalesInvoicePayload createSalesInvoice(@InputArgument("input") CreateSalesInvoiceInput input) {
        List<CreateSalesInvoiceCommand.LineInput> lines = input.getLines().stream().map(l ->
                new CreateSalesInvoiceCommand.LineInput(
                        l.getSalesOrderLineId(),
                        l.getItemId(),
                        l.getItemDescription(),
                        l.getUnitCode(),
                        l.getQuantity(),
                        l.getUnitPrice(),
                        l.getTaxCode(),
                        l.getTaxRate(),
                        l.getRevenueAccountId()
                )).toList();

        SalesInvoiceId id = createSalesInvoiceUseCase.execute(new CreateSalesInvoiceCommand(
                CompanyId.of(input.getCompanyId()),
                input.getInvoiceType(),
                input.getSalesOrderId(),
                input.getSalesOrderNumber(),
                input.getCustomerId(),
                input.getCustomerName(),
                LocalDate.parse(input.getInvoiceDate()),
                input.getDueDate() != null ? LocalDate.parse(input.getDueDate()) : null,
                input.getCurrencyCode(),
                input.getExchangeRate(),
                lines
        ));
        return toInvoicePayload(getSalesInvoiceByIdUseCase.execute(id));
    }

    @DgsMutation
    public SalesInvoicePayload postSalesInvoice(@InputArgument("invoiceId") String invoiceId) {
        postSalesInvoiceUseCase.execute(invoiceId);
        return toInvoicePayload(getSalesInvoiceByIdUseCase.execute(SalesInvoiceId.of(invoiceId)));
    }

    @DgsMutation
    public SalesInvoicePayload cancelSalesInvoice(@InputArgument("invoiceId") String invoiceId,
                                                   @InputArgument("reason") String reason) {
        cancelSalesInvoiceUseCase.execute(invoiceId, reason);
        return toInvoicePayload(getSalesInvoiceByIdUseCase.execute(SalesInvoiceId.of(invoiceId)));
    }

    @DgsData(parentType = "SalesInvoicePayload")
    public CompletableFuture<CompanyPayload> invCompany(DgsDataFetchingEnvironment dfe) {
        SalesInvoicePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsQuery
    public PaymentPayload payment(@InputArgument("id") String id) {
        return toPaymentPayload(getPaymentByIdUseCase.execute(PaymentId.of(id)));
    }

    @DgsQuery
    public List<PaymentPayload> payments(@InputArgument("companyId") String companyId) {
        return getPaymentsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toPaymentPayload).toList();
    }

    @DgsMutation
    public PaymentPayload createPayment(@InputArgument("input") CreatePaymentInput input) {
        PaymentId id = createPaymentUseCase.execute(new CreatePaymentCommand(
                CompanyId.of(input.getCompanyId()),
                input.getPaymentType(),
                input.getInvoiceId(),
                input.getCustomerId(),
                LocalDate.parse(input.getPaymentDate()),
                input.getAmount(),
                input.getCurrencyCode(),
                input.getExchangeRate(),
                input.getPaymentMethod(),
                input.getBankAccountRef(),
                input.getReferenceNumber()
        ));
        return toPaymentPayload(getPaymentByIdUseCase.execute(id));
    }

    @DgsMutation
    public PaymentPayload postPayment(@InputArgument("paymentId") String paymentId) {
        postPaymentUseCase.execute(paymentId);
        return toPaymentPayload(getPaymentByIdUseCase.execute(PaymentId.of(paymentId)));
    }

    @DgsMutation
    public PaymentPayload cancelPayment(@InputArgument("paymentId") String paymentId,
                                         @InputArgument("reason") String reason) {
        cancelPaymentUseCase.execute(paymentId, reason);
        return toPaymentPayload(getPaymentByIdUseCase.execute(PaymentId.of(paymentId)));
    }

    @DgsData(parentType = "PaymentPayload")
    public CompletableFuture<CompanyPayload> payCompany(DgsDataFetchingEnvironment dfe) {
        PaymentPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsQuery
    public CreditNotePayload creditNote(@InputArgument("id") String id) {
        return toCreditNotePayload(getCreditNoteByIdUseCase.execute(CreditNoteId.of(id)));
    }

    @DgsQuery
    public List<CreditNotePayload> creditNotes(@InputArgument("companyId") String companyId) {
        return getCreditNotesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toCreditNotePayload).toList();
    }

    @DgsMutation
    public CreditNotePayload createCreditNote(@InputArgument("input") CreateCreditNoteInput input) {
        List<CreateCreditNoteCommand.LineInput> lines = input.getLines().stream().map(l ->
                new CreateCreditNoteCommand.LineInput(
                        l.getItemId(),
                        l.getItemDescription(),
                        l.getUnitCode(),
                        l.getQuantity(),
                        l.getUnitPrice(),
                        l.getTaxCode(),
                        l.getTaxRate()
                )).toList();

        CreditNoteId id = createCreditNoteUseCase.execute(new CreateCreditNoteCommand(
                CompanyId.of(input.getCompanyId()),
                input.getInvoiceId(),
                input.getCustomerId(),
                LocalDate.parse(input.getCreditNoteDate()),
                input.getCurrencyCode(),
                input.getReason(),
                lines
        ));
        return toCreditNotePayload(getCreditNoteByIdUseCase.execute(id));
    }

    @DgsMutation
    public CreditNotePayload postCreditNote(@InputArgument("creditNoteId") String creditNoteId) {
        postCreditNoteUseCase.execute(creditNoteId);
        return toCreditNotePayload(getCreditNoteByIdUseCase.execute(CreditNoteId.of(creditNoteId)));
    }

    @DgsMutation
    public CreditNotePayload applyCreditNote(@InputArgument("creditNoteId") String creditNoteId,
                                              @InputArgument("amount") BigDecimal amount) {
        applyCreditNoteUseCase.execute(creditNoteId, amount);
        return toCreditNotePayload(getCreditNoteByIdUseCase.execute(CreditNoteId.of(creditNoteId)));
    }

    @DgsData(parentType = "CreditNotePayload")
    public CompletableFuture<CompanyPayload> cnCompany(DgsDataFetchingEnvironment dfe) {
        CreditNotePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsQuery
    public ExpensePayload expense(@InputArgument("id") String id) {
        return toExpensePayload(getExpenseByIdUseCase.execute(ExpenseId.of(id)));
    }

    @DgsQuery
    public List<ExpensePayload> expenses(@InputArgument("companyId") String companyId) {
        return getExpensesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(FinanceGraphql::toExpensePayload).toList();
    }

    @DgsMutation
    public ExpensePayload createExpense(@InputArgument("input") CreateExpenseInput input) {
        ExpenseId id = createExpenseUseCase.execute(new CreateExpenseCommand(
                CompanyId.of(input.getCompanyId()),
                input.getDescription(),
                input.getCategory(),
                input.getSubmittedBy(),
                LocalDate.parse(input.getExpenseDate()),
                input.getAmount(),
                input.getCurrencyCode(),
                input.getReceiptReference(),
                input.getExpenseAccountId()
        ));
        return toExpensePayload(getExpenseByIdUseCase.execute(id));
    }

    @DgsMutation
    public ExpensePayload submitExpense(@InputArgument("expenseId") String expenseId) {
        expenseService.submit(expenseId);
        return toExpensePayload(getExpenseByIdUseCase.execute(ExpenseId.of(expenseId)));
    }

    @DgsMutation
    public ExpensePayload approveExpense(@InputArgument("expenseId") String expenseId,
                                          @InputArgument("approverId") String approverId) {
        expenseService.approve(expenseId, approverId);
        return toExpensePayload(getExpenseByIdUseCase.execute(ExpenseId.of(expenseId)));
    }

    @DgsMutation
    public ExpensePayload rejectExpense(@InputArgument("expenseId") String expenseId,
                                         @InputArgument("reason") String reason) {
        expenseService.reject(expenseId, reason);
        return toExpensePayload(getExpenseByIdUseCase.execute(ExpenseId.of(expenseId)));
    }

    @DgsMutation
    public ExpensePayload postExpense(@InputArgument("expenseId") String expenseId) {
        expenseService.post(expenseId);
        return toExpensePayload(getExpenseByIdUseCase.execute(ExpenseId.of(expenseId)));
    }

    @DgsData(parentType = "ExpensePayload")
    public CompletableFuture<CompanyPayload> expCompany(DgsDataFetchingEnvironment dfe) {
        ExpensePayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }
}

