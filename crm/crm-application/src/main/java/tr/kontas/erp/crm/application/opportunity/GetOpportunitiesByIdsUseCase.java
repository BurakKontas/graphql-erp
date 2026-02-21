package tr.kontas.erp.crm.application.opportunity;

import tr.kontas.erp.crm.domain.opportunity.Opportunity;
import tr.kontas.erp.crm.domain.opportunity.OpportunityId;

import java.util.List;

public interface GetOpportunitiesByIdsUseCase {
    List<Opportunity> execute(List<OpportunityId> ids);
}

