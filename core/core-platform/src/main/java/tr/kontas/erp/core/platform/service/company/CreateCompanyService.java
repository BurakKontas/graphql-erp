package tr.kontas.erp.core.platform.service.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.company.CreateCompanyCommand;
import tr.kontas.erp.core.application.company.CreateCompanyUseCase;
import tr.kontas.erp.core.domain.company.*;

@Service
@RequiredArgsConstructor
public class CreateCompanyService implements CreateCompanyUseCase {
    private final CompanyRepository companyRepository;

    @Override
    public CompanyId execute(CreateCompanyCommand command) {

        CompanyId companyId = CompanyId.newId();

        Company company = new Company(
                companyId,
                command.tenantId(),
                new CompanyName(command.name())
        );

        companyRepository.save(company);

        return companyId;
    }
}
