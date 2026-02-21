package tr.kontas.erp.shipment.domain.shipmentreturn;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ShipmentReturnRepository {
    void save(ShipmentReturn shipmentReturn);
    Optional<ShipmentReturn> findById(ShipmentReturnId id, TenantId tenantId);
    Optional<ShipmentReturn> findByNumber(ShipmentReturnNumber number, TenantId tenantId);
    boolean existsByNumber(ShipmentReturnNumber number, TenantId tenantId);
    List<ShipmentReturn> findByShipmentId(String shipmentId, TenantId tenantId);
    List<ShipmentReturn> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<ShipmentReturn> findByIds(List<ShipmentReturnId> ids);
}

