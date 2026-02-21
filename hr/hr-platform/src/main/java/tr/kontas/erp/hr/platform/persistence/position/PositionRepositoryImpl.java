package tr.kontas.erp.hr.platform.persistence.position;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.position.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl implements PositionRepository {

    private final JpaPositionRepository jpaRepository;

    @Override
    public void save(Position entity) {
        jpaRepository.save(PositionMapper.toEntity(entity));
    }

    @Override
    public Optional<Position> findById(PositionId id, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndId(tenantId.asUUID(), id.asUUID())
                .stream().findFirst().map(PositionMapper::toDomain);
    }

    @Override
    public List<Position> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(PositionMapper::toDomain).toList();
    }

    @Override
    public List<Position> findByIds(List<PositionId> ids) {
        List<UUID> uuids = ids.stream().map(PositionId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream().map(PositionMapper::toDomain).toList();
    }
}
