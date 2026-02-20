package tr.kontas.erp.core.application.reference.tax;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;

public record CreateTaxCommand(
        CompanyId companyId,
        String code,
        String name,
        String type,
        BigDecimal rate
) {
}
