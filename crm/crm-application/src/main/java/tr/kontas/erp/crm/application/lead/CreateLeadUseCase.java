package tr.kontas.erp.crm.application.lead;

import tr.kontas.erp.crm.domain.lead.LeadId;

public interface CreateLeadUseCase {
    LeadId execute(CreateLeadCommand command);
}

