package tr.kontas.erp.shipment.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnNumber;

public interface ShipmentReturnNumberGeneratorPort {
    ShipmentReturnNumber generate(TenantId tenantId, CompanyId companyId, int year);
}

