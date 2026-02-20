package tr.kontas.erp.core.application.reference.payment;

import tr.kontas.erp.core.domain.reference.payment.PaymentTermCode;

public interface CreatePaymentTermUseCase {
    PaymentTermCode execute(CreatePaymentTermCommand command);
}
