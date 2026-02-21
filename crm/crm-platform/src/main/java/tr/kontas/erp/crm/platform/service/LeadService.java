package tr.kontas.erp.crm.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.crm.application.lead.*;
import tr.kontas.erp.crm.application.port.LeadNumberGeneratorPort;
import tr.kontas.erp.crm.domain.lead.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService implements CreateLeadUseCase, GetLeadByIdUseCase,
        GetLeadsByCompanyUseCase {

    private final LeadRepository leadRepository;
    private final LeadNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public LeadId execute(CreateLeadCommand cmd) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = cmd.companyId();
        LeadNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());
        LeadId id = LeadId.newId();
        Lead lead = Lead.create(id, tenantId, companyId, number,
                cmd.title(), cmd.contactId(), cmd.ownerId(),
                cmd.source() != null ? LeadSource.valueOf(cmd.source()) : null,
                cmd.estimatedValue(), cmd.notes(), cmd.expectedCloseDate());
        leadRepository.save(lead);
        lead.getDomainEvents().forEach(eventPublisher::publish);
        lead.clearDomainEvents();
        return id;
    }

    @Override
    public Lead execute(LeadId id) {
        TenantId tenantId = TenantContext.get();
        return leadRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
    }

    @Override
    public List<Lead> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return leadRepository.findByCompanyId(tenantId, companyId);
    }
}

