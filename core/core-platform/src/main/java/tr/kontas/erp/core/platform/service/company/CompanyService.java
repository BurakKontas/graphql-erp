package tr.kontas.erp.core.platform.service.company;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.company.*;
import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.company.CompanyName;
import tr.kontas.erp.core.domain.company.CompanyRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService implements
        CreateCompanyUseCase,
        GetCompanyByIdUseCase,
        GetCompaniesUseCase,
        GetCompaniesByIdsUseCase,
        GetCompaniesByTenantIdsUseCase {
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyId execute(CreateCompanyCommand command) {

        CompanyId companyId = CompanyId.newId();
        TenantId tenantId = TenantContext.get();

        Company company = new Company(
                companyId,
                tenantId,
                new CompanyName(command.name())
        );

        companyRepository.save(company);

        return companyId;
    }

    @Override
    public List<Company> execute() {
        return companyRepository.findAll();
    }

    @Override
    public Company execute(CompanyId id) {
        return companyRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<Company> execute(List<CompanyId> ids) {
        return companyRepository.findByCompanyIds(ids);
    }

    @Override
    public Map<TenantId, List<Company>> executeByTenantIds(List<TenantId> ids) {
        Map<TenantId, List<Company>> resultMap = new HashMap<>();

        Set<Company> companies = companyRepository.findByTenantIds(ids);

        companies.forEach(company -> {
            resultMap.computeIfAbsent(company.getTenantId(), _ -> new ArrayList<>())
                    .add(company);
        });

        return resultMap;
    }
}
