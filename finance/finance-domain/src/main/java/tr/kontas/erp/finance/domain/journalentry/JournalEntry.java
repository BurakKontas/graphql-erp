package tr.kontas.erp.finance.domain.journalentry;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriodId;
import tr.kontas.erp.finance.domain.event.JournalEntryPostedEvent;
import tr.kontas.erp.finance.domain.event.JournalEntryReversedEvent;
import tr.kontas.erp.finance.domain.exception.InvalidJournalEntryStateException;
import tr.kontas.erp.finance.domain.exception.UnbalancedJournalEntryException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class JournalEntry extends AggregateRoot<JournalEntryId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final JournalEntryNumber entryNumber;
    private final JournalEntryType type;
    private final AccountingPeriodId periodId;
    private final LocalDate entryDate;
    private final String description;
    private final String referenceType;
    private final String referenceId;
    private final String currencyCode;
    private final BigDecimal exchangeRate;
    private JournalEntryStatus status;
    private final List<JournalEntryLine> lines;

    public JournalEntry(JournalEntryId id,
                        TenantId tenantId,
                        CompanyId companyId,
                        JournalEntryNumber entryNumber,
                        JournalEntryType type,
                        AccountingPeriodId periodId,
                        LocalDate entryDate,
                        String description,
                        String referenceType,
                        String referenceId,
                        String currencyCode,
                        BigDecimal exchangeRate,
                        JournalEntryStatus status,
                        List<JournalEntryLine> lines) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (entryNumber == null) throw new IllegalArgumentException("entryNumber cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (entryDate == null) throw new IllegalArgumentException("entryDate cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.entryNumber = entryNumber;
        this.type = type;
        this.periodId = periodId;
        this.entryDate = entryDate;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate != null ? exchangeRate : BigDecimal.ONE;
        this.status = status != null ? status : JournalEntryStatus.DRAFT;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }

    public static JournalEntry create(JournalEntryId id,
                                      TenantId tenantId,
                                      CompanyId companyId,
                                      JournalEntryNumber entryNumber,
                                      JournalEntryType type,
                                      AccountingPeriodId periodId,
                                      LocalDate entryDate,
                                      String description,
                                      String referenceType,
                                      String referenceId,
                                      String currencyCode,
                                      BigDecimal exchangeRate,
                                      List<JournalEntryLine> lines) {

        JournalEntry entry = new JournalEntry(id, tenantId, companyId, entryNumber, type,
                periodId, entryDate, description, referenceType, referenceId,
                currencyCode, exchangeRate, JournalEntryStatus.DRAFT, lines);

        entry.validateBalance();
        return entry;
    }

    public void post() {
        if (status != JournalEntryStatus.DRAFT) {
            throw new InvalidJournalEntryStateException(status.name(), "post");
        }
        if (lines.size() < 2) {
            throw new UnbalancedJournalEntryException("Journal entry must have at least 2 lines");
        }
        validateBalance();
        this.status = JournalEntryStatus.POSTED;

        registerEvent(new JournalEntryPostedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                entryNumber.getValue(), type.name(), referenceType, referenceId,
                entryDate, getTotalDebit()
        ));
    }

    public void markReversed() {
        if (status != JournalEntryStatus.POSTED) {
            throw new InvalidJournalEntryStateException(status.name(), "reverse");
        }
        this.status = JournalEntryStatus.REVERSED;

        registerEvent(new JournalEntryReversedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                entryNumber.getValue(), referenceType, referenceId
        ));
    }

    public BigDecimal getTotalDebit() {
        return lines.stream()
                .map(JournalEntryLine::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCredit() {
        return lines.stream()
                .map(JournalEntryLine::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<JournalEntryLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private void validateBalance() {
        BigDecimal totalDebit = getTotalDebit();
        BigDecimal totalCredit = getTotalCredit();

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new UnbalancedJournalEntryException(
                    "Journal entry is unbalanced: debit=%s, credit=%s".formatted(totalDebit, totalCredit));
        }
    }
}

