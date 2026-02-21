package tr.kontas.erp.finance.domain.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findById(PaymentId id, TenantId tenantId);
    List<Payment> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Payment> findByInvoiceId(TenantId tenantId, String invoiceId);
    List<Payment> findByIds(List<PaymentId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}

