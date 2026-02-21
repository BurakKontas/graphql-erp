package tr.kontas.erp.hr.platform.persistence.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.employee.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JpaEmployeeRepository jpaRepository;

    @Override
    public void save(Employee entity) {
        jpaRepository.save(EmployeeMapper.toEntity(entity));
    }

    @Override
    public Optional<Employee> findById(EmployeeId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(EmployeeMapper::toDomain);
    }

    @Override
    public List<Employee> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(EmployeeMapper::toDomain).toList();
    }

    @Override
    public List<Employee> findByIds(List<EmployeeId> ids) {
        List<UUID> uuids = ids.stream().map(EmployeeId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(EmployeeMapper::toDomain).toList();
    }
}
