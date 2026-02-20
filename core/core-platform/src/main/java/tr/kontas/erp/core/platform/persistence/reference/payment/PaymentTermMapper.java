package tr.kontas.erp.core.platform.persistence.reference.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermCode;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

public class PaymentTermMapper {

    public static PaymentTerm toDomain(PaymentTermJpaEntity entity) {
        PaymentTerm paymentTerm = new PaymentTerm(
                new PaymentTermCode(entity.getCode()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                entity.getName(),
                entity.getDueDays()
        );
        if (!entity.isActive()) {
            paymentTerm.deactivate();
        }
        return paymentTerm;
    }

    public static PaymentTermJpaEntity toEntity(PaymentTerm domain) {
        return PaymentTermJpaEntity.builder()
                .code(domain.getId().getValue())
                .tenantId(domain.getTenantId().asUUID())
                .companyId(domain.getCompanyId().asUUID())
                .name(domain.getName())
                .dueDays(domain.getDueDays())
                .active(domain.isActive())
                .build();
    }
}
