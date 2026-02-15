package tr.kontas.erp.core.application.company;

import tr.kontas.erp.core.domain.company.Company;

import java.util.List;

public interface GetCompaniesUseCase {
    List<Company> execute();
}

