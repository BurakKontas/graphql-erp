package tr.kontas.erp.shipment.domain.shipment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository {
    void save(Shipment shipment);
    Optional<Shipment> findById(ShipmentId id, TenantId tenantId);
    Optional<Shipment> findByNumber(ShipmentNumber number, TenantId tenantId);
    boolean existsByNumber(ShipmentNumber number, TenantId tenantId);
    List<Shipment> findByDeliveryOrderId(String deliveryOrderId, TenantId tenantId);
    List<Shipment> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Shipment> findByIds(List<ShipmentId> ids);
}

