package tr.kontas.erp.shipment.platform.persistence.shipment;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipment.*;

import java.util.ArrayList;
import java.util.List;

public class ShipmentMapper {

    public static ShipmentJpaEntity toEntity(Shipment domain) {
        ShipmentJpaEntity e = new ShipmentJpaEntity();
        e.setId(domain.getId().asUUID());
        e.setTenantId(domain.getTenantId().asUUID());
        e.setCompanyId(domain.getCompanyId().asUUID());
        e.setNumber(domain.getNumber().getValue());
        e.setDeliveryOrderId(domain.getDeliveryOrderId());
        e.setSalesOrderId(domain.getSalesOrderId());
        e.setWarehouseId(domain.getWarehouseId());
        e.setTrackingNumber(domain.getTrackingNumber());
        e.setCarrierName(domain.getCarrierName());
        e.setStatus(domain.getStatus().name());
        e.setDispatchedAt(domain.getDispatchedAt());
        e.setDeliveredAt(domain.getDeliveredAt());

        Address a = domain.getShippingAddress();
        if (a != null) {
            e.setAddressLine1(a.getAddressLine1());
            e.setAddressLine2(a.getAddressLine2());
            e.setCity(a.getCity());
            e.setStateOrProvince(a.getStateOrProvince());
            e.setPostalCode(a.getPostalCode());
            e.setCountryCode(a.getCountryCode());
        }

        List<ShipmentLineJpaEntity> lineEntities = new ArrayList<>();
        for (ShipmentLine line : domain.getLines()) {
            ShipmentLineJpaEntity le = new ShipmentLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setShipment(e);
            le.setDeliveryOrderLineId(line.getDeliveryOrderLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setQuantity(line.getQuantity());
            lineEntities.add(le);
        }
        e.setLines(lineEntities);
        return e;
    }

    public static Shipment toDomain(ShipmentJpaEntity e) {
        Address address = null;
        if (e.getAddressLine1() != null) {
            address = new Address(
                    e.getAddressLine1(), e.getAddressLine2(),
                    e.getCity(), e.getStateOrProvince(),
                    e.getPostalCode(), e.getCountryCode()
            );
        }

        List<ShipmentLine> lines = e.getLines().stream()
                .map(le -> new ShipmentLine(
                        ShipmentLineId.of(le.getId()),
                        le.getDeliveryOrderLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity()
                ))
                .toList();

        return new Shipment(
                ShipmentId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new ShipmentNumber(e.getNumber()),
                e.getDeliveryOrderId(),
                e.getSalesOrderId(),
                e.getWarehouseId(),
                address,
                e.getTrackingNumber(),
                e.getCarrierName(),
                ShipmentStatus.valueOf(e.getStatus()),
                e.getDispatchedAt(),
                e.getDeliveredAt(),
                lines
        );
    }
}

