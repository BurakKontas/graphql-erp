package tr.kontas.erp.crm.application.lead;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.crm.domain.lead.Lead;

import java.util.List;

public interface GetLeadsByCompanyUseCase {
    List<Lead> execute(CompanyId companyId);
}

