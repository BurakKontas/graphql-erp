package tr.kontas.erp.shipment.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.application.port.ShipmentNumberGeneratorPort;
import tr.kontas.erp.shipment.domain.shipment.ShipmentNumber;
import tr.kontas.erp.shipment.platform.persistence.shipment.JpaShipmentRepository;

@Component
@RequiredArgsConstructor
public class ShipmentNumberGeneratorImpl implements ShipmentNumberGeneratorPort {

    private final JpaShipmentRepository jpaRepository;

    @Override
    public ShipmentNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "SH-%04d-%06d".formatted(year, nextSeq);
        return new ShipmentNumber(value);
    }
}

