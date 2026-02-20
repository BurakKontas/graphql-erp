package tr.kontas.erp.core.platform.persistence.reference.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermCode;
import tr.kontas.erp.core.domain.reference.payment.PaymentTermRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentTermRepositoryImpl implements PaymentTermRepository {

    private final JpaPaymentTermRepository jpaRepository;

    @Override
    public Optional<PaymentTerm> findByCode(TenantId tenantId, CompanyId companyId, PaymentTermCode code) {
        return jpaRepository.findByTenantIdAndCompanyIdAndCode(
                tenantId.asUUID(), companyId.asUUID(), code.getValue()
        ).map(PaymentTermMapper::toDomain);
    }

    @Override
    public List<PaymentTerm> findByCompany(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(PaymentTermMapper::toDomain)
                .toList();
    }

    public List<PaymentTerm> findByCompanyIds(TenantId tenantId, List<CompanyId> companyIds) {
        List<UUID> ids = companyIds.stream().map(CompanyId::asUUID).toList();
        return jpaRepository.findByTenantIdAndCompanyIdIn(tenantId.asUUID(), ids)
                .stream()
                .map(PaymentTermMapper::toDomain)
                .toList();
    }

    public List<PaymentTerm> findByCodes(List<PaymentTermCode> codes) {
        List<String> codeStrings = codes.stream()
                .map(PaymentTermCode::getValue)
                .toList();
        return jpaRepository.findByCodeIn(codeStrings)
                .stream()
                .map(PaymentTermMapper::toDomain)
                .toList();
    }

    @Override
    public void save(PaymentTerm paymentTerm) {
        jpaRepository.save(PaymentTermMapper.toEntity(paymentTerm));
    }
}
