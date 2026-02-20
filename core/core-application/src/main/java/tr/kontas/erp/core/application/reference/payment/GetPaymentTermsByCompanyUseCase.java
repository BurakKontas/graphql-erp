package tr.kontas.erp.core.application.reference.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;

import java.util.List;

public interface GetPaymentTermsByCompanyUseCase {
    List<PaymentTerm> execute(CompanyId companyId);
}
