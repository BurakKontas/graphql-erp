package tr.kontas.erp.core.application.reference.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;

import java.util.List;
import java.util.Map;

public interface GetPaymentTermsByCompanyIdsUseCase {
    Map<CompanyId, List<PaymentTerm>> executeByCompanyIds(List<CompanyId> companyIds);
}
