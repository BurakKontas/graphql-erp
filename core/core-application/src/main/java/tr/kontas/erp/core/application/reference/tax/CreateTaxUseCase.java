package tr.kontas.erp.core.application.reference.tax;

import tr.kontas.erp.core.domain.reference.tax.TaxCode;

public interface CreateTaxUseCase {
    TaxCode execute(CreateTaxCommand command);
}
