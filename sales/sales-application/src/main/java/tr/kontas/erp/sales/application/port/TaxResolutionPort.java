package tr.kontas.erp.sales.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public interface TaxResolutionPort {
    Tax resolveTax(TenantId tenantId, CompanyId companyId, String taxCode);

    Currency resolveCurrency(String currencyCode);

    PaymentTerm resolvePaymentTerm(TenantId tenantId, CompanyId companyId, String paymentTermCode);
}
