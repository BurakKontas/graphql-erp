package tr.kontas.erp.crm.application.opportunity;

import tr.kontas.erp.crm.domain.opportunity.OpportunityId;

public interface CreateOpportunityUseCase {
    OpportunityId execute(CreateOpportunityCommand command);
}

