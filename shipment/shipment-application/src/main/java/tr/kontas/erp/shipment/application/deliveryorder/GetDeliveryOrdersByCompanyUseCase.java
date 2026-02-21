package tr.kontas.erp.shipment.application.deliveryorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrder;

import java.util.List;

public interface GetDeliveryOrdersByCompanyUseCase {
    List<DeliveryOrder> execute(CompanyId companyId);
}

