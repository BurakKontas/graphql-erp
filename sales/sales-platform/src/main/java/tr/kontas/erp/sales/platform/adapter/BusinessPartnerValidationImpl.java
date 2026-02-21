package tr.kontas.erp.sales.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerRepository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.application.port.BusinessPartnerValidationPort;

@Component
@RequiredArgsConstructor
public class BusinessPartnerValidationImpl implements BusinessPartnerValidationPort {

    private final BusinessPartnerRepository businessPartnerRepository;

    @Override
    public void validateCustomerExists(TenantId tenantId, CompanyId companyId, BusinessPartnerId customerId) {
        BusinessPartner bp = businessPartnerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "BusinessPartner not found: " + customerId.asUUID()));

        if (!bp.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException(
                    "BusinessPartner does not belong to current tenant: " + customerId.asUUID());
        }

        if (!bp.isActive()) {
            throw new IllegalArgumentException(
                    "BusinessPartner is not active: " + customerId.asUUID());
        }
    }

    @Override
    public boolean exists(TenantId tenantId, BusinessPartnerId customerId) {
        return businessPartnerRepository.findById(customerId)
                .filter(bp -> bp.getTenantId().equals(tenantId))
                .filter(BusinessPartner::isActive)
                .isPresent();
    }
}
