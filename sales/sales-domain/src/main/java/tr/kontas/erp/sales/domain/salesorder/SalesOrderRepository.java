package tr.kontas.erp.sales.domain.salesorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface SalesOrderRepository {
    void save(SalesOrder order);
    Optional<SalesOrder> findById(SalesOrderId id, TenantId tenantId);
    Optional<SalesOrder> findByOrderNumber(SalesOrderNumber orderNumber, TenantId tenantId);
    boolean existsByOrderNumber(SalesOrderNumber orderNumber, TenantId tenantId);
    List<SalesOrder> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<SalesOrder> findDraftsByCompanyId(TenantId tenantId, CompanyId companyId);
    List<SalesOrder> findByIds(List<SalesOrderId> ids);
}

