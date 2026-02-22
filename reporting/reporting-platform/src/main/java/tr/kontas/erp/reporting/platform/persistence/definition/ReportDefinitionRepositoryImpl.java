package tr.kontas.erp.reporting.platform.persistence.definition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.definition.ReportDefinition;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportDefinitionRepositoryImpl implements ReportDefinitionRepository {

    private final JpaReportDefinitionRepository jpaRepository;

    @Override
    public void save(ReportDefinition definition) {
        jpaRepository.save(ReportDefinitionMapper.toJpa(definition));
    }

    @Override
    public Optional<ReportDefinition> findById(ReportDefinitionId id, TenantId tenantId) {
        return jpaRepository.findById(id.asUUID()).map(ReportDefinitionMapper::toDomain);
    }

    @Override
    public List<ReportDefinition> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID()).stream()
                .map(ReportDefinitionMapper::toDomain)
                .toList();
    }

    @Override
    public List<ReportDefinition> findByIds(List<ReportDefinitionId> ids) {
        List<java.util.UUID> uuids = ids.stream().map(ReportDefinitionId::asUUID).toList();
        return jpaRepository.findAllById(uuids).stream()
                .map(ReportDefinitionMapper::toDomain)
                .toList();
    }
}

