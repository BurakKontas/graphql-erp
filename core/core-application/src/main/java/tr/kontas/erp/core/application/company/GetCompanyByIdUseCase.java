package tr.kontas.erp.core.application.company;

import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.domain.company.CompanyId;

public interface GetCompanyByIdUseCase {
    Company execute(CompanyId id);
}

