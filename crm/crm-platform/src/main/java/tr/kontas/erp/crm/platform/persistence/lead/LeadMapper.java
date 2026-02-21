package tr.kontas.erp.crm.platform.persistence.lead;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.lead.*;

public class LeadMapper {

    public static LeadJpaEntity toEntity(Lead l) {
        LeadJpaEntity e = new LeadJpaEntity();
        e.setId(l.getId().asUUID());
        e.setTenantId(l.getTenantId().asUUID());
        e.setCompanyId(l.getCompanyId().asUUID());
        e.setLeadNumber(l.getLeadNumber().getValue());
        e.setTitle(l.getTitle());
        e.setContactId(l.getContactId());
        e.setOwnerId(l.getOwnerId());
        e.setStatus(l.getStatus().name());
        e.setSource(l.getSource() != null ? l.getSource().name() : null);
        e.setEstimatedValue(l.getEstimatedValue());
        e.setDisqualificationReason(l.getDisqualificationReason());
        e.setNotes(l.getNotes());
        e.setExpectedCloseDate(l.getExpectedCloseDate());
        e.setOpportunityId(l.getOpportunityId());
        return e;
    }

    public static Lead toDomain(LeadJpaEntity e) {
        return new Lead(
                LeadId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new LeadNumber(e.getLeadNumber()),
                e.getTitle(), e.getContactId(), e.getOwnerId(),
                LeadStatus.valueOf(e.getStatus()),
                e.getSource() != null ? LeadSource.valueOf(e.getSource()) : null,
                e.getEstimatedValue(), e.getDisqualificationReason(),
                e.getNotes(), e.getExpectedCloseDate(), e.getOpportunityId()
        );
    }
}

