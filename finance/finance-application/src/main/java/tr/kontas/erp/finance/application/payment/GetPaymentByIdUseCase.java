package tr.kontas.erp.finance.application.payment;

import tr.kontas.erp.finance.domain.payment.Payment;
import tr.kontas.erp.finance.domain.payment.PaymentId;

public interface GetPaymentByIdUseCase {
    Payment execute(PaymentId id);
}

