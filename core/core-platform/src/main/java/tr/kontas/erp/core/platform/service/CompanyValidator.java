package tr.kontas.erp.core.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.company.CompanyRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

@Component
@RequiredArgsConstructor
public class CompanyValidator {

    private final CompanyRepository companyRepository;

    public void validateExistsForCurrentTenant(CompanyId companyId) {
        TenantId tenantId = TenantContext.get().getId();
        if (companyId == null) return;
        boolean ok = companyRepository.existsByIdAndTenant(companyId, tenantId);
        if (!ok) {
            throw new IllegalArgumentException("Company not found, not active or not in tenant: " + companyId.asUUID());
        }
    }
}
