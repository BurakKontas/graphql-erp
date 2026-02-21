package tr.kontas.erp.crm.platform.persistence.opportunity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.opportunity.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OpportunityRepositoryImpl implements OpportunityRepository {

    private final JpaOpportunityRepository jpa;

    @Override
    public void save(Opportunity entity) {
        jpa.save(OpportunityMapper.toEntity(entity));
    }

    @Override
    public Optional<Opportunity> findById(OpportunityId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(OpportunityMapper::toDomain);
    }

    @Override
    public List<Opportunity> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(OpportunityMapper::toDomain).toList();
    }

    @Override
    public List<Opportunity> findByIds(List<OpportunityId> ids) {
        return jpa.findByIdIn(ids.stream().map(OpportunityId::asUUID).toList())
                .stream().map(OpportunityMapper::toDomain).toList();
    }
}

