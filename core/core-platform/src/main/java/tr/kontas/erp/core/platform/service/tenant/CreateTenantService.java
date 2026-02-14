package tr.kontas.erp.core.platform.service.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.tenant.CreateTenantCommand;
import tr.kontas.erp.core.application.tenant.CreateTenantUseCase;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantCode;
import tr.kontas.erp.core.domain.tenant.TenantName;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.domain.service.TenantProvisioningService;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Service
@RequiredArgsConstructor
public class CreateTenantService implements CreateTenantUseCase {
    private final TenantRepository tenantRepository;
    private final TenantProvisioningService provisioningService;

    @Override
    public TenantId execute(CreateTenantCommand command) {
        TenantId tenantId = TenantId.newId();
        Tenant tenant = new Tenant(tenantId, new TenantName(command.name()), new TenantCode(command.code()));

        tenantRepository.save(tenant);

        provisioningService.provision(tenant);

        return tenantId;
    }
}
