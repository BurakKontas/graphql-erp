package tr.kontas.erp.core.application.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;

import java.util.List;

public interface GetBusinessPartnersByIdsUseCase {
    List<BusinessPartner> execute(List<BusinessPartnerId> ids);
}
