package tr.kontas.erp.crm.application.lead;

import tr.kontas.erp.crm.domain.lead.Lead;
import tr.kontas.erp.crm.domain.lead.LeadId;

public interface GetLeadByIdUseCase {
    Lead execute(LeadId id);
}

