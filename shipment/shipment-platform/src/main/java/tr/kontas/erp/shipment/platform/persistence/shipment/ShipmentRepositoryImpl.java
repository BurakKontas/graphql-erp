package tr.kontas.erp.shipment.platform.persistence.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipment.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final JpaShipmentRepository jpa;

    @Override
    public void save(Shipment shipment) {
        jpa.save(ShipmentMapper.toEntity(shipment));
    }

    @Override
    public Optional<Shipment> findById(ShipmentId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID()).map(ShipmentMapper::toDomain);
    }

    @Override
    public Optional<Shipment> findByNumber(ShipmentNumber number, TenantId tenantId) {
        return jpa.findByNumberAndTenantId(number.getValue(), tenantId.asUUID()).map(ShipmentMapper::toDomain);
    }

    @Override
    public boolean existsByNumber(ShipmentNumber number, TenantId tenantId) {
        return jpa.existsByNumberAndTenantId(number.getValue(), tenantId.asUUID());
    }

    @Override
    public List<Shipment> findByDeliveryOrderId(String deliveryOrderId, TenantId tenantId) {
        return jpa.findByDeliveryOrderIdAndTenantId(deliveryOrderId, tenantId.asUUID())
                .stream().map(ShipmentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Shipment> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(ShipmentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Shipment> findByIds(List<ShipmentId> ids) {
        List<UUID> uuids = ids.stream().map(ShipmentId::asUUID).collect(Collectors.toList());
        return jpa.findByIdIn(uuids).stream().map(ShipmentMapper::toDomain).collect(Collectors.toList());
    }
}

