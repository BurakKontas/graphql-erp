package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.tenant.*;
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
public class TenantService implements
        CreateTenantUseCase,
        GetTenantsUseCase,
        GetTenantByIdUseCase,
        UpdateTenantAuthModeUseCase,
        UpdateTenantOidcSettingsUseCase,
        UpdateTenantLdapSettingsUseCase {
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

    @Override
    @Transactional
    public void execute(UpdateTenantAuthModeCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + command.tenantId()));

        tenant.changeAuthMode(command.authMode());

        tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void execute(UpdateTenantOidcSettingsCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + command.tenantId()));

        tenant.updateOidcSettings(command.oidcSettings());

        tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void execute(UpdateTenantLdapSettingsCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + command.tenantId()));

        tenant.updateLdapSettings(command.ldapSettings());

        tenantRepository.save(tenant);
    }
}
