package tr.kontas.erp.sales.platform.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.application.port.ItemValidationPort;

@Slf4j
@Component
public class ItemValidationImpl implements ItemValidationPort {

    @Override
    public void validateItemExists(TenantId tenantId, CompanyId companyId, String itemId) {
        log.debug("ItemValidation skipped (no inventory module): itemId={}", itemId);
    }

    @Override
    public boolean exists(TenantId tenantId, CompanyId companyId, String itemId) {
        return true;
    }
}
