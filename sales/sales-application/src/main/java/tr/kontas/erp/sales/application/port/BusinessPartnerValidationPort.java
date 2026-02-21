package tr.kontas.erp.sales.application.port;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface BusinessPartnerValidationPort {
    void validateCustomerExists(TenantId tenantId, CompanyId companyId, BusinessPartnerId customerId);

    boolean exists(TenantId tenantId, BusinessPartnerId customerId);
}
