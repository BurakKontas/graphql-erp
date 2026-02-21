package tr.kontas.erp.finance.platform.persistence.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.payment.*;

public class PaymentMapper {

    public static PaymentJpaEntity toEntity(Payment d) {
        PaymentJpaEntity e = new PaymentJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setPaymentNumber(d.getPaymentNumber());
        e.setPaymentType(d.getPaymentType().name());
        e.setInvoiceId(d.getInvoiceId());
        e.setCustomerId(d.getCustomerId());
        e.setPaymentDate(d.getPaymentDate());
        e.setAmount(d.getAmount());
        e.setCurrencyCode(d.getCurrencyCode());
        e.setExchangeRate(d.getExchangeRate());
        e.setPaymentMethod(d.getPaymentMethod().name());
        e.setStatus(d.getStatus().name());
        e.setBankAccountRef(d.getBankAccountRef());
        e.setReferenceNumber(d.getReferenceNumber());
        return e;
    }

    public static Payment toDomain(PaymentJpaEntity e) {
        return new Payment(
                PaymentId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                e.getPaymentNumber(),
                PaymentType.valueOf(e.getPaymentType()),
                e.getInvoiceId(),
                e.getCustomerId(),
                e.getPaymentDate(),
                e.getAmount(),
                e.getCurrencyCode(),
                e.getExchangeRate(),
                PaymentMethod.valueOf(e.getPaymentMethod()),
                PaymentStatus.valueOf(e.getStatus()),
                e.getBankAccountRef(),
                e.getReferenceNumber()
        );
    }
}
