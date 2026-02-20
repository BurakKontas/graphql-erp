package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.reference.tax.CreateTaxCommand;
import tr.kontas.erp.core.application.reference.tax.CreateTaxUseCase;
import tr.kontas.erp.core.application.reference.tax.GetTaxesByCompanyIdsUseCase;
import tr.kontas.erp.core.application.reference.tax.GetTaxesByCompanyUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxCode;
import tr.kontas.erp.core.domain.reference.tax.TaxRate;
import tr.kontas.erp.core.domain.reference.tax.TaxType;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.platform.persistence.reference.tax.TaxRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaxService implements
        CreateTaxUseCase,
        GetTaxesByCompanyUseCase,
        GetTaxesByCompanyIdsUseCase {

    private final TaxRepositoryImpl taxRepository;

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
