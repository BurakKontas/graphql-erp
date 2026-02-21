package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.journalentry.*;
import tr.kontas.erp.finance.application.port.JournalEntryNumberGeneratorPort;
import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriodId;
import tr.kontas.erp.finance.domain.journalentry.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalEntryService implements
        CreateJournalEntryUseCase, GetJournalEntryByIdUseCase,
        GetJournalEntriesByCompanyUseCase, PostJournalEntryUseCase,
        ReverseJournalEntryUseCase {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public JournalEntryId execute(CreateJournalEntryCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LocalDate entryDate = cmd.entryDate() != null ? cmd.entryDate() : LocalDate.now();
        String number = numberGenerator.generate(tenantId, cmd.companyId(), entryDate.getYear());

        JournalEntryId id = JournalEntryId.newId();

        List<JournalEntryLine> lines = cmd.lines().stream()
                .map(l -> new JournalEntryLine(
                        JournalEntryLineId.newId(), id,
                        l.accountId(), l.accountCode(), l.accountName(),
                        l.debitAmount(), l.creditAmount(), l.description()
                )).toList();

        JournalEntry entry = JournalEntry.create(id, tenantId, cmd.companyId(),
                new JournalEntryNumber(number), JournalEntryType.valueOf(cmd.type()),
                cmd.periodId() != null ? AccountingPeriodId.of(cmd.periodId()) : null,
                entryDate, cmd.description(), cmd.referenceType(), cmd.referenceId(),
                cmd.currencyCode(), cmd.exchangeRate(), lines);

        journalEntryRepository.save(entry);
        return id;
    }

    @Override
    public JournalEntry execute(JournalEntryId id) {
        TenantId tenantId = TenantContext.get();
        return journalEntryRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("JournalEntry not found: " + id));
    }

    @Override
    public List<JournalEntry> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return journalEntryRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    @Transactional
    public void execute(String entryId) {
        JournalEntry entry = loadEntry(entryId);
        entry.post();
        saveAndPublish(entry);
    }

    @Override
    @Transactional
    public JournalEntryId execute(String entryId, String reason) {
        TenantId tenantId = TenantContext.get();
        JournalEntry original = loadEntry(entryId);
        original.markReversed();
        saveAndPublish(original);

        String number = numberGenerator.generate(tenantId, original.getCompanyId(), LocalDate.now().getYear());

        JournalEntryId reversalId = JournalEntryId.newId();
        List<JournalEntryLine> reversedLines = original.getLines().stream()
                .map(l -> new JournalEntryLine(
                        JournalEntryLineId.newId(), reversalId,
                        l.getAccountId(), l.getAccountCode(), l.getAccountName(),
                        l.getCreditAmount(), l.getDebitAmount(),
                        "Reversal: " + (l.getDescription() != null ? l.getDescription() : "")
                )).toList();

        JournalEntry reversal = JournalEntry.create(reversalId, tenantId, original.getCompanyId(),
                new JournalEntryNumber(number), JournalEntryType.REVERSAL,
                original.getPeriodId(), LocalDate.now(),
                "Reversal of " + original.getEntryNumber().getValue() + ": " + reason,
                original.getReferenceType(), original.getReferenceId(),
                original.getCurrencyCode(), original.getExchangeRate(), reversedLines);

        reversal.post();
        saveAndPublish(reversal);
        return reversalId;
    }

    private JournalEntry loadEntry(String entryId) {
        TenantId tenantId = TenantContext.get();
        return journalEntryRepository.findById(JournalEntryId.of(entryId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("JournalEntry not found: " + entryId));
    }

    private void saveAndPublish(JournalEntry entry) {
        journalEntryRepository.save(entry);
        eventPublisher.publishAll(entry.getDomainEvents());
        entry.clearDomainEvents();
    }
}

