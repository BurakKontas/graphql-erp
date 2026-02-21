package tr.kontas.erp.shipment.application.port;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderNumber;

public interface DeliveryOrderNumberGeneratorPort {
    DeliveryOrderNumber generate(TenantId tenantId, CompanyId companyId, int year);
}

