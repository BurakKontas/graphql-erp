package tr.kontas.erp.core.application.businesspartner;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerRole;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.Set;

public record CreateBusinessPartnerCommand(
        CompanyId companyId,
        String code,
        String name,
        Set<BusinessPartnerRole> roles,
        String taxNumber
) {
}
