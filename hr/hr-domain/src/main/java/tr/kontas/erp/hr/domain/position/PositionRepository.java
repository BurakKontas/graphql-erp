package tr.kontas.erp.hr.domain.position;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PositionRepository {
    void save(Position entity);
    Optional<Position> findById(PositionId id, TenantId tenantId);
    List<Position> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Position> findByIds(List<PositionId> ids);
}
