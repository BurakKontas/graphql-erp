package tr.kontas.erp.finance.application.payment;

import tr.kontas.erp.finance.domain.payment.PaymentId;

public interface CreatePaymentUseCase {
    PaymentId execute(CreatePaymentCommand command);
}

