package tr.kontas.erp.shipment.application.shipmentreturn;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturn;

import java.util.List;

public interface GetShipmentReturnsByCompanyUseCase {
    List<ShipmentReturn> execute(CompanyId companyId);
}

