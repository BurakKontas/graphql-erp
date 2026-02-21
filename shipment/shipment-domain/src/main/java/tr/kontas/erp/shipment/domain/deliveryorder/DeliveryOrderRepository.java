package tr.kontas.erp.shipment.domain.deliveryorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface DeliveryOrderRepository {
    void save(DeliveryOrder order);
    Optional<DeliveryOrder> findById(DeliveryOrderId id, TenantId tenantId);
    Optional<DeliveryOrder> findByNumber(DeliveryOrderNumber number, TenantId tenantId);
    boolean existsByNumber(DeliveryOrderNumber number, TenantId tenantId);
    List<DeliveryOrder> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<DeliveryOrder> findBySalesOrderId(String salesOrderId, TenantId tenantId);
    List<DeliveryOrder> findByIds(List<DeliveryOrderId> ids);
}

