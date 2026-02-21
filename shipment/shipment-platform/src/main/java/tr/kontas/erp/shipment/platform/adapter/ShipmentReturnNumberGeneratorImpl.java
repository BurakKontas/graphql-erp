package tr.kontas.erp.shipment.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.application.port.ShipmentReturnNumberGeneratorPort;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnNumber;
import tr.kontas.erp.shipment.platform.persistence.shipmentreturn.JpaShipmentReturnRepository;

@Component
@RequiredArgsConstructor
public class ShipmentReturnNumberGeneratorImpl implements ShipmentReturnNumberGeneratorPort {

    private final JpaShipmentReturnRepository jpaRepository;

    @Override
    public ShipmentReturnNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "SR-%04d-%06d".formatted(year, nextSeq);
        return new ShipmentReturnNumber(value);
    }
}

