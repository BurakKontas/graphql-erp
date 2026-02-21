package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.payment.*;
import tr.kontas.erp.finance.application.port.PaymentNumberGeneratorPort;
import tr.kontas.erp.finance.domain.payment.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService implements
        CreatePaymentUseCase, GetPaymentByIdUseCase, GetPaymentsByCompanyUseCase,
        PostPaymentUseCase, CancelPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public PaymentId execute(CreatePaymentCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LocalDate paymentDate = cmd.paymentDate() != null ? cmd.paymentDate() : LocalDate.now();
        String number = numberGenerator.generate(tenantId, cmd.companyId(), paymentDate.getYear());

        PaymentId id = PaymentId.newId();
        Payment payment = new Payment(id, tenantId, cmd.companyId(), number,
                PaymentType.valueOf(cmd.paymentType()), cmd.invoiceId(), cmd.customerId(),
                paymentDate, cmd.amount(), cmd.currencyCode(), cmd.exchangeRate(),
                PaymentMethod.valueOf(cmd.paymentMethod()), PaymentStatus.DRAFT,
                cmd.bankAccountRef(), cmd.referenceNumber());

        paymentRepository.save(payment);
        return id;
    }

    @Override
    public Payment execute(PaymentId id) {
        TenantId tenantId = TenantContext.get();
        return paymentRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));
    }

    @Override
    public List<Payment> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return paymentRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    @Transactional
    public void execute(String paymentId) {
        Payment payment = loadPayment(paymentId);
        payment.post();
        saveAndPublish(payment);
    }

    @Override
    @Transactional
    public void execute(String paymentId, String reason) {
        Payment payment = loadPayment(paymentId);
        payment.cancel(reason);
        paymentRepository.save(payment);
    }

    private Payment loadPayment(String paymentId) {
        TenantId tenantId = TenantContext.get();
        return paymentRepository.findById(PaymentId.of(paymentId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
    }

    private void saveAndPublish(Payment payment) {
        paymentRepository.save(payment);
        eventPublisher.publishAll(payment.getDomainEvents());
        payment.clearDomainEvents();
    }
}
