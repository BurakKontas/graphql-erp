package tr.kontas.erp.shipment.application.shipment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.shipment.domain.shipment.Shipment;

import java.util.List;

public interface GetShipmentsByCompanyUseCase {
    List<Shipment> execute(CompanyId companyId);
}

