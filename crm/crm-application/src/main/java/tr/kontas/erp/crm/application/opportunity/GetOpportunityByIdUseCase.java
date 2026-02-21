package tr.kontas.erp.crm.application.opportunity;

import tr.kontas.erp.crm.domain.opportunity.Opportunity;
import tr.kontas.erp.crm.domain.opportunity.OpportunityId;

public interface GetOpportunityByIdUseCase {
    Opportunity execute(OpportunityId id);
}

