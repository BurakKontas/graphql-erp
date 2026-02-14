package tr.kontas.erp.core.application.company;


import tr.kontas.erp.core.domain.company.CompanyId;

public interface CreateCompanyUseCase {
    CompanyId execute(CreateCompanyCommand command);
}