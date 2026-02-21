package tr.kontas.erp.sales.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;
import tr.kontas.erp.core.domain.reference.currency.CurrencyRepository;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermCode;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermRepository;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxCode;
import tr.kontas.erp.core.domain.reference.tax.TaxRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.application.port.TaxResolutionPort;

@Component
@RequiredArgsConstructor
public class TaxResolutionImpl implements TaxResolutionPort {

    private final TaxRepository taxRepository;
    private final CurrencyRepository currencyRepository;
    private final PaymentTermRepository paymentTermRepository;

    @Override
    public Tax resolveTax(TenantId tenantId, CompanyId companyId, String taxCode) {
        return taxRepository.findByCode(tenantId, companyId, new TaxCode(taxCode))
                .orElseThrow(() -> new IllegalArgumentException("Tax not found: " + taxCode));
    }

    @Override
    public Currency resolveCurrency(String currencyCode) {
        return currencyRepository.findByCode(new CurrencyCode(currencyCode))
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + currencyCode));
    }

    @Override
    public PaymentTerm resolvePaymentTerm(TenantId tenantId, CompanyId companyId, String paymentTermCode) {
        return paymentTermRepository.findByCode(tenantId, companyId, new PaymentTermCode(paymentTermCode))
                .orElseThrow(() -> new IllegalArgumentException("PaymentTerm not found: " + paymentTermCode));
    }
}
