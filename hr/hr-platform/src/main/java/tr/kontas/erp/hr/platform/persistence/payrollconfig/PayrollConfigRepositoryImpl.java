package tr.kontas.erp.hr.platform.persistence.payrollconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.payrollconfig.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PayrollConfigRepositoryImpl implements PayrollConfigRepository {

    private final JpaPayrollConfigRepository jpaRepository;

    @Override
    public void save(PayrollConfig entity) {
        jpaRepository.save(PayrollConfigMapper.toEntity(entity));
    }

    @Override
    public Optional<PayrollConfig> findById(PayrollConfigId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(PayrollConfigMapper::toDomain);
    }

    @Override
    public List<PayrollConfig> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(PayrollConfigMapper::toDomain).toList();
    }

    @Override
    public List<PayrollConfig> findByIds(List<PayrollConfigId> ids) {
        List<UUID> uuids = ids.stream().map(PayrollConfigId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(PayrollConfigMapper::toDomain).toList();
    }
}
