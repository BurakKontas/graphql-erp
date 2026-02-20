package tr.kontas.erp.core.application.reference.payment;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreatePaymentTermCommand(
        CompanyId companyId,
        String code,
        String name,
        int dueDays
) {
}
