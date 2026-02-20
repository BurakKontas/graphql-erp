package tr.kontas.erp.core.application.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;

public interface CreateBusinessPartnerUseCase {
    BusinessPartnerId execute(CreateBusinessPartnerCommand command);
}
