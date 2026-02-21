package tr.kontas.erp.crm.application.opportunity;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.crm.domain.opportunity.Opportunity;

import java.util.List;

public interface GetOpportunitiesByCompanyUseCase {
    List<Opportunity> execute(CompanyId companyId);
}

