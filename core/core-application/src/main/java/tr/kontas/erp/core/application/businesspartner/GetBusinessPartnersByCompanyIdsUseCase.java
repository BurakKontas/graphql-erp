package tr.kontas.erp.core.application.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;
import java.util.Map;

public interface GetBusinessPartnersByCompanyIdsUseCase {
    Map<CompanyId, List<BusinessPartner>> executeByCompanyIds(List<CompanyId> ids);
}
