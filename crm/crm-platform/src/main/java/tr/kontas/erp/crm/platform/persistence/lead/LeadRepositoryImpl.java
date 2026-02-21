package tr.kontas.erp.crm.platform.persistence.lead;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.lead.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LeadRepositoryImpl implements LeadRepository {

    private final JpaLeadRepository jpa;

    @Override
    public void save(Lead entity) {
        jpa.save(LeadMapper.toEntity(entity));
    }

    @Override
    public Optional<Lead> findById(LeadId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(LeadMapper::toDomain);
    }

    @Override
    public List<Lead> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(LeadMapper::toDomain).toList();
    }

    @Override
    public List<Lead> findByIds(List<LeadId> ids) {
        return jpa.findByIdIn(ids.stream().map(LeadId::asUUID).toList())
                .stream().map(LeadMapper::toDomain).toList();
    }
}

