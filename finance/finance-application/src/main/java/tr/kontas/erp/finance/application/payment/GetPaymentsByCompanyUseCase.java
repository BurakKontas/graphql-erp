package tr.kontas.erp.finance.application.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.finance.domain.payment.Payment;
import java.util.List;

public interface GetPaymentsByCompanyUseCase {
    List<Payment> execute(CompanyId companyId);
}

