package tr.kontas.erp.finance.platform.persistence.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.payment.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpa;

    @Override
    public void save(Payment payment) {
        jpa.save(PaymentMapper.toEntity(payment));
    }

    @Override
    public Optional<Payment> findById(PaymentId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(PaymentMapper::toDomain);
    }

    @Override
    public List<Payment> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(PaymentMapper::toDomain).toList();
    }

    @Override
    public List<Payment> findByInvoiceId(TenantId tenantId, String invoiceId) {
        return jpa.findByTenantIdAndInvoiceId(tenantId.asUUID(), invoiceId)
                .stream().map(PaymentMapper::toDomain).toList();
    }

    @Override
    public List<Payment> findByIds(List<PaymentId> ids) {
        return jpa.findByIdIn(ids.stream().map(PaymentId::asUUID).toList())
                .stream().map(PaymentMapper::toDomain).toList();
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpa.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}
