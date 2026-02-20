package tr.kontas.erp.core.application.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;

public interface GetBusinessPartnersUseCase {
    List<BusinessPartner> execute(CompanyId companyId);
}
