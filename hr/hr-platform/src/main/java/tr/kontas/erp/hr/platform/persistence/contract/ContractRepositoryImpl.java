package tr.kontas.erp.hr.platform.persistence.contract;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.contract.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepository {

    private final JpaContractRepository jpaRepository;

    @Override
    public void save(Contract entity) {
        jpaRepository.save(ContractMapper.toEntity(entity));
    }

    @Override
    public Optional<Contract> findById(ContractId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(ContractMapper::toDomain);
    }

    @Override
    public List<Contract> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(ContractMapper::toDomain).toList();
    }

    @Override
    public List<Contract> findByIds(List<ContractId> ids) {
        List<UUID> uuids = ids.stream().map(ContractId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(ContractMapper::toDomain).toList();
    }
}
