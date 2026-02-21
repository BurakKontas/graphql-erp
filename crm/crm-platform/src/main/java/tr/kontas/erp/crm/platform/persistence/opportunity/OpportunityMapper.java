package tr.kontas.erp.crm.platform.persistence.opportunity;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.opportunity.*;

public class OpportunityMapper {

    public static OpportunityJpaEntity toEntity(Opportunity o) {
        OpportunityJpaEntity e = new OpportunityJpaEntity();
        e.setId(o.getId().asUUID());
        e.setTenantId(o.getTenantId().asUUID());
        e.setCompanyId(o.getCompanyId().asUUID());
        e.setOpportunityNumber(o.getOpportunityNumber().getValue());
        e.setTitle(o.getTitle());
        e.setContactId(o.getContactId());
        e.setLeadId(o.getLeadId());
        e.setOwnerId(o.getOwnerId());
        e.setStage(o.getStage().name());
        e.setProbability(o.getProbability());
        e.setExpectedValue(o.getExpectedValue());
        e.setCurrencyCode(o.getCurrencyCode());
        e.setExpectedCloseDate(o.getExpectedCloseDate());
        e.setSalesOrderId(o.getSalesOrderId());
        e.setLostReason(o.getLostReason());
        e.setNotes(o.getNotes());
        return e;
    }

    public static Opportunity toDomain(OpportunityJpaEntity e) {
        return new Opportunity(
                OpportunityId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new OpportunityNumber(e.getOpportunityNumber()),
                e.getTitle(), e.getContactId(), e.getLeadId(), e.getOwnerId(),
                OpportunityStage.valueOf(e.getStage()),
                e.getProbability(), e.getExpectedValue(), e.getCurrencyCode(),
                e.getExpectedCloseDate(), e.getSalesOrderId(),
                e.getLostReason(), e.getNotes()
        );
    }
}

