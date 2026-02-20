package tr.kontas.erp.core.domain.reference.payment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PaymentTermRepository {
    Optional<PaymentTerm> findByCode(TenantId tenantId, CompanyId companyId, PaymentTermCode code);

    List<PaymentTerm> findByCompany(TenantId tenantId, CompanyId companyId);

    void save(PaymentTerm paymentTerm);
}