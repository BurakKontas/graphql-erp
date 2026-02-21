package tr.kontas.erp.shipment.platform.persistence.deliveryorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.deliveryorder.*;

import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderMapper {

    public static DeliveryOrderJpaEntity toEntity(DeliveryOrder domain) {
        DeliveryOrderJpaEntity e = new DeliveryOrderJpaEntity();
        e.setId(domain.getId().asUUID());
        e.setTenantId(domain.getTenantId().asUUID());
        e.setCompanyId(domain.getCompanyId().asUUID());
        e.setNumber(domain.getNumber().getValue());
        e.setSalesOrderId(domain.getSalesOrderId());
        e.setSalesOrderNumber(domain.getSalesOrderNumber());
        e.setCustomerId(domain.getCustomerId());
        e.setStatus(domain.getStatus().name());

        Address a = domain.getShippingAddress();
        if (a != null) {
            e.setAddressLine1(a.getAddressLine1());
            e.setAddressLine2(a.getAddressLine2());
            e.setCity(a.getCity());
            e.setStateOrProvince(a.getStateOrProvince());
            e.setPostalCode(a.getPostalCode());
            e.setCountryCode(a.getCountryCode());
        }

        List<DeliveryOrderLineJpaEntity> lineEntities = new ArrayList<>();
        for (DeliveryOrderLine line : domain.getLines()) {
            DeliveryOrderLineJpaEntity le = new DeliveryOrderLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setDeliveryOrder(e);
            le.setSalesOrderLineId(line.getSalesOrderLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setOrderedQty(line.getOrderedQty());
            le.setShippedQty(line.getShippedQty());
            lineEntities.add(le);
        }
        e.setLines(lineEntities);
        return e;
    }

    public static DeliveryOrder toDomain(DeliveryOrderJpaEntity e) {
        Address address = null;
        if (e.getAddressLine1() != null) {
            address = new Address(
                    e.getAddressLine1(), e.getAddressLine2(),
                    e.getCity(), e.getStateOrProvince(),
                    e.getPostalCode(), e.getCountryCode()
            );
        }

        List<DeliveryOrderLine> lines = e.getLines().stream()
                .map(le -> new DeliveryOrderLine(
                        DeliveryOrderLineId.of(le.getId()),
                        le.getSalesOrderLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getOrderedQty(),
                        le.getShippedQty()
                ))
                .toList();

        return new DeliveryOrder(
                DeliveryOrderId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new DeliveryOrderNumber(e.getNumber()),
                e.getSalesOrderId(),
                e.getSalesOrderNumber(),
                e.getCustomerId(),
                address,
                DeliveryOrderStatus.valueOf(e.getStatus()),
                lines
        );
    }
}

