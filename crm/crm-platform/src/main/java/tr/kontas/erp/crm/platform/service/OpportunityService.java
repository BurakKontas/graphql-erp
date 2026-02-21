package tr.kontas.erp.crm.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.crm.application.opportunity.*;
import tr.kontas.erp.crm.application.port.OpportunityNumberGeneratorPort;
import tr.kontas.erp.crm.domain.opportunity.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpportunityService implements CreateOpportunityUseCase, GetOpportunityByIdUseCase,
        GetOpportunitiesByCompanyUseCase, GetOpportunitiesByIdsUseCase {

    private final OpportunityRepository opportunityRepository;
    private final OpportunityNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public OpportunityId execute(CreateOpportunityCommand cmd) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = cmd.companyId();
        OpportunityNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());
        OpportunityId id = OpportunityId.newId();
        Opportunity opp = Opportunity.create(id, tenantId, companyId, number,
                cmd.title(), cmd.contactId(), cmd.leadId(), cmd.ownerId(),
                cmd.expectedValue(), cmd.currencyCode(),
                cmd.expectedCloseDate(), cmd.notes());
        opportunityRepository.save(opp);
        opp.getDomainEvents().forEach(eventPublisher::publish);
        opp.clearDomainEvents();
        return id;
    }

    @Override
    public Opportunity execute(OpportunityId id) {
        TenantId tenantId = TenantContext.get();
        return opportunityRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found: " + id));
    }

    @Override
    public List<Opportunity> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return opportunityRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<Opportunity> execute(List<OpportunityId> ids) {
        return opportunityRepository.findByIds(ids);
    }
}



