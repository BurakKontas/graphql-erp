package tr.kontas.erp.crm.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.crm.application.port.QuoteNumberGeneratorPort;
import tr.kontas.erp.crm.application.quote.*;
import tr.kontas.erp.crm.domain.quote.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteService implements CreateQuoteUseCase, GetQuoteByIdUseCase,
        GetQuotesByCompanyUseCase {

    private final QuoteRepository quoteRepository;
    private final QuoteNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public QuoteId execute(CreateQuoteCommand cmd) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = cmd.companyId();
        QuoteNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());
        QuoteId id = QuoteId.newId();
        Quote quote = Quote.create(id, tenantId, companyId, number,
                cmd.opportunityId(), cmd.contactId(), cmd.ownerId(),
                cmd.quoteDate(), cmd.expiryDate(), cmd.currencyCode(),
                cmd.paymentTermCode(), cmd.discountRate(), cmd.notes());
        quoteRepository.save(quote);
        quote.getDomainEvents().forEach(eventPublisher::publish);
        quote.clearDomainEvents();
        return id;
    }

    @Override
    public Quote execute(QuoteId id) {
        TenantId tenantId = TenantContext.get();
        return quoteRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Quote not found: " + id));
    }

    @Override
    public List<Quote> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return quoteRepository.findByCompanyId(tenantId, companyId);
    }
}

