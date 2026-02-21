package tr.kontas.erp.shipment.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.application.port.DeliveryOrderNumberGeneratorPort;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderNumber;
import tr.kontas.erp.shipment.platform.persistence.deliveryorder.JpaDeliveryOrderRepository;

@Component
@RequiredArgsConstructor
public class DeliveryOrderNumberGeneratorImpl implements DeliveryOrderNumberGeneratorPort {

    private final JpaDeliveryOrderRepository jpaRepository;

    @Override
    public DeliveryOrderNumber generate(TenantId tenantId, CompanyId companyId, int year) {
        int nextSeq = jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID()) + 1;
        String value = "DO-%04d-%06d".formatted(year, nextSeq);
        return new DeliveryOrderNumber(value);
    }
}

