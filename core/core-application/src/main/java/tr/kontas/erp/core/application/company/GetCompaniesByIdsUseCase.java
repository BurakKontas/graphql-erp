package tr.kontas.erp.core.application.company;

import tr.kontas.erp.core.domain.company.Company;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;

public interface GetCompaniesByIdsUseCase {
    List<Company> execute(List<CompanyId> ids);
}
