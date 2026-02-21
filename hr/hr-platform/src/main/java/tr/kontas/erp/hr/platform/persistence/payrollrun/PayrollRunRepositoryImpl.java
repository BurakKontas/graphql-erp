package tr.kontas.erp.hr.platform.persistence.payrollrun;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.payrollrun.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PayrollRunRepositoryImpl implements PayrollRunRepository {

    private final JpaPayrollRunRepository jpaRepository;

    @Override
    public void save(PayrollRun entity) {
        jpaRepository.save(PayrollRunMapper.toEntity(entity));
    }

    @Override
    public Optional<PayrollRun> findById(PayrollRunId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(PayrollRunMapper::toDomain);
    }

    @Override
    public List<PayrollRun> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(PayrollRunMapper::toDomain).toList();
    }

    @Override
    public List<PayrollRun> findByIds(List<PayrollRunId> ids) {
        List<UUID> uuids = ids.stream().map(PayrollRunId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(PayrollRunMapper::toDomain).toList();
    }
}
