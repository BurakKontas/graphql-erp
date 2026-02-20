package tr.kontas.erp.core.application.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;

public interface GetBusinessPartnerByIdUseCase {
    BusinessPartner execute(BusinessPartnerId id);
}
