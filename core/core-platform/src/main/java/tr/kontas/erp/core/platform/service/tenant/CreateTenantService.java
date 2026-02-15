package tr.kontas.erp.core.platform.service.tenant;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.tenant.CreateTenantCommand;
import tr.kontas.erp.core.application.tenant.CreateTenantUseCase;
import tr.kontas.erp.core.application.tenant.GetTenantByIdUseCase;
import tr.kontas.erp.core.application.tenant.GetTenantsUseCase;
import tr.kontas.erp.core.domain.service.TenantProvisioningService;
import tr.kontas.erp.core.domain.tenant.Tenant;
import tr.kontas.erp.core.domain.tenant.TenantCode;
import tr.kontas.erp.core.domain.tenant.TenantName;
import tr.kontas.erp.core.domain.tenant.TenantRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateTenantService implements
        CreateTenantUseCase,
        GetTenantsUseCase,
        GetTenantByIdUseCase {
    private final TenantRepository tenantRepository;
    private final TenantProvisioningService provisioningService;

    @Override
    @Transactional
    public TenantId execute(CreateTenantCommand command) {
        TenantId tenantId = TenantId.newId();
        Tenant tenant = new Tenant(tenantId, new TenantName(command.name()), new TenantCode(command.code()));

        tenantRepository.save(tenant);

        provisioningService.provision(tenant);

        return tenantId;
    }

    @Override
    public Optional<Tenant> execute(TenantId id) {
        return tenantRepository.findById(id);
    }

    @Override
    public List<Tenant> execute() {
        return tenantRepository.findAll();
    }
}
