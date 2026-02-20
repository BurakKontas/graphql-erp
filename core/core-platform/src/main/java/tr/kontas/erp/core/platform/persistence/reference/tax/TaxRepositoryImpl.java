package tr.kontas.erp.core.platform.persistence.reference.tax;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.reference.tax.TaxCode;
import tr.kontas.erp.core.domain.reference.tax.TaxRepository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaxRepositoryImpl implements TaxRepository {

    private final JpaTaxRepository jpaRepository;

    @Override
    public Optional<Tax> findByCode(TenantId tenantId, CompanyId companyId, TaxCode code) {
        return jpaRepository.findByTenantIdAndCompanyIdAndCode(
                tenantId.asUUID(), companyId.asUUID(), code.getValue()
        ).map(TaxMapper::toDomain);
    }

    @Override
    public List<Tax> findByCompany(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(TaxMapper::toDomain)
                .toList();
    }

    @Override
    public List<Tax> findAllActiveByCompany(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyIdAndActiveTrue(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(TaxMapper::toDomain)
                .toList();
    }

    public List<Tax> findByCompanyIds(TenantId tenantId, List<CompanyId> companyIds) {
        List<UUID> ids = companyIds.stream().map(CompanyId::asUUID).toList();
        return jpaRepository.findByTenantIdAndCompanyIdIn(tenantId.asUUID(), ids)
                .stream()
                .map(TaxMapper::toDomain)
                .toList();
    }

    public List<Tax> findByCodes(List<TaxCode> codes) {
        List<String> codeStrings = codes.stream()
                .map(TaxCode::getValue)
                .toList();
        return jpaRepository.findByCodeIn(codeStrings)
                .stream()
                .map(TaxMapper::toDomain)
                .toList();
    }

    @Override
    public void save(Tax tax) {
        jpaRepository.save(TaxMapper.toEntity(tax));
    }
}
