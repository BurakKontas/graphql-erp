package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.creditnote.*;
import tr.kontas.erp.finance.application.port.CreditNoteNumberGeneratorPort;
import tr.kontas.erp.finance.domain.creditnote.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditNoteService implements
        CreateCreditNoteUseCase, GetCreditNoteByIdUseCase,
        GetCreditNotesByCompanyUseCase, PostCreditNoteUseCase, ApplyCreditNoteUseCase {

    private final CreditNoteRepository creditNoteRepository;
    private final CreditNoteNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public CreditNoteId execute(CreateCreditNoteCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LocalDate date = cmd.creditNoteDate() != null ? cmd.creditNoteDate() : LocalDate.now();
        String number = numberGenerator.generate(tenantId, cmd.companyId(), date.getYear());

        CreditNoteId id = CreditNoteId.newId();
        List<CreditNoteLine> lines = cmd.lines().stream()
                .map(l -> new CreditNoteLine(
                        CreditNoteLineId.newId(), id, l.itemId(), l.itemDescription(),
                        l.unitCode(), l.quantity(), l.unitPrice(), l.taxCode(), l.taxRate()
                )).toList();

        CreditNote creditNote = new CreditNote(id, tenantId, cmd.companyId(), number,
                cmd.invoiceId(), cmd.customerId(), date, cmd.currencyCode(),
                CreditNoteStatus.DRAFT, null, cmd.reason(), lines);

        creditNoteRepository.save(creditNote);
        return id;
    }

    @Override
    public CreditNote execute(CreditNoteId id) {
        TenantId tenantId = TenantContext.get();
        return creditNoteRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("CreditNote not found: " + id));
    }

    @Override
    public List<CreditNote> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return creditNoteRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    @Transactional
    public void execute(String creditNoteId) {
        CreditNote cn = loadCreditNote(creditNoteId);
        cn.post();
        saveAndPublish(cn);
    }

    @Override
    @Transactional
    public void execute(String creditNoteId, BigDecimal amount) {
        CreditNote cn = loadCreditNote(creditNoteId);
        cn.applyToInvoice(amount);
        creditNoteRepository.save(cn);
    }

    private CreditNote loadCreditNote(String creditNoteId) {
        TenantId tenantId = TenantContext.get();
        return creditNoteRepository.findById(CreditNoteId.of(creditNoteId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("CreditNote not found: " + creditNoteId));
    }

    private void saveAndPublish(CreditNote cn) {
        creditNoteRepository.save(cn);
        eventPublisher.publishAll(cn.getDomainEvents());
        cn.clearDomainEvents();
    }
}
