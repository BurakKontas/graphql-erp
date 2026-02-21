package tr.kontas.erp.shipment.platform.persistence.shipmentreturn;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipmentreturn.*;

import java.util.ArrayList;
import java.util.List;

public class ShipmentReturnMapper {

    public static ShipmentReturnJpaEntity toEntity(ShipmentReturn domain) {
        ShipmentReturnJpaEntity e = new ShipmentReturnJpaEntity();
        e.setId(domain.getId().asUUID());
        e.setTenantId(domain.getTenantId().asUUID());
        e.setCompanyId(domain.getCompanyId().asUUID());
        e.setNumber(domain.getNumber().getValue());
        e.setShipmentId(domain.getShipmentId());
        e.setSalesOrderId(domain.getSalesOrderId());
        e.setWarehouseId(domain.getWarehouseId());
        e.setReason(domain.getReason().getValue());
        e.setStatus(domain.getStatus().name());
        e.setReceivedAt(domain.getReceivedAt());

        List<ShipmentReturnLineJpaEntity> lineEntities = new ArrayList<>();
        for (ShipmentReturnLine line : domain.getLines()) {
            ShipmentReturnLineJpaEntity le = new ShipmentReturnLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setShipmentReturn(e);
            le.setShipmentLineId(line.getShipmentLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setQuantity(line.getQuantity());
            le.setLineReason(line.getLineReason());
            lineEntities.add(le);
        }
        e.setLines(lineEntities);
        return e;
    }

    public static ShipmentReturn toDomain(ShipmentReturnJpaEntity e) {
        List<ShipmentReturnLine> lines = e.getLines().stream()
                .map(le -> new ShipmentReturnLine(
                        ShipmentReturnLineId.of(le.getId()),
                        le.getShipmentLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity(),
                        le.getLineReason()
                ))
                .toList();

        return new ShipmentReturn(
                ShipmentReturnId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new ShipmentReturnNumber(e.getNumber()),
                e.getShipmentId(),
                e.getSalesOrderId(),
                e.getWarehouseId(),
                new ReturnReason(e.getReason()),
                ReturnStatus.valueOf(e.getStatus()),
                e.getReceivedAt(),
                lines
        );
    }
}

