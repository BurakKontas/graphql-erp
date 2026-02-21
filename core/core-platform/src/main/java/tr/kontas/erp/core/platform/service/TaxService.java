package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.reference.tax.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.*;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.platform.persistence.reference.tax.TaxRepositoryImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaxService implements
        CreateTaxUseCase,
        GetTaxesByCompanyUseCase,
        GetTaxesByCompanyIdsUseCase,
        UpdateTaxRateUseCase {

    private final TaxRepositoryImpl taxRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public TaxCode execute(CreateTaxCommand command) {
        TenantId tenantId = TenantContext.get();
        TaxCode code = new TaxCode(command.code());

        if (taxRepository.findByCode(tenantId, command.companyId(), code).isPresent()) {
            throw new IllegalArgumentException("Tax with code " + command.code() + " already exists");
        }

        TaxType type = TaxType.valueOf(command.type());
        TaxRate rate = new TaxRate(command.rate());

        Tax tax = new Tax(
                code,
                tenantId,
                command.companyId(),
                command.name(),
                type,
                rate
        );

        taxRepository.save(tax);

        return code;
    }

    @Override
    @Transactional
    public void execute(UpdateTaxRateCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = CompanyId.of(command.companyId());
        TaxCode taxCode = new TaxCode(command.taxCode());

        Tax tax = taxRepository.findByCode(tenantId, companyId, taxCode)
                .orElseThrow(() -> new IllegalArgumentException("Tax not found: " + command.taxCode()));

        BigDecimal oldRate = tax.getRate().getValue();
        tax.updateRate(new TaxRate(command.newRate()));
        taxRepository.save(tax);

        // Publish event so sales module can react
        eventPublisher.publish(new TaxRateChangedEvent(
                tenantId.asUUID(),
                companyId.asUUID(),
                command.taxCode(),
                oldRate,
                command.newRate()
        ));
    }

    @Override
    public List<Tax> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return taxRepository.findByCompany(tenantId, companyId);
    }

    @Override
    @Transactional
    public Map<CompanyId, List<Tax>> executeByCompanyIds(List<CompanyId> companyIds) {
        TenantId tenantId = TenantContext.get();
        Map<CompanyId, List<Tax>> resultMap = new HashMap<>();

        List<Tax> taxes = taxRepository.findByCompanyIds(tenantId, companyIds);

        taxes.forEach(tax -> {
            resultMap.computeIfAbsent(tax.getCompanyId(), _ -> new ArrayList<>())
                    .add(tax);
        });

        return resultMap;
    }
}
