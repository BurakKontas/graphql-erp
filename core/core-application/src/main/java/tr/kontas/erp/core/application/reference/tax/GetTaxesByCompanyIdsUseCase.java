package tr.kontas.erp.core.application.reference.tax;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;

import java.util.List;
import java.util.Map;

public interface GetTaxesByCompanyIdsUseCase {
    Map<CompanyId, List<Tax>> executeByCompanyIds(List<CompanyId> companyIds);
}
