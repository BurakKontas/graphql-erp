package tr.kontas.erp.core.application.reference.tax;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;

import java.util.List;

public interface GetTaxesByCompanyUseCase {
    List<Tax> execute(CompanyId companyId);
}
